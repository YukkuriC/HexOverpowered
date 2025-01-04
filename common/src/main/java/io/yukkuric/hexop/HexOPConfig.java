package io.yukkuric.hexop;

public class HexOPConfig {
    static API imp;

    public static final String DESCRIP_MOTE_GLANCE = "Enables 'A Glance of Mote Chest' (a simple chest GUI) on right click";

    public static void bindConfigImp(API api) {
        imp = api;
    }

    public static boolean EnablesMoteChestGUI() {
        return imp.EnablesMoteChestGUI();
    }

    public interface API {
        boolean EnablesMoteChestGUI();
    }
}
