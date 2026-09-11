package io.yukkuric.hexop.ext

class SilencedCastingEnv {
    companion object {
        // not supported in 1.19
        fun <T> from(env: T) = env
    }
}