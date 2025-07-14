package io.yukkuric.hexop.actions

import at.petrak.hexcasting.api.block.circle.BlockEntityAbstractImpetus
import at.petrak.hexcasting.api.item.MediaHolderItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import io.yukkuric.hexop.HexOPConfig
import io.yukkuric.hexop.HexOverpowered.IsModLoaded
import io.yukkuric.hexop.personal_mana.PersonalManaHolder
import net.minecraft.server.level.ServerPlayer
import ram.talia.hexal.api.spell.casting.IMixinCastingContext

object OpChargeMedia : BaseSelectAction(
    { _, _, _, _ -> HexOPConfig.EnablesChargeMediaAction() },
    OpChargeForReal,
    OpChargeFake,
) {
    private val hasHexal = lazy { IsModLoaded("hexal") }
    private const val MEDIA_TARGET = 114514 * MediaConstants.DUST_UNIT

    object OpChargeForReal : ConstMediaAction {
        override val argc = 0

        private fun rechargePersonalMedia(caster: ServerPlayer?) {
            val holder = PersonalManaHolder.get(caster) ?: return
            holder.media = MEDIA_TARGET.coerceAtMost(holder.maxMedia)
        }

        override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
            if (ctx.source == CastingContext.CastSource.PACKAGED_HEX || ctx.source == CastingContext.CastSource.STAFF)
                rechargePersonalMedia(ctx.caster)

            if (ctx.source == CastingContext.CastSource.PACKAGED_HEX) {
                val stack = ctx.caster.getItemInHand(ctx.castingHand)
                val item = stack.item
                if (item is MediaHolderItem) {
                    item.setMedia(stack, MEDIA_TARGET)
                }
            } else if (ctx.source == CastingContext.CastSource.SPELL_CIRCLE && ctx.spellCircle != null) {
                val src = ctx.world.getBlockEntity(ctx.spellCircle!!.impetusPos)
                if (src is BlockEntityAbstractImpetus) src.media = MEDIA_TARGET
            } else if (hasHexal.value && (ctx as Object) is IMixinCastingContext) {
                (ctx as IMixinCastingContext).wisp?.media = MEDIA_TARGET
            }
            return listOf()
        }
    }

    object OpChargeFake : ConstMediaAction {
        override val argc = 0

        override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
            // TODO: play sound
            return listOf()
        }
    }
}