package rs.lunarshop.data;

import rs.lunarshop.enums.AchvTier;
import rs.lunarshop.subjects.lunarprops.LunarAchvData;

public final class AchvID {
    public static LunarAchvData NewtFirstTime = create(0, AchvTier.COMMON);
    public static LunarAchvData LunarBusiness = create(1, AchvTier.COMMON);
    public static LunarAchvData OutsideLand = create(2, AchvTier.COMMON);
    public static LunarAchvData RichInCoins = create(3, AchvTier.RARE);
    public static LunarAchvData LunarBandit = create(4, AchvTier.LEGEND);
    public static LunarAchvData LunarCmdEss = create(5, AchvTier.COMMON);
    public static LunarAchvData ManyBeetles = create(6, AchvTier.COMMON);
    public static LunarAchvData StackingVase = create(7, AchvTier.COMMON);
    public static LunarAchvData InstantKillHeart = create(8, AchvTier.RARE);
    public static LunarAchvData FakeWineHarms = create(9, AchvTier.RARE);
    public static LunarAchvData PureCalm = create(10, AchvTier.LEGEND);
    public static LunarAchvData JustLucky = create(11, AchvTier.LEGEND);
    public static LunarAchvData TooLucky = create(12, AchvTier.LEGEND);
    public static LunarAchvData ClearlyClear = create(13, AchvTier.LEGEND);
    public static LunarAchvData HighOne = create(14, AchvTier.LEGEND);
    
    private static LunarAchvData create(int key, AchvTier tier) {
        return new LunarAchvData(key, tier);
    }
}