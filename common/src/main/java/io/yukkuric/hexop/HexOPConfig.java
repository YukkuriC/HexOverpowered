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
    public static int TrulyHurtLevel() {
        return imp.TrulyHurtLevel();
    }
    public static boolean EnablesFactorCutSpell() {
        return imp.EnablesFactorCutSpell();
    }
    public static int FactorCutPrimeCost() {
        return imp.FactorCutPrimeCost();
    }
    public static int FactorCutNonPrimeCostScale() {
        return imp.FactorCutNonPrimeCostScale();
    }
    public static int FactorCutFallbackCost() {
        return imp.FactorCutFallbackCost();
    }
    public static boolean FactorCutRandomMode() {
        return imp.FactorCutRandomMode();
    }
    public static int FactorCutMinimumFactor() {
        return imp.FactorCutMinimumFactor();
    }
    public static int FactorCutKillingBlowLine() {
        return imp.FactorCutKillingBlowLine();
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
    public static boolean ExecutablePropertyIota() {
        return imp.ExecutablePropertyIota();
    }
    public static boolean EnablesArtifactIO() {
        return imp.EnablesArtifactIO();
    }
    public interface API {
        String desc_RevealsHexInsideCastingItems = "Displays the hex inside readonly casting items; working client-side";
        String desc_EnablesMoteItemHandler = "Enables item IO without casting patterns";
        String desc_EnablesMoteChestGUI = "Enables 'A Glance of Mote Chest' (a simple chest GUI) on right click\ndisables if `EnablesMoteItemHandler` is off";
        String desc_TrueNameCrossDimension = "Player references are always valid even in another dimension";
        String desc_EnablesMishapNoYeet = "Mishaps won't drop items anymore";
        String desc_EnablesTeleportVehicles = "Teleporting riding entities no longer do mishaps";
        String desc_EnablesChargeMediaAction = "if not, the `YJSP's Charge Media` pattern will be disabled";
        String desc_EnablesMindEnvActions = "if not, mind environment series patterns will be disabled";
        String desc_TrulyHurtLevel = "level 1 enables extra `setHealth` method call;\nlevel 2 adds saved data searching & replacing if lv.1 failed";
        String desc_EnablesFactorCutSpell = "if not, Factor Cut pattern will be disabled";
        String desc_FactorCutPrimeCost = "const cost for prime factors (in 1e-4 dust)";
        String desc_FactorCutNonPrimeCostScale = "const cost for non-prime factors (multiplied by the factor itself, in 1e-4 dust)";
        String desc_FactorCutFallbackCost = "cost for the fallback hurt-1 action (in 1e-4 dust)";
        String desc_FactorCutRandomMode = "target's health gets extra random reduction after each successful division";
        String desc_FactorCutMinimumFactor = "minimum factor usable for division; factors less than this (if not equal to target health) will be denied";
        String desc_FactorCutKillingBlowLine = "entities with health under hou many points can be wiped for free";
        String desc_EnablesPersonalMediaPool = "Enables player's personal media pool";
        String desc_PersonalMediaMax = "Max media (in 0.0001 dust) for personal pool";
        String desc_PersonalMediaRegenStep = "How many media points (in 0.0001 dust) personal pool regenerates each time";
        String desc_PersonalMediaRegenInterval = "For every X ticks personal pool regenerate once";
        String desc_FakePlayerDontRegenMedia = "Fake players (for example, Deployer from Create) won't regenerate their media pool";
        String desc_PersonalMediaAfterEnlightened = "Only activates personal media bar after enlightened";
        String desc_FiresPersonalMediaEvents = "Triggers events when personal media pool changed";
        String desc_ExecutablePropertyIota = "making property iotas directly execute what's inside when themselves executed";
        String desc_EnablesArtifactIO = "Makes artifacts directly read/writeable via HexParse methods; requires restart for changes to apply.";

        boolean RevealsHexInsideCastingItems();
        boolean EnablesMoteItemHandler();
        boolean EnablesMoteChestGUI();
        boolean TrueNameCrossDimension();
        boolean EnablesMishapNoYeet();
        boolean EnablesTeleportVehicles();
        boolean EnablesChargeMediaAction();
        boolean EnablesMindEnvActions();
        int TrulyHurtLevel();
        boolean EnablesFactorCutSpell();
        int FactorCutPrimeCost();
        int FactorCutNonPrimeCostScale();
        int FactorCutFallbackCost();
        boolean FactorCutRandomMode();
        int FactorCutMinimumFactor();
        int FactorCutKillingBlowLine();
        boolean EnablesPersonalMediaPool();
        int PersonalMediaMax();
        int PersonalMediaRegenStep();
        int PersonalMediaRegenInterval();
        boolean FakePlayerDontRegenMedia();
        boolean PersonalMediaAfterEnlightened();
        boolean FiresPersonalMediaEvents();
        boolean ExecutablePropertyIota();
        boolean EnablesArtifactIO();
    }
}
