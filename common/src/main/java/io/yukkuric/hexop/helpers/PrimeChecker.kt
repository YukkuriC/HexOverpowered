package io.yukkuric.hexop.helpers

// I assume this works
object PrimeChecker {
    const val MAX_POOL_TARGET = 46340 // int(sqrt(INT_MAX))
    private var cachedUntil = 2
    private val poolSet = HashSet<Int>()

    init {
        poolSet.add(2)
    }

    // always call after squared
    private fun extendTo(target: Int) {
        val target = target.coerceAtMost(MAX_POOL_TARGET)
        if (cachedUntil >= target) return
        val cacheStart = cachedUntil + 1
        val newPool = BooleanArray(target + 1)
        newPool.fill(true, cacheStart)
        for (i in poolSet) {
            newPool[i] = true
            for (j in ((cacheStart / i) * i).coerceAtLeast(i * i)..target step i) newPool[j] = false
        }
        for (i in cacheStart..target) {
            if (newPool[i]) {
                for (j in i * i..target step i) newPool[j] = false
                poolSet.add(i)
            }
        }
        cachedUntil = target
    }

    @Synchronized
    fun isPrime(num: Int): Boolean {
        if (num < 2) return false
        if (num <= MAX_POOL_TARGET) {
            if (num > cachedUntil) extendTo((cachedUntil * cachedUntil).coerceAtLeast(num))
            return poolSet.contains(num)
        }
        extendTo(MAX_POOL_TARGET)
        for (p in poolSet) if (num % p == 0) return false
        return true
    }
}