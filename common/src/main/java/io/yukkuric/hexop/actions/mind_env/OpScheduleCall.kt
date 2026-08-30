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
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.NumericTag
import net.minecraft.server.MinecraftServer
import java.util.*

const val USERDATA_USELESS_CALL = "hexop:dumbass_score"
const val USELESS_CALL_THRESHOLD = 10

object OpScheduleCall : ConstMediaAction {
    class Signal(val code: TreeList<Iota>) {
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
    private val TaskQueue = PriorityQueue<Task> { ta, tb -> ta.myAge - tb.myAge }

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
        val code = args[0].let { topIota ->
            if (topIota is ListIota) topIota.list
            else if (topIota.executable()) TreeList.from(listOf(topIota))
            else throw MishapInvalidIota.of(topIota, 0, "evaluatable")
        }
        val delay = args.getInt(1)
        val ravenDataATM = myImage.userData.get(HexAPI.RAVENMIND_USERDATA)
        val action = Runnable {
            val vm = CastingVM.empty(SilencedCastingEnv.from(env))
            ravenDataATM?.let { vm.image.userData.put(HexAPI.RAVENMIND_USERDATA, it) }
            myImage.userData[USERDATA_USELESS_CALL]?.let { vm.image.userData.put(USERDATA_USELESS_CALL, it) }
            vm.queueExecuteAndWrapIotas(code.toList(), env.world)
        }
        if (delay <= 0) {
            try {
                action.run()
            } catch (e: StackOverflowError) {
                throw MishapEvalTooMuch()
            }
            return listOf()
        }

        val key = pickKeyFrom(env)
        val signal = Signal(code)
        SignalMap.put(key, signal)?.let {
            it.cancelled = true
            val dumbCount = 1 + ((myImage.userData.get(USERDATA_USELESS_CALL) as? NumericTag)?.asInt ?: 0)
            myImage.userData.put(USERDATA_USELESS_CALL, IntTag.valueOf(dumbCount))
            if (dumbCount >= USELESS_CALL_THRESHOLD) throw MishapEvalTooMuch()
        }

        val server = env.world.server
        TaskQueue.add(Task(delay + server.tickCount, env, signal, action))
        return listOf()
    }

    private fun pickKeyFrom(env: CastingEnvironment) = if (env is CircleCastEnv) env.impetus else env.caster

    @JvmStatic
    fun ProcessQueue(server: MinecraftServer) {
        while (!TaskQueue.isEmpty()) {
            if (TaskQueue.peek().execute(server.tickCount)) TaskQueue.remove()
            else break
        }
    }
    @JvmStatic
    fun ResetQueue(server: MinecraftServer) = TaskQueue.clear()

    fun QueryScheduledCode(env: CastingEnvironment): TreeList<Iota>? {
        val key = pickKeyFrom(env)
        val signal = SignalMap[key]
        if (signal == null || signal.cancelled) return null
        return signal.code
    }
}