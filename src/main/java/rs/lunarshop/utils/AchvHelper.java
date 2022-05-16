package rs.lunarshop.utils;

import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.relics.lunar.Purity;
import rs.lunarshop.subjects.lunarprops.LunarAchvData;

public class AchvHelper {
    
    public static boolean IsAchvUnlocked(int key) {
        return LunarMod.achvTracker.isAchvUnlocked(key);
    }
    
    public static boolean IsAchvUnlocked(LunarAchvData data) {
        return IsAchvUnlocked(data.key);
    }
    
    public static void UnlockAchv(int key) {
        LunarMod.achvTracker.unlockAchv(key);
    }
    
    public static void UnlockAchv(@NotNull LunarAchvData data) {
        UnlockAchv(data.key);
    }
    
    public static void CheckAchvOnGameVictory() {
        checkPurityVictoryAchv();
    }
    
    static void checkPurityVictoryAchv() {
        if (LMSK.Player().hasRelic(ItemID.Purity.internalID)) {
            Purity p = (Purity) LMSK.Player().getRelic(ItemID.Purity.internalID);
            p.checkAchv();
        }
    }
}