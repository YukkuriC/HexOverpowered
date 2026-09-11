package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.OperationResult
import at.petrak.hexcasting.api.spell.SpellList
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation
import at.petrak.hexcasting.api.spell.getInt
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import at.petrak.hexcasting.api.spell.mishaps.MishapDisallowedSpell
import at.petrak.hexcasting.api.spell.mishaps.MishapError
import at.petrak.hexcasting.api.spell.mishaps.MishapEvalTooDeep
import io.yukkuric.hexop.HexOPConfig
import net.minecraft.server.MinecraftServer
import java.util.*

const val USELESS_CALL_THRESHOLD = 10
const val MAX_CALL_THRESHOLD = 100000

// test 1: (mind_env/running_code,print,num_3,mind_env/schedule)num_3,mind_env/schedule // forever
// test 2: (print,(),num_0,mind_env/schedule)#my_aim,raycast,for_range/floodfill // stops at 1000 for size exceeded
// test 3: (print,(),num_1,mind_env/schedule)#my_aim,raycast,for_range/floodfill // stops at 10 executions
// test 4: (read/local,print,num_0,mind_env/schedule)write/local,read/local,num_0,mind_env/schedule // stops at 1e5 exec
object OpScheduleCall : ConstMediaAction {
    class SameTickCallCounter(val limit: Int) {
        var lastChangeAge = -1
        var calledCount = 0
        fun update(now: Int) {
            if (now == lastChangeAge) {
                calledCount++
                if (calledCount >= limit) {
                    calledCount = 0
                    throw MishapEvalTooDeep()
                }
            } else {
                calledCount = 1
            }
            lastChangeAge = now
        }
    }

    class TaskPool(val key: Any?) {
        val queue = ArrayDeque<Task>()

        fun cancel(): Boolean {
            val ret = queue.any { !it.executed }
            queue.clear()
            return ret
        }

        fun execute(tick: Int) {
            val peek = queue.peekFirst() ?: return
            if (peek.execute(tick)) {
                val newPeek = queue.peekFirst()
                if (peek === newPeek) queue.pollFirst()
            }
        }

        val counterNormal = SameTickCallCounter(MAX_CALL_THRESHOLD)
        val counterDumb = SameTickCallCounter(USELESS_CALL_THRESHOLD)
    }

    val lazyMyPattern = lazy {
        HexPattern.fromAngles("waawedaqqqqdeaqq", HexDir.SOUTH_WEST)
    }

    class Task(
        val myAge: Int, val env: CastingContext,
        val code: SpellList, val action: (now: Int) -> Unit,
    ) {
        var executed = false
        fun execute(tick: Int): Boolean {
            if (tick < myAge) return false
            try {
                action(tick)
            } catch (e: Exception) {
                val mishap = MishapError(e)
                val msg = mishap.errorMessage(env, Mishap.Context(lazyMyPattern.value, null))
                env.caster?.sendSystemMessage(msg)
            }
            executed = true
            return true
        }
    }

    private val TaskMap = WeakHashMap<Any?, TaskPool>()

    // extracting image
    var myRaven: Iota? = null
    override fun operate(
        continuation: SpellContinuation,
        stack: MutableList<Iota>,
        ravenmind: Iota?,
        ctx: CastingContext
    ): OperationResult {
        myRaven = ravenmind
        return super.operate(continuation, stack, ravenmind, ctx)
    }

    override val argc = 2
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        if (!HexOPConfig.EnablesMindEnvActions()) throw MishapDisallowedSpell()
        val env = ctx

        // prepare all
        val tickThisExec = env.world.server.tickCount
        val key = pickKeyFrom(env)
        val code = args[0].let { topIota ->
            if (topIota is ListIota) topIota.list
            else SpellList.LList(listOf(topIota))
        }
        val delay = args.getInt(1)
        val ravenDataATM = myRaven
        val taskPool = TaskMap.computeIfAbsent(key, ::TaskPool)
        val action: (Int) -> Unit = { tick ->
            taskPool.counterNormal.update(tick)
            val vm = CastingHarness(env)
            vm.ravenmind = ravenDataATM
            vm.executeIotas(code.toList(), env.world)
        }

        // remove old
        if (taskPool.cancel()) taskPool.counterDumb.update(tickThisExec)

        // 0t call
        if (delay <= 0) {
            try {
                action.invoke(tickThisExec)
            } catch (e: StackOverflowError) {
                throw MishapEvalTooDeep()
            }
            return listOf()
        }

        // append new
        taskPool.queue.addLast(Task(delay + tickThisExec, env, code, action))

        // end
        return listOf()
    }

    private fun pickKeyFrom(env: CastingContext) = env.caster

    @JvmStatic
    fun ProcessQueue(server: MinecraftServer) {
        TaskMap.forEach { (_, task) ->
            task.execute(server.tickCount)
        }
    }
    @JvmStatic
    fun ResetQueue(server: MinecraftServer) {
        TaskMap.clear()
    }

    fun QueryScheduledCode(env: CastingContext): SpellList? {
        val key = pickKeyFrom(env)
        val pool = TaskMap[key]
        return pool?.queue?.peekFirst()?.code
    }
}