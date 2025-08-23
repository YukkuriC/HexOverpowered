package io.yukkuric.hexop;

public class HexOPConfig {
    static API imp;

    public static boolean loaded() {
        return imp != null;
    }
    public static void bindConfigImp(API api) {
        imp = api;
    }

    public static boolean RevealsHexInsideCastingItems() {
        return imp.RevealsHexInsideCastingItems();
    }
    public static boolean EnablesMoteItemHandler() {
        return imp.EnablesMoteItemHandler();
    }
    public static boolean EnablesMoteChestGUI() {
        return imp.EnablesMoteChestGUI();
    }
    public static boolean TrueNameCrossDimension() {
        return imp.TrueNameCrossDimension();
    }
    public static boolean EnablesMishapNoYeet() {
        return imp.EnablesMishapNoYeet();
    }
    public static boolean EnablesTeleportVehicles() {
        return imp.EnablesTeleportVehicles();
    }
    public static boolean EnablesChargeMediaAction() {
        return imp.EnablesChargeMediaAction();
    }
    public static boolean EnablesMindEnvActions() {
        return imp.EnablesMindEnvActions();
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
    public static boolean PersonalMediaAfterEnlightened() {
        return imp.PersonalMediaAfterEnlightened();
    }
    public static boolean FiresPersonalMediaEvents() {
        return imp.FiresPersonalMediaEvents();
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
        String desc_RevealsHexInsideCastingItems = "<Display>\nDisplays the hex inside readonly casting items; working client-side";
        String desc_EnablesMoteItemHandler = "<Mote>\nEnables item IO without casting patterns";
        String desc_EnablesMoteChestGUI = "<Mote>\nEnables 'A Glance of Mote Chest' (a simple chest GUI) on right click\ndisables if `EnablesMoteItemHandler` is off";
        String desc_TrueNameCrossDimension = "<Misc>\nPlayer references are always valid even in another dimension";
        String desc_EnablesMishapNoYeet = "<Mishap>\nMishaps won't drop items anymore";
        String desc_EnablesTeleportVehicles = "<Mishap>\nTeleporting riding entities no longer do mishaps";
        String desc_EnablesChargeMediaAction = "<Pattern>\nif not, the `YJSP's Charge Media` pattern will be disabled";
        String desc_EnablesMindEnvActions = "<Pattern>\nif not, mind environment series patterns will be disabled";
        String desc_EnablesPersonalMediaPool = "<Personal Media>\nEnables player's personal media pool";
        String desc_PersonalMediaMax = "<Personal Media>\nMax media (in 0.0001 dust) for personal pool";
        String desc_PersonalMediaRegenStep = "<Personal Media>\nHow many media points (in 0.0001 dust) personal pool regenerates each time";
        String desc_PersonalMediaRegenInterval = "<Personal Media>\nFor every X ticks personal pool regenerate once";
        String desc_FakePlayerDontRegenMedia = "<Personal Media>\nFake players (for example, Deployer from Create) won't regenerate their media pool";
        String desc_PersonalMediaAfterEnlightened = "<Personal Media>\nOnly activates personal media bar after enlightened";
        String desc_FiresPersonalMediaEvents = "<Personal Media>\nTriggers events when personal media pool changed";
        String desc_EnablesAmethystCircle = "<Amethyst Circle>\nmedia waves from circles go through amethyst budding blocks, and accelerates their crystal growth";
        String desc_AmethystCircleSingleChargeCost = "<Amethyst Circle>\neach time media reduces for crystal growth passing a budding block";
        String desc_AmethystCircleFullPowerLevel = "<Amethyst Circle>\nmax possible charging level of the wave: growth strength = min(this, wave total media / single cost)";

        boolean RevealsHexInsideCastingItems();
        boolean EnablesMoteItemHandler();
        boolean EnablesMoteChestGUI();
        boolean TrueNameCrossDimension();
        boolean EnablesMishapNoYeet();
        boolean EnablesTeleportVehicles();
        boolean EnablesChargeMediaAction();
        boolean EnablesMindEnvActions();
        boolean EnablesPersonalMediaPool();
        int PersonalMediaMax();
        int PersonalMediaRegenStep();
        int PersonalMediaRegenInterval();
        boolean FakePlayerDontRegenMedia();
        boolean PersonalMediaAfterEnlightened();
        boolean FiresPersonalMediaEvents();
        boolean EnablesAmethystCircle();
        int AmethystCircleSingleChargeCost();
        int AmethystCircleFullPowerLevel();
    }
}
