package rs.lunarshop.subjects;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.interfaces.relics.AttackModifierRelic;
import rs.lunarshop.interfaces.relics.RegenModifierRelic;
import rs.lunarshop.items.abstracts.PlanetRelic;
import rs.lunarshop.subjects.lunarprops.LunarItemID;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.interfaces.relics.ArmorModifierRelic;
import rs.lunarshop.interfaces.relics.LuckModifierRelic;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.relics.RelicAttrs;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.lunarshop.subjects.lunarprops.LunarItem;
import rs.lunarshop.ui.OmniPanel;
import rs.lunarshop.utils.ColorHelper;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.LunarUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public abstract class AbstractLunarRelic extends LMCustomRelic implements LunarUtils, ArmorModifierRelic, AttackModifierRelic, 
        RegenModifierRelic, CustomSavable<LunarConfig> {
    private static final RelicStrings LunarStrings = CardCrawlGame.languagePack.getRelicStrings(LunarMod.Prefix("LunarRelic"));
    private static final Map<Integer, Boolean> corruptedMap = new HashMap<>();
    public final LunarItem props;
    private List<PowerTip> info;
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

    private AbstractLunarRelic(int lunarID, String relicID, LunarClass clazz, int stack) {
        super(relicID, ImageMaster.loadImage("LunarAssets/imgs/items/relics/" + lunarID + ".png"),
                ImageMaster.loadImage("LunarAssets/imgs/items/relics/" + lunarID + ".png"), RelicTier.SPECIAL, 
                LandingSound.CLINK);
        props = new LunarItem(lunarID, relicID).setClazz(clazz);
        info = new ArrayList<>();
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
        loadAttrs();
        updateExtraTips();
    }
    
    protected AbstractLunarRelic(@NotNull LunarItemID itemID, LunarClass clazz, int stack) {
        this(itemID.lunarID, itemID.internalID, clazz, stack);
    }
    
    private void loadAttrs() {
        props.setRarity(RelicAttrs.GetRarity(props.internalID));
        try {
            Field landingSFX = AbstractRelic.class.getDeclaredField("landingSFX");
            landingSFX.setAccessible(true);
            LandingSound sfx = RelicAttrs.GetSFX(props.internalID);
            landingSFX.set(this, sfx);
        } catch (Exception e) {
            log("Failed to load landing sfx");
            e.printStackTrace();
        }
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    public final AbstractLunarRelic stackAmt(int amt, boolean stacking) {
        if (stackable) {
            if (stack < 0) stack = 0;
            stack += amt;
            flash();
            updatePlayerStats();
            refreshStats();
            onStackAmt(amt, stacking);
            updateExtraTips();
        }
        return this;
    }
    
    public final AbstractLunarRelic loseStack(int loss) {
        if (stackable && stack >= loss) {
            stack -= loss;
            updatePlayerStats();
            refreshStats();
            onStackAmt(loss, false);
            updateExtraTips();
        }
        if (stack == 0) {
            LunarMod.addToTop(new QuickAction(() -> cpr().loseRelic(props.internalID)));
        }
        return this;
    }
    
    private void updatePlayerStats() {
        if (this instanceof LuckModifierRelic)
            LunarMaster.Luck(true);
        if (this instanceof ArmorModifierRelic)
            LunarMaster.Armor(true);
    }
    
    protected void onStackAmt(int amt, boolean stacking) {}
    
    @Override
    public void instantObtain() {
        if (cprHasLunarRelic(props.lunarID)) {
            if (stackable) {
                Optional<AbstractLunarRelic> item = cprExptRelic(props.lunarID);
                item.ifPresent(r -> r.stackAmt(1, true));
            }
        } else {
            super.instantObtain();
            if (isEquipment) {
                obtainEquipment(cpr());
            }
            updatePlayerStats();
        }
    }
    
    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if (cprHasLunarRelic(props.lunarID)) {
            if (stackable) {
                Optional<AbstractLunarRelic> item = cprExptRelic(props.lunarID);
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
            updatePlayerStats();
        }
    }
    
    @Override
    public void obtain() {
        if (cprHasLunarRelic(props.lunarID)) {
            if (stackable) {
                Optional<AbstractLunarRelic> item = cprExptRelic(props.lunarID);
                item.ifPresent(r -> r.stackAmt(1, true));
            }
            hb.hovered = false;
        } else {
            super.obtain();
            if (isEquipment) {
                obtainEquipment(cpr());
            }
            updatePlayerStats();
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
        if (!isSeen) UnlockTracker.markRelicAsSeen(props.internalID);
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
                        autoActivate();
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
    
    protected void corruptItem(@NotNull LunarItemID itemID) {
        if (corruptedMap.get(itemID.lunarID)) return;
        corruptedMap.put(itemID.lunarID, true);
        log(itemID.lunarID + " has been corrupted");
    }
    
    protected void clarifyItem(@NotNull LunarItemID itemID) {
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
        if (isTurnBased && turns > 0) {
            turns--;
            if (turns == 0) recharge();
        }
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (isBattleBased && battles > 0) {
            battles--;
            if (battles == 0) recharge();
        }
    }
    
    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        super.renderCounter(sb, inTopPanel);
        if (stackable && stack > 1) {
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
        boolean correctType = shopType == props.getClazz().type;
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
        LunarMod.LogInfo(name + ": " + what);
    }
    
    @Override
    public void warn(Object what) {
        LunarMod.LogInfo(name + ": " + what);
    }
    
    @Override
    protected boolean onRightClick() {
        if (!targetRequired && useAutoActivate) return false;
        if ((isEquipment() && canActivateEquipment()) || canActivate()) {
            log("Activating...");
            if (targetRequired) {
            
            } else {
                setActivate();
            }
            return true;
        }
        return false;
    }
    
    public void autoActivate() {
        if ((isEquipment() && canActivateEquipment()) || canActivate()) {
            if (targetRequired) return;
            log("Auto activating...");{
                setActivate();
            }
        }
    }
    
    public final void setActivate() {
        activate();
    }
    
    protected void activate(AbstractCreature s, AbstractCreature t) {}
    
    protected void activate() {}
    
    protected boolean rollCloverLuck(float chance) {
        return ItemHelper.RollCloverLuck(props.lunarID, chance);
    }
    
    protected boolean rollCloverBadLuck(float chance) {
        return ItemHelper.RollCloverBadLuck(props.lunarID, chance);
    }
    
    protected boolean rollStaticLuck(float chance) {
        return ItemHelper.RollLuck(props.lunarID, chance);
    }
    
    protected boolean rollStaticBadLuck(float chance) {
        return ItemHelper.RollBadLuck(props.lunarID, chance);
    }
    
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
    
    protected void createStatsInfo(String info, Object... args) {
        updateTip(new PowerTip(LunarStrings.DESCRIPTIONS[0] + OmniPanel.NAME + LunarStrings.DESCRIPTIONS[1], 
                String.format(info, args)));
    }
    
    private boolean canDisplayStatsInfo() {
        return info != null && cpr() != null && LunarMod.OmniPanel != null && LunarMod.OmniPanel.showDetails();
    }
    
    public void callByOmniPanelCheck() {
        updateExtraTips();
    }
    
    protected void updateExtraTips() {
        tips.clear();
        tips.add(new PowerTip(name, description));
        if (canDisplayStatsInfo()) {
            refreshStats();
            constructInfo();
            initializeTips();
        }
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
    
    @Override
    public int getPrice() {
        return props.priceGolds();
    }
    
    @Override
    public LunarConfig onSave() {
        RelicConfigBuilder builder = new RelicConfigBuilder(this);
        builder.alterValue(isEquipment, cooldown, stack);
        builder.map("battles", String.valueOf(battles));
        builder.map("turns", String.valueOf(turns));
        saveThings(builder);
        return builder.build();
    }
    
    protected void saveThings(RelicConfigBuilder builder) {}
    
    @Override
    public void onLoad(LunarConfig lunarConfig) {
        if (lunarConfig != null && lunarConfig.getSaver() == props.lunarID) {
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
    
    public void afterEqmtActivated(AbstractLunarEquipment equipment) {}
    public int onEqmtStartCooldown(AbstractLunarEquipment equipment, int cooldown) {
        return cooldown;
    }
    public void onEqmtRecharge(AbstractLunarEquipment equipment) {}
    
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
}