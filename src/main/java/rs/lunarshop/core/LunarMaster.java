package rs.lunarshop.core;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.StartActSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.Nullable;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.data.DifficultyMod;
import rs.lunarshop.interfaces.relics.LuckModifierRelic;
import rs.lunarshop.items.relics.LunarPass;
import rs.lunarshop.items.relics.lunar.Clover;
import rs.lunarshop.shops.ShopEventManager;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.ItemStatHelper;
import rs.lunarshop.utils.LunarUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SpireInitializer
public class LunarMaster implements LunarUtils, CustomSavable<Map<String, Integer>>, PostDungeonUpdateSubscriber, 
        OnStartBattleSubscriber, StartGameSubscriber, StartActSubscriber {
    private static final Map<Integer, DifficultyMod> DIFFICULTY_MOD_MAP = new HashMap<>();
    
    private static Random itemRng;
    private static Random shopRng;
    protected static int LunarCoins;
    private static float luck;
    private static int justLuckyCounter;
    public static ShopEventManager ShopManager;
    public static int ProgModCount;
    
    public static void initialize() {
        LunarMaster master = new LunarMaster();
        LunarCoins = 0;
        luck = 0F;
        BaseMod.subscribe(master);
        BaseMod.addSaveField("LunarMaster", master);
    }
    
    public static void AddDifficultyMod(DifficultyMod mod) {
        DIFFICULTY_MOD_MAP.put(mod.level, mod);
    }
    
    @Nullable
    public static DifficultyMod GetDifficultyMod(int level) {
        if (DIFFICULTY_MOD_MAP.containsKey(level))
            return DIFFICULTY_MOD_MAP.get(level);
        LunarMod.LogInfo("UNDEFINED DIFFICULTY LEVEL [" + level + "]");
        return null;
    }
    
    public static void PickUpLunarCoin(int gain) {
        if (gain <= 0) return;
        LunarCoins += gain;
    }
    
    public static boolean SpendLunarCoin(int cost) {
        if (cost > LunarCoins) return false;
        LunarCoins -= cost;
        if (LunarCoins == 0)
            LunarMod.LogInfo("All lunar coins were run out");
        return true;
    }
    
    public static boolean RollLuck(String from, float chance, boolean clover) {
        refreshLuckStats();
        if (chance <= 0F) return false;
        if (chance >= 1F) {
            LunarMod.WarnInfo("Chance meets 100 percent, returning true");
            return true;
        }
        Random rng = GetItemRng();
        int extraRoll = clover ? MathUtils.ceil(Math.abs(luck)) : 0;
        float luckyTime = rng.random(0F, 1F);
        float initLucky = luckyTime;
        for (int i = 0; i < extraRoll; i++) {
            float boundary = rng.random(0F, 1F);
            luckyTime = luck > 0F ? Math.min(luckyTime, boundary) : Math.max(luckyTime, boundary);
        }
        if (clover) {
            checkJustLucky(chance, initLucky, luckyTime);
            checkTooLucky(chance, initLucky, luckyTime);
        }
        if (initLucky > chance && luckyTime <= chance) {
            if (ShopManager.cprHasLunarRelic(ItemHelper.GetProp(9))) {
                Clover r = (Clover) LMSK.Player().getRelic(ItemHelper.GetRelicID(9));
                if (r != null) r.gotLucky();
            }
        }
        boolean lucky = luckyTime <= chance;
        LunarMod.LogInfo("Rolled luck for [" + from + "] at [" + chance + "] " + lucky);
        return lucky;
    }
    
    static void checkJustLucky(float chance, float initLucky, float luckyTime) {
        if (chance >= 0.25F || Clover.CloverCounts() <= 3 || AchvHelper.IsAchvUnlocked(AchvID.JustLucky)) return;
        if (initLucky <= chance) {
            justLuckyCounter++;
        }
        if (initLucky > chance && luckyTime <= chance) {
            justLuckyCounter = 0;
        }
        if (justLuckyCounter >= 3)
            AchvHelper.UnlockAchv(AchvID.JustLucky);
    }
    
    static void checkTooLucky(float chance, float initLucky, float luckyTIme) {
        if (chance <= 0.85F || luck < 5 || AchvHelper.IsAchvUnlocked(AchvID.TooLucky)) return;
        if (initLucky > chance && luckyTIme > chance)
            AchvHelper.UnlockAchv(AchvID.TooLucky);
    }
     
    public static Random GetItemRng() {
        return itemRng;
    }
    
    public static Random GetShopRng() {
        return shopRng;
    }
    
    public static float Luck(boolean refresh) {
        if (refresh) refreshLuckStats();
        return luck;
    }
    
    public static float Luck() {
        return Luck(false);
    }
    
    private static void refreshLuckStats() {
        float origin = 0;
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof LuckModifierRelic)
                origin = ((LuckModifierRelic) r).modifyLuck(origin);
        }
        luck = origin;
    }
    
    public static int LunarCoin() {
        return LunarCoins;
    }
    
    private void checkRngs() {
        if (itemRng == null)
            itemRng = new Random(Settings.seed);
        if (shopRng == null)
            shopRng = new Random(Settings.seed);
    }
    
    private void checkShopManager() {
        if (ShopManager == null) {
            ShopManager = new ShopEventManager();
        }
    }
    
    @Override
    public Map<String, Integer> onSave() {
        ItemStatHelper.FlushCachedStats();
        Map<String, Integer> map = new HashMap<>();
        checkRngs();
        checkShopManager();
        map.put("itemRng", itemRng.counter);
        map.put("shopRng", shopRng.counter);
        map.put("shopFloorLast", ShopManager.getFloorLastShop());
        map.put("justLucky", justLuckyCounter);
        map.put("progModCount", ProgModCount);
        return map;
    }
    
    @Override
    public void onLoad(Map<String, Integer> saveFields) {
        ItemStatHelper.RefreshAllRelicStats();
        if (saveFields != null && CardCrawlGame.loadingSave) {
            checkRngs();
            checkShopManager();
            LunarCoins = LunarMod.GetInt("LunarCoins");
            log("Loading lunar masters");
            checkRngs();
            itemRng.counter = saveFields.get("itemRng");
            log("Loading item rng counter [" + itemRng.counter + "]");
            shopRng.counter = saveFields.get("shopRng");
            log("Loading shop rng counter [" + shopRng.counter + "]");
            ShopManager.setFloorLastShop(saveFields.get("shopFloorLast"));
            justLuckyCounter = saveFields.get("justLucky");
            ProgModCount = saveFields.get("progModCount");
        }
    }
    
    @Override
    public Type savedType() {
        return new TypeToken<Map<String, Integer>>(){}.getType();
    }
    
    @Override
    public void receiveOnBattleStart(AbstractRoom r) {
        checkRngs();
    }
    
    @Override
    public void receiveStartGame() {
        if (!CardCrawlGame.loadingSave) {
            log("Initializing new lunar things");
            ShopManager = new ShopEventManager();
            itemRng = new Random(Settings.seed);
            shopRng = new Random(Settings.seed);
            ProgModCount = 0;
            LunarPass.ResetPass();
            LunarMod.ReloadPanel();
        }
    }
    
    @Override
    public void receivePostDungeonUpdate() {
        checkShopManager();
    }
    
    @Override
    public void receiveStartAct() {
        if (AbstractDungeon.floorNum > 1) {
            ProgModCount++;
            log("Transiting dungeon to next one, current progcount [" + ProgModCount + "]");
        }
        if (AbstractDungeon.floorNum <= 1) {
            if (CardCrawlGame.dungeon instanceof Exordium) {
                if (LunarUtils.EclipseLevel() >= LoadoutManager.ECLIPSES.LV1)
                    LMSK.Player().currentHealth -= 10;
            }
        }
    }
}