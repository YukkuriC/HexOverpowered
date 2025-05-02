package io.yukkuric.hexop;

public class HexOPConfig {
    static API imp;

    public static boolean loaded() {
        return imp != null;
    }
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
    public static boolean EnablesPersonalMediaPool() {
        return imp.EnablesPersonalMediaPool();
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
    public static boolean FakePlayerDontRegenMedia() {
        return imp.FakePlayerDontRegenMedia();
    }
    public static boolean EnablesAmethystCircle() {
        return imp.EnablesAmethystCircle();
    }
    public static int AmethystCircleSingleChargeCost() {
        return imp.AmethystCircleSingleChargeCost();
    }
    public static int AmethystCircleFullPowerLevel() {
        return imp.AmethystCircleFullPowerLevel();
    }
    public interface API {
        String desc_EnablesMoteChestGUI = "<Mote>\nEnables 'A Glance of Mote Chest' (a simple chest GUI) on right click";
        String desc_EnablesMishapNoYeet = "<Mishap>\nMishaps won't drop items anymore";
        String desc_EnablesTeleportVehicles = "<Mishap>\nTeleporting riding entities no longer do mishaps";
        String desc_EnablesPersonalMediaPool = "<Personal Media>\nEnables player's personal media pool";
        String desc_PersonalMediaMax = "<Personal Media>\nMax media (in 0.0001 dust) for personal pool";
        String desc_PersonalMediaRegenStep = "<Personal Media>\nHow many media points (in 0.0001 dust) personal pool regenerates each time";
        String desc_PersonalMediaRegenInterval = "<Personal Media>\nFor every X ticks personal pool regenerate once";
        String desc_FakePlayerDontRegenMedia = "<Personal Media>\nFake players (for example, Deployer from Create) won't regenerate their media pool";
        String desc_EnablesAmethystCircle = "<Amethyst Circle>\nmedia waves from circles go through amethyst budding blocks, and accelerates their crystal growth";
        String desc_AmethystCircleSingleChargeCost = "<Amethyst Circle>\neach time media reduces for crystal growth passing a budding block";
        String desc_AmethystCircleFullPowerLevel = "<Amethyst Circle>\nmax possible charging level of the wave: growth strength = min(this, wave total media / single cost)";

        boolean EnablesMoteChestGUI();
        boolean EnablesMishapNoYeet();
        boolean EnablesTeleportVehicles();
        boolean EnablesPersonalMediaPool();
        int PersonalMediaMax();
        int PersonalMediaRegenStep();
        int PersonalMediaRegenInterval();
        boolean FakePlayerDontRegenMedia();
        boolean EnablesAmethystCircle();
        int AmethystCircleSingleChargeCost();
        int AmethystCircleFullPowerLevel();
    }
}
