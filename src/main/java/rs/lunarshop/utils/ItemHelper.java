package rs.lunarshop.utils;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.random.Random;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.rewards.LunarMiscReward;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.abstracts.lunarprops.LunarItemProp;
import rs.lunarshop.ui.ItemTargetInvoker;

import java.util.HashMap;
import java.util.Map;

import static rs.lunarshop.patches.MiscRewardEnum.LUNAR_COIN;
import static rs.lunarshop.patches.MiscRewardEnum.OLD_CHEST;

public class ItemHelper {
    private static final Map<Integer, LunarItemProp> ItemPropMap = new HashMap<>();
    private static final Map<String, Integer> ItemLocalnameMap = new HashMap<>();
    public static final ItemTargetInvoker TARGET_INVOKER = new ItemTargetInvoker();
    
    public static void UseTarget(AbstractLunarRelic item, AbstractCreature s) {
        TARGET_INVOKER.active(item, t -> item.onTargetedUse(s, t), t -> item.canUseOn(s, t));
    }
    
    public static void AddItemProp(@NotNull LunarItemProp prop) {
        int lunarID = prop.lunarID;
        if (ItemPropMap.containsKey(lunarID))
            LunarMod.LogInfo("Replacing item [" + lunarID + "] properties");
        ItemPropMap.put(lunarID, prop);
        ItemLocalnameMap.put(prop.localname, lunarID);
    }
    
    public static LunarItemProp GetProp(int lunarID) {
        if (!ItemPropMap.containsKey(lunarID))
            return LunarItemProp.GetReplacer();
        return ItemPropMap.get(lunarID);
    }
    
    public static LunarItemProp GetProp(String localname) {
        if (ItemLocalnameMap.containsKey(localname)) {
            int lunarID = ItemLocalnameMap.get(localname);
            return GetProp(lunarID);
        }
        return LunarItemProp.GetReplacer();
    }
    
    @NotNull
    public static String GetRelicID(int lunarID) {
        LunarItemProp prop = GetProp(lunarID);
        return LunarMod.Prefix(prop.localID);
    }
    
    @NotNull
    public static String GetRelicID(String localname) {
        LunarItemProp prop = GetProp(localname);
        return LunarMod.Prefix(prop.localID);
    }
    
    public static boolean RollCloverLuck(LunarItemProp prop, float chance) {
        return LunarMaster.RollLuck(prop.localname, chance, true);
    }
    
    public static boolean RollCloverLuck(String from, float chance) {
        return LunarMaster.RollLuck(from, chance, true);
    }
    
    public static boolean RollCloverBadLuck(LunarItemProp prop, float chance) {
        return !RollCloverLuck(prop, 1F - chance);
    }
    
    public static boolean RollLuck(LunarItemProp prop, float chance) {
        return LunarMaster.RollLuck(prop.localname, chance, false);
    }
    
    public static boolean RollLuck(String from, float chance) {
        return LunarMaster.RollLuck(from, chance, false);
    }
    
    public static boolean RollBadLuck(LunarItemProp prop, float chance) {
        return !RollLuck(prop, 1F - chance);
    }
    
    public static Random GetItemRng() {
        return LunarMaster.GetItemRng();
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