package io.yukkuric.hexop;

import java.util.ServiceLoader;

public abstract class HexOPXPlat {
    public static final HexOPXPlat INSTANCE = get();
    protected abstract boolean isModLoaded(String id);

    private static HexOPXPlat get() {
        var providers = ServiceLoader.load(HexOPXPlat.class).stream().toList();
        if (providers.size() != 1) {
            for (var p : providers) HexOverpowered.LOGGER.error(p.type().getName());
            throw new IllegalStateException("... multiple XPlat detected");
        } else {
            var provider = providers.get(0);
            return provider.get();
        }
    }
}
