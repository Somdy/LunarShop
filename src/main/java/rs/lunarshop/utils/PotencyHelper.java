package rs.lunarshop.utils;

import rs.lunarshop.core.LunarMod;

public class PotencyHelper {
    // Lunar Potency
    public static final int LUNAR_LV1 = 0;
    public static final int LUNAR_LV2 = 1;
    public static final int LUNAR_LV3 = 2;
    public static final int LUNAR_LV4 = 3;
    public static final int LUNAR_LV5 = 4;
    public static final int LUNAR_LV6 = 5;
    
    public static boolean LunarReached(int level) {
        return LunarMod.LunarPotencyActive(level);
    }
}