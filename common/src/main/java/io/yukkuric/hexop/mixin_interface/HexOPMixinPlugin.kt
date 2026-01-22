package io.yukkuric.hexop.mixin_interface

import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class HexOPMixinPlugin : IMixinConfigPlugin {
    private val modCheckTargets = mapOf(
        Pair("hexcellular", "miyucomics.hexcellular.PropertyIota"),
        Pair("hexal", "ram.talia.hexal.common.blocks.entity.BlockEntityMediafiedStorage")
    )

    override fun shouldApplyMixin(targetCls: String, mixinCls: String): Boolean {
        for (modid in modCheckTargets) if (mixinCls.contains(modid.key) && !classExists(modid.value)) return false
        return true
    }

    private fun classExists(path: String): Boolean {
        val resourcePath = path.replace('.', '/') + ".class"
        val classLoader = Thread.currentThread().contextClassLoader
        val resource = classLoader.getResource(resourcePath)
        return resource != null
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