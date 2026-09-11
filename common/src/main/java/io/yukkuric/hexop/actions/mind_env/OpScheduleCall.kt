package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapEvalTooMuch
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.utils.TreeList
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.ext.SilencedCastingEnv
import io.yukkuric.hexop.legacy.MishapDisallowedSpell
import io.yukkuric.hexop.legacy.caster
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
                    throw MishapEvalTooMuch()
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

    class Task(
        val myAge: Int, val env: CastingEnvironment,
        val code: TreeList<Iota>, val action: (now: Int) -> Unit,
    ) {
        var executed = false
        fun execute(tick: Int): Boolean {
            if (tick < myAge) return false
            try {
                action(tick)
            } catch (e: Exception) {
                val mishap = MishapInternalException(e)
                val msg = mishap.errorMessageWithName(env, Mishap.Context(null, null))
                if (env is CircleCastEnv) env.impetus?.postPrint(msg)
                else env.caster?.sendSystemMessage(msg)
            }
            executed = true
            return true
        }
    }

    private val TaskMap = WeakHashMap<Any?, TaskPool>()

    // extracting image
    lateinit var myImage: CastingImage
    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        myImage = image
        return super.operate(env, image, continuation)
    }

    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (!HexOPConfig.EnablesMindEnvActions()) throw MishapDisallowedSpell()

        // prepare all
        val tickThisExec = env.world.server.tickCount
        val key = pickKeyFrom(env)
        val code = args[0].let { topIota ->
            if (topIota is ListIota) topIota.list
            else if (topIota.executable()) TreeList.from(listOf(topIota))
            else throw MishapInvalidIota.of(topIota, 0, "evaluatable")
        }
        val delay = args.getInt(1)
        val ravenDataATM = myImage.userData.get(HexAPI.RAVENMIND_USERDATA)
        val taskPool = TaskMap.computeIfAbsent(key, ::TaskPool)
        val action: (Int) -> Unit = { tick ->
            taskPool.counterNormal.update(tick)
            val vm = CastingVM.empty(SilencedCastingEnv.from(env))
            ravenDataATM?.let { vm.image.userData.put(HexAPI.RAVENMIND_USERDATA, it) }
            vm.queueExecuteAndWrapIotas(code.toList(), env.world)
        }

        // remove old
        if (taskPool.cancel()) taskPool.counterDumb.update(tickThisExec)

        // 0t call
        if (delay <= 0) {
            try {
                action.invoke(tickThisExec)
            } catch (e: StackOverflowError) {
                throw MishapEvalTooMuch()
            }
            return listOf()
        }

        // append new
        taskPool.queue.addLast(Task(delay + tickThisExec, env, code, action))

        // end
        return listOf()
    }

    private fun pickKeyFrom(env: CastingEnvironment) = if (env is CircleCastEnv) env.impetus else env.caster

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

    fun QueryScheduledCode(env: CastingEnvironment): TreeList<Iota>? {
        val key = pickKeyFrom(env)
        val pool = TaskMap[key]
        return pool?.queue?.peekFirst()?.code
    }
}