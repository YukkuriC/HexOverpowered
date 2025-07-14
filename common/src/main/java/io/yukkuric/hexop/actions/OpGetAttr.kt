package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import io.yukkuric.hexop.HexOPAttributes
import net.minecraft.world.entity.ai.attributes.Attribute

class OpGetAttr(private val attr: Attribute) : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val caster = env.caster
        return listOf(DoubleIota(caster?.getAttributeValue(attr) ?: 0.0))
    }

    companion object {
        val GetMana = OpGetAttr(HexOPAttributes.PERSONAL_MEDIA)
        val GetMaxMana = OpGetAttr(HexOPAttributes.PERSONAL_MEDIA_MAX)
    }
}