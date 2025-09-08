package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.DoubleIota
import at.petrak.hexcasting.api.spell.iota.Iota
import io.yukkuric.hexop.HexOPAttributes
import net.minecraft.world.entity.ai.attributes.Attribute

class OpGetAttr(attr: () -> Attribute) : ConstMediaAction {
    override val argc = 0
    private val attrGetter = lazy(attr)

    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        val caster = ctx.caster
        return listOf(DoubleIota(caster.getAttributeValue(attrGetter.value)))
    }

    companion object {
        val GetMana = OpGetAttr { HexOPAttributes.PERSONAL_MEDIA }
        val GetMaxMana = OpGetAttr { HexOPAttributes.PERSONAL_MEDIA_MAX }
    }
}