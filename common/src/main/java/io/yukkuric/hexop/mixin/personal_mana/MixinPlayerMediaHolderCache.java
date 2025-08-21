package io.yukkuric.hexop.mixin.personal_mana;

import io.yukkuric.hexop.personal_mana.PersonalManaHolder;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class MixinPlayerMediaHolderCache implements PersonalManaHolder.PlayerHolder {
    private PersonalManaHolder _cachedHolder;

    @Override
    public PersonalManaHolder getPersonalMediaHolder() {
        if (_cachedHolder == null) _cachedHolder = new PersonalManaHolder(Player.class.cast(this));
        return _cachedHolder;
    }
}
