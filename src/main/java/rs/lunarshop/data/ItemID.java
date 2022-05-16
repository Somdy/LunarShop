package rs.lunarshop.data;

import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.lunarprops.LunarItemID;

public final class ItemID {
    // Relics
    public static final LunarItemID Unknown = create(-1, "Unknown");
    public static final LunarItemID Fealty = create(0, "Fealty");
    public static final LunarItemID Crown = create(1, "Crown");
    public static final LunarItemID Corpsebloom = create(2, "Corpsebloom");
    public static final LunarItemID LessBossHp = create(3, "LessBossHp");
    public static final LunarItemID DrownedGesture = create(4, "DrownedGesture");
    public static final LunarItemID PowerRachis = create(5, "PowerRachis");
    public static final LunarItemID Purity = create(6, "Purity");
    public static final LunarItemID SharpedGlass = create(7, "SharpedGlass");
    public static final LunarItemID Beetle = create(8, "Beetle");
    public static final LunarItemID Clover = create(9, "Clover");
    public static final LunarItemID Gouge = create(10, "Gouge");
    public static final LunarItemID JumpHigher = create(11, "JumpHigher");
    public static final LunarItemID Horn = create(12, "Horn");
    public static final LunarItemID InstantKiller = create(17, "InstantKiller");
    public static final LunarItemID DeathMark = create(18, "DeathMark");
    public static final LunarItemID LeechingSeed = create(19, "LeechingSeed");
    public static final LunarItemID Planula = create(20, "Planula");
    public static final LunarItemID Vase = create(21, "Vase");
    public static final LunarItemID RoseBuckler = create(22, "RoseBuckler");
    public static final LunarItemID Shattering = create(23, "Shattering");
    public static final LunarItemID ChargedDrill = create(24, "ChargedDrill");
    public static final LunarItemID BossBullet = create(25, "BossBullet");
    public static final LunarItemID Microbots = create(26, "Microbots");
    public static final LunarItemID Daisy = create(27, "Daisy");
    public static final LunarItemID Scythe = create(28, "Scythe");
    public static final LunarItemID GhorTome = create(29, "GhorTome");
    public static final LunarItemID Infusion = create(31, "Infusion");
    public static final LunarItemID BleederDagger = create(32, "BleederDagger");
    public static final LunarItemID Chronobauble = create(33, "Chronobauble");
    public static final LunarItemID TougherTimes = create(34, "TougherTimes");
    public static final LunarItemID Crowbar = create(35, "Crowbar");
    public static final LunarItemID RitualDagger = create(36, "RitualDagger");
    public static final LunarItemID ArmorPlate = create(37, "ArmorPlate");
    public static final LunarItemID TheGlasses = create(38, "TheGlasses");
    public static final LunarItemID Medkit = create(39, "Medkit");
    public static final LunarItemID AtGMk = create(40, "MissileMk1");
    public static final LunarItemID StealthKit = create(41, "Stealthkit");
    public static final LunarItemID Aegis = create(42, "Aegis");
    public static final LunarItemID Rejuvenation = create(43, "HealingRack");
    public static final LunarItemID Spleen = create(44, "Spleen");
    public static final LunarItemID Knurl = create(45, "Knurl");
    public static final LunarItemID AlienHead = create(50, "AlienHead");
    public static final LunarItemID Crystal = create(51, "Crystal");
    public static final LunarItemID Syringe = create(52, "Syringe");
    public static final LunarItemID HopooFeather = create(53, "HopooFeather");
    public static final LunarItemID Pearl = create(54, "Pearl");
    public static final LunarItemID PerfectPearl = create(55, "PerfectPearl");
    public static final LunarItemID DioFriend = create(56, "DioFriend");
    public static final LunarItemID Slug = create(59, "Slug");
    public static final LunarItemID RustyKey = create(60, "RustyKey");
    public static final LunarItemID Predatory = create(61, "Predatory");
    public static final LunarItemID Ukulele = create(62, "Ukulele");
    public static final LunarItemID Behemoth = create(64, "Behemoth");
    
    // Equipments
    public static final LunarItemID ReduceStrength = create(13, "ReduceStrength");
    public static final LunarItemID Helfire = create(14, "Helfire");
    public static final LunarItemID FakeWine = create(15, "FakeWine");
    public static final LunarItemID Meteorite = create(16, "Meteorite");
    public static final LunarItemID CrowdFunder = create(30, "CrowdFunder");
    public static final LunarItemID JadeElephant = create(46, "JadeElephant");
    public static final LunarItemID Opus = create(47, "Opus");
    public static final LunarItemID Capacitor = create(48, "Capacitor");
    public static final LunarItemID MissileLauncher = create(49, "MissileLauncher");
    public static final LunarItemID Cube = create(63, "BlackholeCube");
    
    // Specials
    public static final LunarItemID DioConsumed = create(57, "DioConsumed");
    public static final LunarItemID WineAffliction = create(58, "WineAffliction");
    
    private static LunarItemID create(int lunarID, String internalID) {
        return LunarItemID.Create(lunarID, LunarMod.Prefix(internalID));
    }
}