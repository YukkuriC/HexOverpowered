package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.SpellList
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.getInt
import at.petrak.hexcasting.api.spell.getList
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexDir
import at.petrak.hexcasting.api.spell.math.HexPattern
import at.petrak.hexcasting.api.spell.mishaps.Mishap
import at.petrak.hexcasting.api.spell.mishaps.MishapError
import at.petrak.hexcasting.api.spell.mishaps.MishapEvalTooDeep
import net.minecraft.server.MinecraftServer
import java.util.*

object OpScheduleCall : ConstMediaAction {
    class Signal(val code: SpellList) {
        var cancelled = false
    }

    val lazyMyPattern = lazy {
        HexPattern.fromAngles("waawedaqqqqdeaqq", HexDir.SOUTH_WEST)
    }

    class Task(
        val myAge: Int, val env: CastingContext,
        val signal: Signal, val action: Runnable
    ) {
        fun execute(tick: Int): Boolean {
            if (tick < myAge) return false
            if (signal.cancelled) return true
            try {
                action.run()
            } catch (e: Exception) {
                val mishap = MishapError(e)
                var msg = mishap.errorMessage(env, Mishap.Context(lazyMyPattern.value, null))
                env.caster?.sendSystemMessage(msg)
            }
            return true
        }
    }

    private val SignalMap = WeakHashMap<Any?, Signal>()
    private val TaskQueue = PriorityQueue<Task> { ta, tb -> ta.myAge - tb.myAge }

    override val argc = 2
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val env = ctx
        val code = args.getList(0)
        val delay = args.getInt(1)
        val action = Runnable {
            val vm = CastingHarness(env)
            vm.executeIotas(code.toList(), env.world)
        }
        if (delay <= 0) {
            try {
                action.run()
            } catch (e: StackOverflowError) {
                throw MishapEvalTooDeep()
            }
            return listOf()
        }

        val key = pickKeyFrom(env)
        val signal = Signal(code)
        SignalMap.put(key, signal)?.cancelled = true

        val server = env.world.server
        TaskQueue.add(Task(delay + server.tickCount, env, signal, action))
        return listOf()
    }

    private fun pickKeyFrom(env: CastingContext) = env.caster

    @JvmStatic
    fun ProcessQueue(server: MinecraftServer) {
        while (!TaskQueue.isEmpty()) {
            if (TaskQueue.peek().execute(server.tickCount)) TaskQueue.remove()
            else break
        }
    }
    @JvmStatic
    fun ResetQueue(server: MinecraftServer) = TaskQueue.clear()

    fun QueryScheduledCode(env: CastingContext): SpellList? {
        val key = pickKeyFrom(env)
        val signal = SignalMap[key]
        if (signal == null || signal.cancelled) return null
        return signal.code
    }
}