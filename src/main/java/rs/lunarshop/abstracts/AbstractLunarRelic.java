package rs.lunarshop.abstracts;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.common.BetterDamageAllEnemiesAction;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.interfaces.ArmorModifierInterface;
import rs.lunarshop.interfaces.AttackModifierInterface;
import rs.lunarshop.interfaces.CritModifierInterface;
import rs.lunarshop.interfaces.relics.LuckModifierRelic;
import rs.lunarshop.interfaces.RegenModifierInterface;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.abstracts.PlanetRelic;
import rs.lunarshop.abstracts.lunarprops.LunarItemProp;
import rs.lunarshop.ui.OmniPanel;
import rs.lunarshop.utils.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractLunarRelic extends LMCustomRelic implements LunarUtils, ArmorModifierInterface, 
        AttackModifierInterface, RegenModifierInterface, CritModifierInterface, CustomSavable<LunarConfig> {
    
    private static final RelicStrings LunarStrings = CardCrawlGame.languagePack.getRelicStrings(LunarMod.Prefix("LunarRelic"));
    public static final String[] TEXT = LunarStrings.DESCRIPTIONS;
    private static final Map<Integer, Boolean> corruptedMap = new HashMap<>();
    public final LunarItemProp prop;
    private List<LunarTip> info;
    public List<LunarTip> lunarTips;
    private boolean stackable;
    private boolean isEquipment;
    protected boolean isFunder;
    private boolean targetRequired;
    private boolean useAutoActivate;
    private boolean isBattleBased;
    private boolean isTurnBased;
    private boolean depleted;
    protected int stack;
    protected int baseCooldown;
    protected int cooldown;
    protected int baseBattle;
    protected int battles;
    protected int baseTurn;
    protected int turns;
    protected PresetInfoFunction infoFunc;

    private AbstractLunarRelic(LunarItemProp prop, LunarClass clazz, int stack) {
        super(prop.getGameID(), ImageMaster.loadImage("LunarAssets/imgs/items/relics/"
                        + prop.lunarID + ".png"), ImageMaster.loadImage("LunarAssets/imgs/items/relics/"
                + prop.lunarID + ".png"), RelicTier.SPECIAL, prop.sound);
        this.prop = prop.setClazz(clazz);
        info = new ArrayList<>();
        lunarTips = new ArrayList<>();
        stackable = true;
        isEquipment = false;
        isFunder = false;
        targetRequired = false;
        useAutoActivate = false;
        isBattleBased = false;
        isTurnBased = false;
        depleted = false;
        this.stack = stack;
        baseCooldown = cooldown = -1;
        baseBattle = battles = 0;
        baseTurn = turns = 0;
        infoFunc = null;
        updateExtraTips();
    }
    
    protected AbstractLunarRelic(int lunarID, LunarClass clazz, int stack) {
        this(ItemHelper.GetProp(lunarID), clazz, stack);
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    private final void updateItemStats() {
        ItemStatHelper.RelicStats stats = ItemStatHelper.GetRelicStats(this);
        if (stats.maxStack < this.stack)
            ItemStatHelper.PutRelicHighestStack(this, this.stack);
        ItemStatHelper.PutRelicCollectCount(this);
    }
    
    public final AbstractLunarRelic stackAmt(int amt, boolean stacking) {
        if (stackable) {
            if (stack < 0) stack = 0;
            stack += amt;
            flash();
            updatePlayerLuck();
            refreshStats();
            onStackAmt(amt, stacking);
            updateExtraTips();
            updateItemStats();
        }
        return this;
    }
    
    public final AbstractLunarRelic loseStack(int loss) {
        if (stackable && stack >= loss) {
            stack -= loss;
            updatePlayerLuck();
            refreshStats();
            onStackAmt(loss, false);
            updateExtraTips();
        }
        if (stack == 0) {
            LunarMod.addToTop(new QuickAction(() -> cpr().loseRelic(prop.getGameID())));
        }
        return this;
    }
    
    private void updatePlayerLuck() {
        if (this instanceof LuckModifierRelic)
            LunarMaster.Luck(true);
    }
    
    protected void onStackAmt(int amt, boolean stacking) {}
    
    @Override
    public void instantObtain() {
        if (cprHasLunarRelic(prop.lunarID)) {
            if (stackable) {
                Optional<AbstractLunarRelic> item = cprExptRelic(prop.lunarID);
                item.ifPresent(r -> r.stackAmt(1, true));
            }
        } else {
            super.instantObtain();
            if (isEquipment) {
                obtainEquipment(cpr());
            }
            updatePlayerLuck();
            updateItemStats();
        }
    }
    
    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if (cprHasLunarRelic(prop.lunarID)) {
            if (stackable) {
                Optional<AbstractLunarRelic> item = cprExptRelic(prop.lunarID);
                item.ifPresent(r -> r.stackAmt(1, true));
            }
            isDone = true;
            isObtained = true;
            discarded = true;
        } else {
            super.instantObtain(p, slot, callOnEquip);
            if (isEquipment) {
                obtainEquipment(p);
            }
            updatePlayerLuck();
            updateItemStats();
        }
    }
    
    @Override
    public void obtain() {
        if (cprHasLunarRelic(prop.lunarID)) {
            if (stackable) {
                Optional<AbstractLunarRelic> item = cprExptRelic(prop.lunarID);
                item.ifPresent(r -> r.stackAmt(1, true));
            }
            hb.hovered = false;
        } else {
            super.obtain();
            if (isEquipment) {
                obtainEquipment(cpr());
            }
            updatePlayerLuck();
            updateItemStats();
        }
    }
    
    protected void obtainEquipment(AbstractPlayer p) {
        if (LunarMod.EqmtProxy.hasEquipment() || hasOtherEquipment()) {
            String rID;
            if (LunarMod.EqmtProxy.hasEquipment()) {
                rID = LunarMod.EqmtProxy.getEquipment().relicId;
                if (rID.equals(relicId)) return;
            }
            else {
                rID = cpr().relics.stream().filter(r -> r instanceof AbstractLunarRelic
                        && ((AbstractLunarRelic) r).isEquipment && r != this)
                        .findFirst().map(r -> r.relicId).get();
            }
            p.loseRelic(rID);
            AbstractDungeon.topPanel.adjustRelicHbs();
        }
        if (!isSeen) UnlockTracker.markRelicAsSeen(prop.getGameID());
    }
    
    private boolean hasOtherEquipment() {
        return cpr().relics.stream().anyMatch(r -> r instanceof AbstractLunarRelic
                && ((AbstractLunarRelic) r).isEquipment && r != this);
    }
    
    protected void startCooldown() {
        log("Starting cooldown by " + baseCooldown);
        cooldown = baseCooldown;
        if (isEquipment() && !isFunder) {
            for (AbstractRelic r : cpr().relics) {
                if (r instanceof AbstractLunarRelic && !((AbstractLunarRelic) r).isEquipment()) {
                    cooldown = ((AbstractLunarRelic) r)
                            .onEqmtStartCooldown((AbstractLunarEquipment) this, cooldown);
                }
            }
        }
    }
    
    protected void reduceCooldown() {
        reduceCooldown(1);
    }
    
    protected void reduceCooldown(int reduce) {
        if (cooldown > 0) {
            blink();
            cooldown -= reduce;
            if (cooldown <= 0) {
                cooldown = 0;
                if (isEquipment() && !isFunder) {
                    for (AbstractRelic r : cpr().relics) {
                        if (r instanceof AbstractLunarRelic && !((AbstractLunarRelic) r).isEquipment()) {
                            ((AbstractLunarRelic) r).onEqmtRecharge((AbstractLunarEquipment) this);
                        }
                    }
                    if (useAutoActivate)
                        autoUse();
                }
            }
            log("Cooldown reduced: " + cooldown);
        }
    }
    
    protected boolean isFullyCooldowned() {
        return cooldown <= 0;
    }
    
    public boolean canActivateEquipment() {
        return isFullyCooldowned();
    }
    
    protected boolean canActivate() {
        boolean can = true;
        if (isTurnBased)
            can = turns == 0;
        else if (isBattleBased)
            can = battles == 0;
        return can && !isEquipment;
    }
    
    /**
     * call this when nonequipment items run out uses
     */
    protected void deplete() {
        grayscale = true;
        depleted = true;
        if (isTurnBased) {
            turns = baseTurn;
            log("Depleted for " + turns + " turns.");
        }
        if (isBattleBased) {
            battles = baseBattle;
            log("Depleted for " + battles + " battles.");
        }
        updateExtraTips();
    }
    
    public boolean canBeCleansed() {
        return prop.getClazz() == LunarClass.LUNAR || prop.getClazz() == LunarClass.VOID;
    }
    
    protected void corruptItem(@NotNull LunarItemProp itemID) {
        if (corruptedMap.get(itemID.lunarID)) return;
        corruptedMap.put(itemID.lunarID, true);
        log(itemID.lunarID + " has been corrupted");
    }
    
    protected void clarifyItem(@NotNull LunarItemProp itemID) {
        if (!corruptedMap.get(itemID.lunarID)) return;
        corruptedMap.put(itemID.lunarID, false);
        log(itemID.lunarID + " has been clarified");
    }
    
    /**
     * call this when nonequipment items recharge
     */
    protected void recharge() {
        grayscale = false;
        depleted = false;
        blink();
    }
    
    public void blink() {
        flashTimer = 1.5F;
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (isTurnBased) {
            if (turns > 0) 
                turns--;
            else if (turns <= 0) 
                recharge();
        }
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (isBattleBased) {
            if (battles > 0) 
                battles--;
            else if (battles <= 0) 
                recharge();
        }
    }
    
    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        super.renderCounter(sb, inTopPanel);
        if (stackable && stack >= 1) {
            try {
                renderStack(sb, inTopPanel);
            } catch (Exception e) {
                log("Failed to render stack amount: " + stack);
                e.printStackTrace();
            }
        }
        if (isEquipment && cooldown > 0) {
            try {
                renderCooldown(sb, inTopPanel);
            } catch (Exception e) {
                log("Failed to render cooldown: " + cooldown);
                e.printStackTrace();
            }
        }
        renderCustomValues(sb, inTopPanel);
    }
    
    public void renderStack(SpriteBatch sb, boolean inTopPanel) throws IllegalAccessException, NoSuchFieldException {
        Field offset = AbstractRelic.class.getDeclaredField("offsetX");
        offset.setAccessible(true);
        final float offsetX = offset.getFloat(this);
        if (inTopPanel) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, ("x" + stack),
                    offsetX + currentX + scale(40F), currentY + scale(25F), Color.WHITE.cpy());
        } else {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, ("x" + stack),
                    currentX + scale(40F), currentY + scale(25F), Color.WHITE.cpy());
        }
    }
    
    public void renderCooldown(SpriteBatch sb, boolean inTopPanel) throws IllegalAccessException, NoSuchFieldException {
        Field offset = AbstractRelic.class.getDeclaredField("offsetX");
        offset.setAccessible(true);
        final float offsetX = offset.getFloat(this);
        if (inTopPanel) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, cooldownText(),
                    offsetX + currentX + scale(30F), currentY + scale(25F), cooldownColor());
        } else {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, cooldownText(),
                    currentX + scale(30F), currentY + scale(25F), cooldownColor());
        }
    }
    
    protected void renderText(SpriteBatch sb, boolean inTopPanel, String text, float offsetX, float offsetY,
                                    float scale, Color color) {
        try {
            Field offset = AbstractRelic.class.getDeclaredField("offsetX");
            offset.setAccessible(true);
            final float offsetx = offset.getFloat(this);
            if (inTopPanel) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, text, offsetx + currentX + scale(offsetX),
                        currentY + scale(offsetY), scale, color);
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, text, currentX + scale(offsetX),
                        currentY + scale(offsetY), scale, color);
            }
        } catch (Exception e) {
            log("Failed to render custom amount: " + text);
            e.printStackTrace();
        }
    }
    
    protected void renderText(SpriteBatch sb, boolean inTopPanel, String text, float offsetX, float offsetY, Color color) {
        try {
            Field offset = AbstractRelic.class.getDeclaredField("offsetX");
            offset.setAccessible(true);
            final float offsetx = offset.getFloat(this);
            if (inTopPanel) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, text, offsetx + currentX + scale(offsetX),
                        currentY + scale(offsetY), color);
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, text, currentX + scale(offsetX),
                        currentY + scale(offsetY), color);
            }
        } catch (Exception e) {
            log("Failed to render custom amount: " + text);
            e.printStackTrace();
        }
    }
    
    @Override
    public void renderBossTip(SpriteBatch sb) {
        LunarTipHelper.QueueLunarTips(Settings.WIDTH * 0.63F, Settings.HEIGHT * 0.63F, lunarTips);
    }
    
    @Override
    public void renderTip(SpriteBatch sb) {
        if (InputHelper.mX < 1400F * Settings.scale) {
            if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.RELIC_VIEW) {
                LunarTipHelper.QueueLunarTips(90F * Settings.scale, Settings.HEIGHT * 0.7F, lunarTips);
            }
            else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP
                    && this.tips.size() > 2 && !AbstractDungeon.player.hasRelic(relicId)) {
                LunarTipHelper.QueueLunarTips(InputHelper.mX + 60F * Settings.scale, 
                        InputHelper.mY + 180F * Settings.scale, lunarTips);
            }
            else if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(relicId)) {
                LunarTipHelper.QueueLunarTips(InputHelper.mX + 60F * Settings.scale, 
                        InputHelper.mY - 30F * Settings.scale, lunarTips);
            }
            else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                LunarTipHelper.QueueLunarTips(260F * Settings.scale, 
                        InputHelper.mY + 50F * Settings.scale, lunarTips);
            }
            else {
                LunarTipHelper.QueueLunarTips(InputHelper.mX + 50F * Settings.scale, 
                        InputHelper.mY + 50F * Settings.scale, lunarTips);
            }
        } else {
            LunarTipHelper.QueueLunarTips(InputHelper.mX - 350F * Settings.scale, 
                    InputHelper.mY - 50F * Settings.scale, lunarTips);
        }
    }
    
    protected void renderCustomValues(SpriteBatch sb, boolean inTopPanel) {}
    
    private String cooldownText() {
        return isFullyCooldowned() ? "!" : String.valueOf(cooldown);
    }
    
    private Color cooldownColor() {
        return isFullyCooldowned() ? Color.ORANGE.cpy() : Color.WHITE.cpy();
    }
    
    public int getStack() {
        return stack;
    }
    
    public int getCooldown() {
        return cooldown;
    }
    
    public AbstractLunarRelic setStackable(boolean stackable) {
        this.stackable = stackable;
        return this;
    }

    public AbstractLunarRelic setEquipment(boolean isEquipment) {
        this.isEquipment = isEquipment;
        this.stackable = !isEquipment;
        return this;
    }
    
    public AbstractLunarRelic setTargetRequired(boolean required) {
        this.targetRequired = required;
        return this;
    }
    
    public AbstractLunarRelic setUseAutoActivate(boolean useAutoActivate) {
        this.useAutoActivate = useAutoActivate;
        return this;
    }
    
    public boolean isUseAutoActivate() {
        return useAutoActivate;
    }
    
    /**
     * set cooldown for an equipment item
     * @param cooldown the base cooldown to use this item again
     * @param init if set true, cooldown set to 0, otherwise, cooldown set to base
     * @return the item
     * @apiNote this is used for equipment items
     * @see #setBattleUse(int)
     * @see #setTurnUse(int)
     */
    public AbstractLunarRelic setCooldown(int cooldown, boolean init) {
        this.baseCooldown = cooldown;
        this.cooldown = init ? 0 : baseCooldown;
        return this;
    }
    
    /**
     * set this item can be activated per battles
     * @param battles the battles to use this again, default by 1 (per battle)
     * @apiNote this is used for nonequipment items
     * @see #setCooldown(int, boolean)
     */
    protected void setBattleUse(int battles) {
        isBattleBased = true;
        this.baseBattle = battles;
    }
    
    protected void setBattleUse() {
        setBattleUse(1);
    }
    
    /**
     * set this item can be activated per turns
     * @param turns the turns to use this again, default by 1 (per turn)
     * @apiNote this is used for nonequipment items
     * @see #setCooldown(int, boolean)
     */
    protected void setTurnUse(int turns) {
        isTurnBased = true;
        this.baseTurn = turns;
    }
    
    protected void setTurnUse() {
        setTurnUse(1);
    }

    public final boolean canSpawnInShop(int shopType) {
        boolean correctType = shopType == prop.getClazz().type;
        return correctType && canSpawnForShopping(shopType);
    }
    
    public final boolean canSpawnInReward() {
        return canSpawnForReward();
    }

    public boolean canSpawnForShopping(int shopType) {
        return true;
    }
    
    public boolean canSpawnForReward() {
        return true;
    }

    @Override
    public boolean canSpawn() {
        return false;
    }
    
    @Override
    public void log(Object what) {
        LunarMod.LogInfo("[" + name + "]> " + what);
    }
    
    @Override
    public void warn(Object what) {
        LunarMod.LogInfo("[" + name + "]> " + what);
    }
    
    public final boolean canUseOn(AbstractCreature s, AbstractCreature t) {
        return !t.isDeadOrEscaped() && selfCanUseOn(s, t);
    }
    
    protected boolean selfCanUseOn(AbstractCreature s, AbstractCreature t) {
        return true;
    }
    
    @Override
    protected boolean onRightClick() {
        if (useAutoActivate) return false;
        if ((isEquipment() && canActivateEquipment()) || canActivate()) {
            if (targetRequired) {
                updateEquipmentTargetMode();
            } else {
                onUse();
            }
            return true;
        }
        return false;
    }
    
    public void autoUse() {
        if ((isEquipment() && canActivateEquipment()) || canActivate()) {
            if (targetRequired) {
                List<AbstractCreature> opts = getAllExptCreatures(c -> canUseOn(cpr(), c));
                if (!opts.isEmpty()) {
                    getRandom(opts, ItemHelper.GetItemRng()).ifPresent(c -> onTargetedUse(cpr(), c));
                }
                return;
            }
            log("auto using [" + prop.localname + "]");{
                onUse();
            }
        }
    }
    
    protected void updateEquipmentTargetMode() {
        ItemHelper.UseTarget(this, cpr());
    }
    
    public final void onUse() {
        use();
    }
    
    public final void onTargetedUse(AbstractCreature s, AbstractCreature t) {
        use(s, t);
    }
    
    protected void use(AbstractCreature s, AbstractCreature t) {}
    
    protected void use() {}
    
    protected boolean rollCloverLuck(float chance) {
        return ItemHelper.RollCloverLuck(prop, chance);
    }
    
    protected boolean rollCloverBadLuck(float chance) {
        return ItemHelper.RollCloverBadLuck(prop, chance);
    }
    
    protected boolean rollStaticLuck(float chance) {
        return ItemHelper.RollLuck(prop, chance);
    }
    
    protected boolean rollStaticBadLuck(float chance) {
        return ItemHelper.RollBadLuck(prop, chance);
    }
    
    protected void presetInfo(PresetInfoFunction func) {
        this.infoFunc = func;
    }
    
    @Override
    protected void initializeTips() {
        super.initializeTips();
        if (!tips.isEmpty()) {
            if (canDisplayStatsInfo()) {
                tips.stream().filter(t -> t.header.equals(name))
                        .findFirst()
                        .ifPresent(t -> {
                            String origin = t.body;
                            String info = getInfoString();
                            if (info != null && !info.isEmpty()) {
                                origin = origin + " NL NL " + TEXT[0] + " NL " + info;
                            }
                            t.body = origin;
                        });
            }
            lunarTips = LunarTip.FromPowerTips(tips, true, t -> {
                if (t.header.equals(name)) {
                    t.hColor = getTipBgColor();
                }
            });
        }
    }
    
    private final String getInfoString() {
        String[] strings = new String[]{DESCRIPTIONS[1]};
        if (infoFunc != null)
            infoFunc.preset(strings);
        return strings[0];
    }
    
    private final Color getTipBgColor() {
        Color color = selfGetTipBgColor();
        if (color != null) return color;
        if (prop != null) {
            switch (prop.getRarity()) {
                case COMMON:
                    return Color.DARK_GRAY;
                case UNCOMMON:
                    return Color.OLIVE;
                case RARE:
                    return Color.GOLDENROD;
                case LEGEND:
                    return Color.FIREBRICK;
                case MYTHIC:
                    return Color.ROYAL;
                default:
                    return Color.VIOLET;
            }
        }
        return Color.SKY;
    }
    
    protected Color selfGetTipBgColor() {
        return null;
    }
    
    @NotNull
    public final List<LunarTip> getTextInfo() {
        List<LunarTip> info = new ArrayList<>();
        info.add(new LunarTip("", description));
        ItemStatHelper.RelicStats stats = ItemStatHelper.GetRelicStats(this);
        if (!stackable) {
            info.add(new LunarTip("", TEXT[1] + stats.collects));
        } else {
            info.add(new LunarTip("", TEXT[1] + stats.collects + " NL "
                    + TEXT[2] + (stats.maxStack > 0 ? "x" + stats.maxStack : 0)));
        }
        info.add(new LunarTip("", flavorText).makeMsg("FLAVOR"));
        return info;
    }
    
    @Deprecated
    public void constructInfo() {}
    
    protected void addTip(PowerTip tip) {
        if (tips.stream().noneMatch(t -> t.header.equals(tip.header) || t.body.equals(tip.body)))
            tips.add(tip);
    }
    
    protected void addTip(String head, String body) {
        addTip(new PowerTip(head, body));
    }
    
    protected void updateTip(PowerTip tip) {
        Optional<PowerTip> opt = tips.stream().filter(t -> t.header.equals(tip.header) || t.body.equals(tip.body)).findFirst();
        if (opt.isPresent()) {
            opt.get().body = tip.body;
        } else {
            addTip(tip);
        }
    }
    
    protected void updateTip(String head, String body) {
        updateTip(new PowerTip(head, body));
    }
    
    @Deprecated
    protected void createStatsInfo(String info, Object... args) {
        updateTip(new PowerTip(TEXT[0] + OmniPanel.NAME + TEXT[1], String.format(info, args)));
    }
    
    protected void createInfo(@NotNull String[] rawInfo, Object... args) {
         rawInfo[0] = String.format(rawInfo[0], args);
    }
    
    protected String createInfo(@NotNull String rawInfo, Object... args) {
        return String.format(rawInfo, args);
    }
    
    private boolean canDisplayStatsInfo() {
        return LunarUtils.RoomAvailable() && cpr() != null && LunarMod.OmniPanel != null && LunarMod.OmniPanel.showDetails();
    }
    
    public void callByOmniPanelCheck() {
        updateExtraTips();
    }
    
    protected void updateExtraTips() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        if (LunarUtils.RoomAvailable())
            refreshStats();
        initializeTips();
    }
    
    @Override
    public void update() {
        super.update();
        if (hb.hovered && canDisplayStatsInfo())
            updateExtraTips();
    }
    
    public void refreshStats() {}
    
    public final boolean isEquipment() {
        return isEquipment;
    }
    
    public final Color getRenderColor() {
        if (this instanceof LunarRelic || this instanceof LunarEquipment)
            return ColorHelper.Lunar;
        if (this instanceof PlanetRelic)
            return ColorHelper.Planet;
        return ColorHelper.White;
    }
    
    public final int getPopupIcon() {
        return prop.popupIcon;
    }
    
    @Override
    public int getPrice() {
        return prop.priceGolds();
    }
    
    @Override
    public final LunarConfig onSave() {
        RelicConfigBuilder builder = new RelicConfigBuilder(this);
        builder.alterValue(isEquipment, cooldown, stack);
        builder.map("battles", String.valueOf(battles));
        builder.map("turns", String.valueOf(turns));
        saveThings(builder);
        return builder.build();
    }
    
    protected void saveThings(RelicConfigBuilder builder) {}
    
    @Override
    public final void onLoad(LunarConfig lunarConfig) {
        if (lunarConfig != null && lunarConfig.getSaver() == prop.lunarID) {
            if (isEquipment)
                cooldown = lunarConfig.getValue();
            else
                stack = lunarConfig.getValue();
            if (lunarConfig.hasMapKey("battles"))
                battles = Integer.parseInt(lunarConfig.getMapValue("battles"));
            if (lunarConfig.hasMapKey("turns")) 
                turns = Integer.parseInt(lunarConfig.getMapValue("turns"));
            loadThings(lunarConfig);
            refreshStats();
            updateExtraTips();
        }
    }
    
    protected void loadThings(LunarConfig config) {}
    
    @Override
    public Type savedType() {
        return new TypeToken<LunarConfig>(){}.getType();
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, AbstractCreature s, int damage, DamageInfo.DamageType type, 
                                             AbstractGameAction.AttackEffect effect, DamageInfoTag... tags) {
        CustomDmgInfo info = crtDmgInfo(s, damage, type);
        DamageInfoTag.PutTags(info, tags);
        return new NullableSrcDamageAction(t, info, effect);
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, AbstractCreature s, int damage, 
                                             DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        return damage(t, s, damage, type, effect, null);
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, int damage, AbstractGameAction.AttackEffect effect,
                                             DamageInfoTag... tags) {
        return damage(t, cpr(), damage, DamageInfo.DamageType.THORNS, effect, tags);
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, int damage, AbstractGameAction.AttackEffect effect) {
        return damage(t, cpr(), damage, DamageInfo.DamageType.THORNS, effect, null);
    }
    
    protected BetterDamageAllEnemiesAction damageAll(AbstractCreature s, int baseDamage, AbstractGameAction.AttackEffect effect, 
                                                     DamageInfoTag... tags) {
        CustomDmgInfo[] dmgInfos = CustomDmgInfo.createInfoArray(crtDmgInfo(s, baseDamage, DamageInfo.DamageType.THORNS), 
                true);
        if (tags != null && tags.length > 0 && dmgInfos.length > 0) {
            for (CustomDmgInfo i : dmgInfos) {
                DamageInfoTag.PutTags(i, tags);
            }
        }
        return new BetterDamageAllEnemiesAction(dmgInfos, effect, true, false, null);
    }
    
    protected BetterDamageAllEnemiesAction damageAll(AbstractCreature s, int baseDamage, boolean pure,
                                                     DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect,
                                                     Consumer<AbstractCreature> func, DamageInfoTag... tags) {
        CustomDmgInfo[] dmgInfos = CustomDmgInfo.createInfoArray(crtDmgInfo(s, baseDamage, type), pure);
        if (tags != null && tags.length > 0 && dmgInfos.length > 0) {
            for (CustomDmgInfo i : dmgInfos) {
                DamageInfoTag.PutTags(i, tags);
            }
        }
        return new BetterDamageAllEnemiesAction(dmgInfos, effect, pure, false, func);
    }
    
    protected BetterDamageAllEnemiesAction damageAll(AbstractCreature s, int baseDamage, AbstractGameAction.AttackEffect effect) {
        int[] damage = DamageInfo.createDamageMatrix(baseDamage, true);
        return new BetterDamageAllEnemiesAction(damage, crtDmgSrc(s), DamageInfo.DamageType.THORNS, effect);
    }
    
    protected void atbTmpAction(Runnable action) {
        addToBot(new QuickAction(action));
    }
    
    protected void attTmpAction(Runnable action) {
        addToTop(new QuickAction(action));
    }
    
    public void afterEqmtActivated(AbstractLunarEquipment equipment) {}
    public int onEqmtStartCooldown(AbstractLunarEquipment equipment, int cooldown) {
        return cooldown;
    }
    public void onEqmtRecharge(AbstractLunarEquipment equipment) {}
    
    /**
     * this applies before a critical hit functions
     */
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {}
    public void afterOneDamaged(DamageInfo info, AbstractCreature who) {}
    public void onProbablyKillMonster(DamageInfo info, int damageAmt, AbstractMonster m) {}
    
    public int onPlayerMaxHpChange(int amount) {
        return amount;
    }
    
    /**
     * Calls later than {@link #onPlayerHeal(int)}
     */
    public int preModifyHeal(AbstractCreature who, int healAmt) {
        return healAmt;
    }
    
    @FunctionalInterface
    public interface PresetInfoFunction {
        void preset(String[] rawStrings);
    }
}