package io.yukkuric.hexop.actions.mind_env

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException
import io.yukkuric.hexop.ext.SilencedCastingEnv
import net.minecraft.server.MinecraftServer
import java.util.*

object OpScheduleCall : ConstMediaAction {
    class Signal(val code: SpellList) {
        var cancelled = false
    }

    class Task(
        val myAge: Int, val env: CastingEnvironment,
        val signal: Signal, val action: Runnable
    ) {
        fun execute(tick: Int): Boolean {
            if (tick < myAge) return false
            if (signal.cancelled) return true
            try {
                action.run()
            } catch (e: Exception) {
                val mishap = MishapInternalException(e)
                var msg = mishap.errorMessageWithName(env, Mishap.Context(null, null))
                if (env is CircleCastEnv) env.impetus?.postPrint(msg)
                else env.caster?.sendSystemMessage(msg)
            }
            return true
        }
    }

    private val SignalMap = WeakHashMap<Any?, Signal>()
    private val TaskQueue = LinkedList<Task>()

    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val code = args.getList(0)
        val delay = args.getInt(1)
        val action = Runnable {
            val vm = CastingVM.empty(SilencedCastingEnv.from(env))
            vm.queueExecuteAndWrapIotas(code.toList(), env.world)
        }
        if (delay <= 0) {
            action.run()
            return listOf()
        }

        val key = pickKeyFrom(env)
        val signal = Signal(code)
        val oldSignal = SignalMap.put(key, signal)
        oldSignal?.cancelled = true

        val server = env.world.server
        TaskQueue.addLast(Task(delay + server.tickCount, env, signal, action))
        return listOf()
    }

    private fun pickKeyFrom(env: CastingEnvironment) = if (env is CircleCastEnv) env.impetus else env.caster

    @JvmStatic
    fun ProcessQueue(server: MinecraftServer) {
        while (!TaskQueue.isEmpty()) {
            if (TaskQueue.first.execute(server.tickCount)) TaskQueue.removeFirst()
            else break
        }
    }

    fun QueryScheduledCode(env: CastingEnvironment): SpellList? {
        val key = pickKeyFrom(env)
        val signal = SignalMap[key]
        if (signal == null || signal.cancelled) return null
        return signal.code
    }
}