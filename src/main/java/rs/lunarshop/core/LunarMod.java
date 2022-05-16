package rs.lunarshop.core;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.eventUtil.util.Condition;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rewards.RewardSave;
import rs.lazymankits.LManager;
import rs.lazymankits.interfaces.OnGainBlockSubscriber;
import rs.lazymankits.interfaces.OnInitializeSubscriber;
import rs.lazymankits.utils.LMSK;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.lunarshop.achievements.AchvGrid;
import rs.lunarshop.achievements.AchvManager;
import rs.lunarshop.achievements.AchvTracker;
import rs.lunarshop.config.MiscConfig;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.events.CleansingPoolEvent;
import rs.lunarshop.events.EventManager;
import rs.lunarshop.events.LunarMerchantEvent;
import rs.lunarshop.interfaces.relics.BlockModifierRelic;
import rs.lunarshop.items.equipments.EquipmentManager;
import rs.lunarshop.items.relics.LunarPass;
import rs.lunarshop.items.relics.RelicAttrs;
import rs.lunarshop.items.relics.RelicManager;
import org.jetbrains.annotations.Nullable;
import rs.lazymankits.LMDebug;
import rs.lunarshop.localizations.LocaleManager;
import rs.lunarshop.patches.MiscRewardEnum;
import rs.lunarshop.rewards.LunarMiscReward;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.ui.EquipmentProxy;
import rs.lunarshop.ui.OmniPanel;
import rs.lunarshop.ui.cmdpicker.PickerCaller;
import rs.lunarshop.ui.cmdpicker.PickerScreen;
import rs.lunarshop.utils.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class LunarMod implements LunarUtils, EditCardsSubscriber, EditRelicsSubscriber, EditKeywordsSubscriber, EditStringsSubscriber, 
        PostDungeonUpdateSubscriber, PostRenderSubscriber, PostInitializeSubscriber, OnGainBlockSubscriber, OnInitializeSubscriber,
        PostCreateStartingRelicsSubscriber, PostUpdateSubscriber, AddAudioSubscriber, MaxHPChangeSubscriber {
    public static final String MOD_NAME = "Bazaars Between Time";
    public static final String[] AUTHORS = {"Somdy"};
    public static final String DESCRIPTION = "Mainly based on risk of rain 2";
    private static String PREFIX = "lunar";
    private static LunarMod lunar;
    
    private static List<AbstractGameAction> actionList = new ArrayList<>();
    
    public static OmniPanel OmniPanel;
    public static EquipmentProxy EqmtProxy;
    public static PickerScreen ItemPicker;
    
    public static LocaleManager LocalePack;
    
    public static AchvTracker achvTracker;
    public static AchvGrid achvGrid;
    
    public static boolean TimeScalingDifficultyMode = false;
    public static boolean MakePassAStarterRelic = false;
    public static boolean DefeatedLunarSnecko = false;
    
    public static boolean[] LunarPotencies = new boolean[]{false, false, false, false, false, false}; // size = 6

    public static void initialize() {
        lunar = new LunarMod();
        achvTracker = new AchvTracker();
        SpireConfig config = makeConfig();
        loadProperties(config);
    }
    
    public LunarMod() {
        BaseMod.subscribe(this);
        LManager.Sub(this);
    }
    
    @Nullable
    private static SpireConfig makeConfig() {
        Properties properties = new Properties();
        properties.setProperty("LunarCoins", Integer.toString(LunarMaster.LunarCoin()));
        properties.setProperty("timeScalesMode", Boolean.toString(TimeScalingDifficultyMode));
        properties.setProperty("makePassStarting", Boolean.toString(MakePassAStarterRelic));
        properties.setProperty("defeatedLunarSnecko", Boolean.toString(DefeatedLunarSnecko));
        
        achvTracker.initProperties(properties);
        
        for (int i = 0; i < LunarPotencies.length; i++) {
            properties.setProperty("lunarPotency#" + i, Boolean.toString(LunarPotencies[i]));
        }
    
        EventManager.InitProperties(properties);
        
        try {
            return new SpireConfig("LunarMod", "LunarModConfig", properties);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void loadProperties(SpireConfig config) {
        if (config == null) {
            WarnInfo("Missing rs.lunarshop.config file");
            return;
        }
        LunarMaster.lunarCoins = config.getInt("LunarCoins");
        TimeScalingDifficultyMode = config.getBool("timeScalesMode");
        MakePassAStarterRelic = config.getBool("makePassStarting");
        
        MsgHelper.PreLoad("Bosses Defeated: ");
        DefeatedLunarSnecko = config.getBool("defeatedLunarSnecko");
        if (DefeatedLunarSnecko)
            MsgHelper.Append("Lunar Snecko");
        MsgHelper.End();
        
        MsgHelper.PreLoad("Lunar Potency: ");
        for (int i = 0; i < LunarPotencies.length; i++) {
            LunarPotencies[i] = config.getBool("lunarPotency#" + i);
            if (LunarPotencies[i])
                MsgHelper.Append("Level_" + (i + 1));
        }
        MsgHelper.End();
        
        achvTracker.loadConfig(config);
        
        EventManager.LoadConfig(config);
    }
    
    private static void save(SpireConfig config) {
        if (config == null) return;
        try {
            config.save();
        } catch (Exception e) {
            WarnInfo("Failed to save current rs.lunarshop.config file");
        }
    }
    
    public static void SaveConfig() {
        SpireConfig config = makeConfig();
        config.setInt("LunarCoins", LunarMaster.LunarCoin());
        config.setBool("timeScalesMode", TimeScalingDifficultyMode);
        config.setBool("makePassStarting", MakePassAStarterRelic);
        config.setBool("defeatedLunarSnecko", DefeatedLunarSnecko);
    
        saveLunarPotencies(config);
        
        EventManager.SaveRecords(config);
        
        save(config);
    }
    
    private static void saveLunarPotencies(SpireConfig config) {
        for (int i = 0; i < LunarPotencies.length; i++) {
            config.setBool("lunarPotency#" + i, LunarPotencies[i]);
        }
    }
    
    public static int GetInt(String key) {
        SpireConfig config = makeConfig();
        return config.getInt(key);
    }
    
    public static boolean GetBool(String key) {
        SpireConfig config = makeConfig();
        return config.getBool(key);
    }
    
    public static void SaveInt(String key, int value) {
        SpireConfig config = makeConfig();
        config.setInt(key, value);
        save(config);
    }
    
    public static void SaveBool(String key, boolean value) {
        SpireConfig config = makeConfig();
        config.setBool(key, value);
        save(config);
    }
    
    public static void DefeatLunarSnecko() {
        if (!DefeatedLunarSnecko())
            setLunarSneckoDefeated();
        SpireConfig config = makeConfig();
        
        // Before unlocking new potency logic
        int coins = 10;
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1)) coins += 10;
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV6)) coins += 5;
        LunarMaster.PickUpLunarCoin(coins);
        config.setInt("LunarCoins", LunarMaster.LunarCoin());
        
        // Unlock new potency logic
        int unlockIndex = -1;
        for (int i = 0; i < LunarPotencies.length; i++) {
            if (!LunarPotencies[i]) {
                unlockIndex = i;
                break;
            }
        }
        if (unlockIndex > -1 && unlockIndex < LunarPotencies.length) {
            LunarPotencies[unlockIndex] = true;
            LogInfo("Unlocked " + unlockIndex + "th Lunar Potency, saving config");
            achvTracker.checkUnlocks();
            saveLunarPotencies(config);
        } else {
            LogInfo("All Lunar Potencies are unlocked");
        }
        
        save(config);
    }
    
    private static void setLunarSneckoDefeated() {
        DefeatedLunarSnecko = true;
        SpireConfig config = makeConfig();
        config.setBool("defeatedLunarSnecko", DefeatedLunarSnecko);
        save(config);
    }
    
    public static boolean DefeatedLunarSnecko() {
        SpireConfig config = makeConfig();
        return config.getBool("defeatedLunarSnecko");
    }
    
    public static boolean LunarPotencyActive(int slot) {
        if (slot >= LunarPotencies.length)
            slot = LunarPotencies.length - 1;
        return LunarPotencies[slot];
    }
    
    public static String Prefix(String origin) {
        return PREFIX + ":" + origin;
    }

    public static void LogInfo(Object what) {
        LMDebug.Log(lunar, "LUNAR [LOG] ==> " + what);
    }
    
    public static void WarnInfo(Object what) {
        if (!Loader.DEBUG) Loader.DEBUG = true;
        LMDebug.Log(lunar, "LUNAR [WARN] ==> " + what);
    }

    public static void PatchLog(Object what) {
        LMDebug.Log(lunar, "LUNAR [PATCH] ==> " + what);
    }
    
    public static UIStrings UIStrings(String ID) {
        if (!ID.startsWith(PREFIX))
            ID = Prefix(ID);
        return CardCrawlGame.languagePack.getUIString(ID);
    }
    
    public static EventStrings EventStrings(String ID) {
        if (!ID.startsWith(PREFIX))
            ID = Prefix(ID);
        return CardCrawlGame.languagePack.getEventString(ID);
    }
    
    public static boolean HasPass() {
        LunarPass pass = GetPass();
        return pass != null;
    }
    
    public static boolean HasPass(int p) {
        LunarPass pass = GetPass();
        if (pass != null)
            return pass.hasPass(p);
        return false;
    }
    
    public static boolean HasNoActivatedPass() {
        return !HasPass(0) && !HasPass(1) && !HasPass(2);
    }
    
    public static void ActivatePass(int p) {
        LunarPass pass = GetPass();
        if (pass != null) pass.activatePass(p);
    }
    
    @Nullable
    public static LunarPass GetPass() {
        if (AbstractDungeon.player != null && AbstractDungeon.getCurrMapNode() != null) {
            Optional<AbstractRelic> pass = AbstractDungeon.player.relics.stream().filter(r -> r instanceof LunarPass).findFirst();
            return (LunarPass) pass.orElse(null);
        }
        return null;
    }
    
    @Override
    public void receiveEditCards() {
    
    }
    
    @Override
    public void receiveEditKeywords() {
        String lang = getSupportedLanguage(Settings.language);
        Gson gson = new Gson();
        String keywordJson = Gdx.files.internal("LunarAssets/locals/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(keywordJson, Keyword[].class);
        assert keywords != null;
        for (Keyword k : keywords) {
            BaseMod.addKeyword(k.NAMES, k.DESCRIPTION);
        }
    }
    
    @Override
    public void receiveEditRelics() {
        achvTracker.checkUnlocks();
        
        RelicAttrs.Initialize();
        RelicManager.LoadRelics();
        EquipmentManager.LoadEquipments();
    }
    
    @Override
    public void receiveEditStrings() {
        String lang = getSupportedLanguage(Settings.language);
        BaseMod.loadCustomStringsFile(RelicStrings.class, "LunarAssets/locals/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "LunarAssets/locals/" + lang + "/ui.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, "LunarAssets/locals/" + lang + "/events.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "LunarAssets/locals/" + lang + "/powers.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "LunarAssets/locals/" + lang + "/monsters.json");
    }
    
    @Override
    public void receiveOnInitialize() {
        LocalePack = new LocaleManager();
    }
    
    @Override
    public void receivePostInitialize() {
        LunarImageMst.Initialize();
        
        BaseMod.registerCustomReward(MiscRewardEnum.LUNAR_COIN, 
                (onLoad) -> {
                    return ItemHelper.GetLunarCoinReward(onLoad.amount);
                },
                (onSave) -> {
                    return new RewardSave(onSave.type.toString(), "LunarCoinReward", ((LunarMiscReward) onSave).coins, 0);
                });
        BaseMod.addSaveField("LunarMiscConfig", new MiscConfig());
        
        loadEvents();
        makeModPanels();
        
        OmniPanel = new OmniPanel();
        EqmtProxy = new EquipmentProxy();
        ItemPicker = new PickerScreen();
        
        AchvManager.Initialize();
    
        MsgHelper.Log();
    }
    
    private static void loadEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(CleansingPoolEvent.ID, CleansingPoolEvent.class)
                .spawnCondition(new Condition() {
                    @Override
                    public boolean test() {
                        return EventManager.CanEventSpawn(CleansingPoolEvent.KEY);
                    }
                })
                .bonusCondition(new Condition() {
                    @Override
                    public boolean test() {
                        return EventManager.CanEventSpawn(CleansingPoolEvent.KEY);
                    }
                })
                .eventType(EventUtils.EventType.SHRINE)
                .create());
//        BaseMod.addEvent(new AddEventParams.Builder(LunarMerchantEvent.ID, LunarMerchantEvent.class).create());
    }
    
    private static void makeModPanels() {
        ModPanel settings = new ModPanel();
        ModLabeledToggleButton PASS_STARTER = new ModLabeledToggleButton("将维度碎片作为初始遗物(Starts With Fragment)", 
                350F, 700F, Color.WHITE.cpy(), FontHelper.charDescFont, MakePassAStarterRelic, settings, (l) -> {},
                (btn) -> {
            MakePassAStarterRelic = btn.enabled;
            SpireConfig config = makeConfig();
            config.setBool("makePassStarting", MakePassAStarterRelic);
            save(config);
                });
        settings.addUIElement(PASS_STARTER);
        BaseMod.registerModBadge(LunarImageMst.Badge, MOD_NAME, Arrays.toString(AUTHORS), DESCRIPTION, settings);
    }
    
    public static void ReloadPanel() {
        OmniPanel = new OmniPanel();
        EqmtProxy = new EquipmentProxy();
    }
    
    public static void addToBot(AbstractGameAction action) {
        actionList.add(action);
    }
    
    public static void addToTop(AbstractGameAction action) {
        actionList.add(0, action);
    }
    
    public static void PreloadItemPicker() {
        if (ItemPicker == null)
            ItemPicker = new PickerScreen();
        ItemPicker.clear();
    }
    
    public static void PreloadItemPicker(Color theme, Color outline) {
        PreloadItemPicker();
        ItemPicker.setColor(outline, theme);
    }
    
    public static void OpenCommandPicker(PickerCaller caller, Collection<? extends AbstractRelic> relics) {
        ItemPicker.distinctItems(true);
        ItemPicker.open(caller, relics, 1, false, true, false, true);
    }
    
    public static void OpenCleansingPicker(PickerCaller caller, int amount, boolean anyNumber) {
        List<AbstractRelic> relics = LunarUtils.CopyRelicList(LMSK.Player().relics, 
                r -> r instanceof AbstractLunarRelic && ((AbstractLunarRelic) r).props.getClazz() != LunarClass.SPECIAL);
        ItemPicker.distinctItems(false);
        ItemPicker.open(caller, relics, amount, anyNumber, true, true, false);
    }
    
    @Override
    public void receivePostDungeonUpdate() {
        if (canUpdatePanel()) {
            OmniPanel.update();
            EqmtProxy.update();
        }
        if (ItemPicker != null && ItemPicker.opening) {
            ItemPicker.update();
        }
        if (actionList.size() > 0) {
            actionList.get(0).update();
            if (actionList.get(0).isDone)
                actionList.remove(0);
        }
    }
    
    @Override
    public void receivePostUpdate() {
        achvTracker.update();
    }
    
    @Override
    public void receivePostRender(SpriteBatch sb) {
        achvTracker.render(sb);
    }
    
    public static void RenderLunarTopPanel(SpriteBatch sb) {
        if (canRenderOmniPanel()) {
            OmniPanel.render(sb);
        }
        if (ItemPicker != null && ItemPicker.opening) {
            ItemPicker.render(sb);
        }
    }
    
    public static void RenderLunarPanel(SpriteBatch sb) {
        if (EqmtProxy != null) {
            EqmtProxy.render(sb);
        }
    }
    
    private static boolean canUpdatePanel() {
        return OmniPanel != null && EqmtProxy != null;
    }
    
    private static boolean canRenderOmniPanel() {
        return OmniPanel != null && AbstractDungeon.player != null && AbstractDungeon.getCurrMapNode() != null;
    }
    
    public static void OpenAchvGrid() {
        if (achvGrid == null)
            achvGrid = new AchvGrid();
        achvGrid.init();
    }
    
    @Override
    public float receiveOnGainBlock(AbstractCreature target, float blockAmt) {
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof BlockModifierRelic)
                blockAmt = ((BlockModifierRelic) r).onGainBlock(target, blockAmt);
        }
        return blockAmt;
    }
    
    @Override
    public int receiveMaxHPChange(int amount) {
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof AbstractLunarRelic) {
                amount = ((AbstractLunarRelic) r).onPlayerMaxHpChange(amount);
            }
        }
        return MaxHPChangeSubscriber.super.receiveMaxHPChange(amount);
    }
    
    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass cls, ArrayList<String> relics) {
        if (MakePassAStarterRelic) {
            relics.add(LunarPass.ID);
        }
    }
    
    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(AudioMst.TONE_MSG, "LunarAssets/audio/TONE_MSG.ogg");
    }
}