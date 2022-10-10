package rs.lunarshop.core;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.interfaces.powers.ArmorModifierPower;
import rs.lunarshop.interfaces.powers.AttackModifierPower;
import rs.lunarshop.interfaces.relics.ArmorModifierRelic;
import rs.lunarshop.interfaces.relics.AttackModifierRelic;
import rs.lunarshop.interfaces.relics.LuckModifierRelic;
import rs.lunarshop.interfaces.relics.RegenModifierRelic;
import rs.lunarshop.items.relics.LunarPass;
import rs.lunarshop.items.relics.lunar.Clover;
import rs.lunarshop.patches.mechanics.ArmorField;
import rs.lunarshop.patches.mechanics.RegenField;
import rs.lunarshop.shops.ShopEventManager;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.ui.loadout.LoadoutTab;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.LunarUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SpireInitializer
public class LunarMaster implements LunarUtils, CustomSavable<Map<String, Integer>>, PostDungeonUpdateSubscriber, 
        OnStartBattleSubscriber, StartGameSubscriber {
    private static Random itemRng;
    private static Random shopRng;
    private static int attack;
    private static int regen;
    private static int armor;
    protected static int lunarCoins;
    private static float luck;
    protected static int eclipse;
    
    private static int justLuckyCounter;
    
    public static ShopEventManager ShopManager;
    
    public static void initialize() {
        LunarMaster master = new LunarMaster();
        attack = 0;
        regen = 0;
        armor = 0;
        lunarCoins = 0;
        luck = 0F;
        eclipse = 0;
        BaseMod.subscribe(master);
        BaseMod.addSaveField("LunarMaster", master);
    }
    
    public static void PickUpLunarCoin(int gain) {
        if (gain <= 0) return;
        lunarCoins += gain;
    }
    
    public static boolean SpendLunarCoin(int cost) {
        if (cost > lunarCoins) return false;
        lunarCoins -= cost;
        if (lunarCoins == 0)
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
        return lunarCoins;
    }
    
    public static int Attack(boolean refresh) {
        if (refresh) refreshAttackStats();
        return attack;
    }
    
    public static int Attack() {
        return Attack(false);
    }
    
    private static void refreshAttackStats() {
        int origin = 0;
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof AttackModifierRelic)
                origin = ((AttackModifierRelic) r).modifyAttack(origin);
        }
        for (AbstractPower p : LMSK.Player().powers) {
            if (p instanceof AttackModifierPower)
                origin = ((AttackModifierPower) p).modifyAttack(origin);
        }
        attack = origin;
    }
    
    public static int Regen(boolean refresh) {
        if (refresh) refreshRegenStats();
        return regen;
    }
    
    public static int Regen() {
        return Regen(false);
    }
    
    private static void refreshRegenStats() {
        int origin = RegenField.regen.get(LMSK.Player());
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof RegenModifierRelic)
                origin = ((RegenModifierRelic) r).modifyRegen(origin);
        }
        regen = origin;
    }
    
    public static int Armor(boolean refresh) {
        if (refresh) refreshArmorStats();
        return armor;
    }
    
    public static int Armor() {
        return Armor(false);
    }
    
    private static void refreshArmorStats() {
        int origin = ArmorField.armor.get(LMSK.Player());
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof ArmorModifierRelic)
                origin = ((ArmorModifierRelic) r).modifyArmor(origin);
        }
        for (AbstractPower p : LMSK.Player().powers) {
            if (p instanceof ArmorModifierPower)
                origin = ((ArmorModifierPower) p).modifyArmor(origin);
        }
        armor = origin;
    }
    
    public static void SetEclipse(int level) {
        eclipse = level;
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
            shopRng = new Random(Settings.seed);
            LunarPass.ResetPass();
            LunarMod.ReloadPanel();
        }
    }
    
    @Override
    public void receivePostDungeonUpdate() {
        checkShopManager();
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
        Map<String, Integer> map = new HashMap<>();
        checkRngs();
        checkShopManager();
        map.put("itemRng", itemRng.counter);
        map.put("shopRng", shopRng.counter);
        map.put("shopFloorLast", ShopManager.getFloorLastShop());
        map.put("justLucky", justLuckyCounter);
        map.put("eclipse", eclipse);
        return map;
    }
    
    @Override
    public void onLoad(Map<String, Integer> saveFields) {
        if (saveFields != null && CardCrawlGame.loadingSave) {
            checkRngs();
            checkShopManager();
            lunarCoins = LunarMod.GetInt("LunarCoins");
            log("Loading lunar masters");
            checkRngs();
            saveFields.forEach((k, v) -> {
                if (k.equals("itemRng")) {
                    itemRng.counter = v;
                    log("Loading item rng counter [" + v + "]");
                } else if (k.equals("shopRng")) {
                    shopRng.counter = v;
                    log("Loading shop rng counter [" + v + "]");
                }
                else if (k.equals("shopFloorLast")) {
                    ShopManager.setFloorLastShop(v);
                }
                else if (k.equals("justLucky")) {
                    justLuckyCounter = v;
                }
                else if (k.equals("eclipse")) {
                    eclipse = v;
                }
            });
        }
    }
    
    @Override
    public Type savedType() {
        return new TypeToken<Map<String, Integer>>(){}.getType();
    }
}