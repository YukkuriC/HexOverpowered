package io.yukkuric.hexop;

public class HexOPConfig {
    static API imp;

    public static boolean loaded() {
        return imp != null;
    }

    public static final String DESCRIP_MOTE_GLANCE = "Enables 'A Glance of Mote Chest' (a simple chest GUI) on right click";
    public static final String DESCRIP_NO_YEET = "Mishaps won't drop items anymore";
    public static final String DESCRIP_TP_VEHICLES = "Teleporting riding entities no longer do mishaps";
    public static final String DESCRIP_MANA_ENABLE = "Enables player's personal media pool";
    public static final String DESCRIP_MANA_MAX = "Max media (in 0.0001 dust) for personal pool";
    public static final String DESCRIP_MANA_REGEN_STEP = "How many media points (in 0.0001 dust) personal pool regenerates each time";
    public static final String DESCRIP_MANA_REGEN_INTERVAL = "For every X ticks personal pool regenerate once";

    public static void bindConfigImp(API api) {
        imp = api;
    }

    public static boolean EnablesMoteChestGUI() {
        return imp.EnablesMoteChestGUI();
    }
    public static boolean EnablesMishapNoYeet() {
        return imp.EnablesMishapNoYeet();
    }
    public static boolean EnablesTeleportVehicles() {
        return imp.EnablesTeleportVehicles();
    }

    public static boolean DisablesPersonalMediaPool() {
        return !imp.EnablesPersonalMediaPool();
    }
    public static int PersonalMediaMax() {
        return imp.PersonalMediaMax();
    }
    public static int PersonalMediaRegenStep() {
        return imp.PersonalMediaRegenStep();
    }
    public static int PersonalMediaRegenInterval() {
        return imp.PersonalMediaRegenInterval();
    }

    public interface API {
        boolean EnablesMoteChestGUI();
        boolean EnablesMishapNoYeet();
        boolean EnablesTeleportVehicles();

        boolean EnablesPersonalMediaPool();
        int PersonalMediaMax();
        int PersonalMediaRegenStep();
        int PersonalMediaRegenInterval();
    }
}
