package io.yukkuric.hexop.mixin_interface

import io.yukkuric.hexop.HexOverpowered
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class HexOPMixinPlugin : IMixinConfigPlugin {
    private val modCheckTargets = listOf("hexcellular", "hexal")
    override fun shouldApplyMixin(targetCls: String, mixinCls: String): Boolean {
        for (modid in modCheckTargets) if (mixinCls.contains(modid) && !HexOverpowered.IsModLoaded(modid)) return false
        return true
    }

    override fun getRefMapperConfig() = null
    override fun getMixins() = null

    override fun onLoad(p0: String) {
    }

    override fun preApply(p0: String, p1: ClassNode, p2: String, p3: IMixinInfo) {
    }

    override fun postApply(p0: String, p1: ClassNode, p2: String, p3: IMixinInfo) {
    }

    override fun acceptTargets(p0: MutableSet<String>, p1: MutableSet<String>) {
    }
}