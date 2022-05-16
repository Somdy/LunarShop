package rs.lunarshop.utils;

import com.megacrit.cardcrawl.random.Random;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.rewards.LunarMiscReward;
import rs.lunarshop.subjects.lunarprops.LunarItemID;

import static rs.lunarshop.patches.MiscRewardEnum.LUNAR_COIN;
import static rs.lunarshop.patches.MiscRewardEnum.OLD_CHEST;

public class ItemHelper {
    
    public static boolean RollCloverLuck(int lunarID, float chance) {
        return LunarMaster.RollLuck(lunarID, chance, true);
    }
    
    public static boolean RollCloverLuck(LunarItemID lunarID, float chance) {
        return LunarMaster.RollLuck(lunarID.lunarID, chance, true);
    }
    
    public static boolean RollCloverBadLuck(int lunarID, float chance) {
        return !RollCloverLuck(lunarID, 1F - chance);
    }
    
    public static boolean RollLuck(int lunarID, float chance) {
        return LunarMaster.RollLuck(lunarID, chance, false);
    }
    
    public static boolean RollLuck(Random rng, float chance) {
        return LunarMaster.RollLuck(rng.random(0, 2), chance, false);
    }
    
    public static boolean RollBadLuck(int lunarID, float chance) {
        return !RollLuck(lunarID, 1F - chance);
    }
    
    public static Random GetItemRng(int lunarID) {
        return LunarMaster.GetRng(lunarID);
    }
    
    public static Random GetItemRng(LunarItemID lunarID) {
        return LunarMaster.GetRng(lunarID.lunarID);
    }
    
    @NotNull
    public static LunarMiscReward GetLunarCoinReward(int coins) {
        LunarMiscReward reward = new LunarMiscReward(LunarImageMst.LunarCoin, 
                coins + " " + LunarMiscReward.TEXT[coins > 10 ? 1 : 0], LUNAR_COIN);
        reward.coins = coins;
        return reward;
    }
    
    public static LunarMiscReward GetOldChestReward() {
        LunarMiscReward reward = new LunarMiscReward(LunarImageMst.OldChest, LunarMiscReward.TEXT[2], OLD_CHEST);
        return reward;
    }
}