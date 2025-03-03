package io.yukkuric.hexop;

public class HexOPConfig {
    static API imp;

    public static final String DESCRIP_MOTE_GLANCE = "Enables 'A Glance of Mote Chest' (a simple chest GUI) on right click";
    public static final String DESCRIP_NO_YEET = "Mishaps won't drop items anymore";
    public static final String DESCRIP_TP_VEHICLES = "Teleporting riding entities no longer do mishaps";

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

    public interface API {
        boolean EnablesMoteChestGUI();

        boolean EnablesMishapNoYeet();

        boolean EnablesTeleportVehicles();
    }
}
