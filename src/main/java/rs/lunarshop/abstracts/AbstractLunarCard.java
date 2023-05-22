package rs.lunarshop.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.lazymankits.abstracts.DamageInfoTag;
import rs.lazymankits.abstracts.LMCustomCard;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.abstracts.lunarprops.LunarCardProp;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.localizations.LunarCardLocals;
import rs.lunarshop.localizations.LunarLocalLoader;
import rs.lunarshop.patches.card.LunarCardEnum;
import rs.lunarshop.utils.InfoTagHelper;
import rs.lunarshop.utils.LunarCardHelper;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.mechanics.AttackHelper;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractLunarCard extends LMCustomCard implements LunarUtils {
    private static final int ED = 0;
    private static final int EB = 1;
    private static final int EM = 2;
    
    private final int[] baseExtraNumbers;
    private final int[] extraNumbers;
    private final boolean[] upgradedExtraNumber;
    private final boolean[] isExtraNumberModified;
    public int[] extraMultiDamage;
    
    public final LunarCardProp prop;
    protected final LunarCardLocals cardLocals;
    protected final String NAME;
    protected final String DESCRIPTION;
    protected final String[] UPGRADE_DESC;
    protected final String[] MSG;
    
    private AbstractLunarCard(LunarCardProp prop, CardTarget target) {
        super(prop.getGameID(), "uninitialized", LunarUtils.CardImageURL(String.valueOf(prop.lunarID)), -2, 
                "uninitialized", CardType.SKILL, LunarCardEnum.LUNAR_CARD_COLOR, CardRarity.SPECIAL, target);
        this.prop = prop;
        cardLocals = LunarLocalLoader.GetCardLocal(prop.getLocalID());
        NAME = cardLocals.NAME;
        DESCRIPTION = cardLocals.DESCRIPTION;
        UPGRADE_DESC = cardLocals.UPGRADE_DESC;
        MSG = cardLocals.MSG != null ? cardLocals.MSG : new String[]{"NO MSG"};
        baseExtraNumbers = new int[3];
        extraNumbers = new int[3];
        upgradedExtraNumber = new boolean[3];
        isExtraNumberModified = new boolean[3];
        initializeValues();
    }
    
    public AbstractLunarCard(int lunarID, CardTarget target) {
        this(LunarCardHelper.GetProp(lunarID), target);
    }
    
    private void initializeValues() {
        name = NAME;
        rawDescription = DESCRIPTION;
        initializeTitle();
        initializeDescription();
        type = prop.type;
        rarity = prop.rarity;
        setCostValue(prop.getCost(false), true);
        setDamageValue(prop.getDamage(false), true);
        setBlockValue(prop.getBlock(false), true);
        setMagicValue(prop.getMagic(false), true);
        extraNumbers[ED] = baseExtraNumbers[ED] = prop.getExtraDamage(false);
        extraNumbers[EB] = baseExtraNumbers[EB] = prop.getExtraBlock(false);
        extraNumbers[EM] = baseExtraNumbers[EM] = prop.getExtraMagic(false);
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        applyExtraPowers();
    }
    
    public void applyExtraPowers() {
        applyPowersToExtraBlock();
        applyPowersToExtraMagic();
        isExtraNumberModified[ED] = false;
        if (!isMultiDamage) {
            float tmp = baseExtraNumbers[ED];
            tmp = applyExtraDamage(tmp, null);
            isExtraNumberModified[ED] = baseExtraNumbers[ED] != MathUtils.floor(tmp);
            extraNumbers[ED] = MathUtils.floor(tmp);
        } else {
            List<AbstractMonster> monsterList = currRoom().monsters.monsters;
            extraMultiDamage = new int[monsterList.size()];
            for (int i = 0; i < extraMultiDamage.length; i++) {
                float tmp = baseExtraNumbers[ED];
                tmp = applyExtraDamage(tmp, monsterList.get(i));
                isExtraNumberModified[ED] = baseExtraNumbers[ED] != MathUtils.floor(tmp);
                extraMultiDamage[i] = MathUtils.floor(tmp);
            }
            extraNumbers[ED] = extraMultiDamage[0];
        }
    }
    
    private float applyExtraDamage(float base, AbstractCreature target) {
        float tmp = base;
        for (AbstractRelic r : cpr().relics) {
            tmp = r.atDamageModify(tmp, this);
        }
        for (AbstractPower p : cpr().powers) {
            tmp = p.atDamageGive(tmp, damageTypeForTurn, this);
        }
        tmp = AttackHelper.ApplyPowersToPlayer(cpr(), tmp);
        tmp = cpr().stance.atDamageGive(tmp, damageTypeForTurn, this);
        if (target != null) {
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageReceive(tmp, damageTypeForTurn, this);
            }
        }
        for (AbstractPower p : cpr().powers) {
            tmp = p.atDamageFinalGive(tmp, damageTypeForTurn, this);
        }
        if (target != null) {
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageFinalReceive(tmp, damageTypeForTurn, this);
            }
        }
        if (tmp < 0F) tmp = 0F;
        return tmp;
    }
    
    protected void applyPowersToExtraBlock() {
        isExtraNumberModified[EB] = false;
        float tmp = baseExtraNumbers[EB];
        for (AbstractPower p : cpr().powers) {
            tmp = p.modifyBlock(tmp, this);
        }
        for (AbstractPower p : cpr().powers) {
            tmp = p.modifyBlockLast(tmp);
        }
        if (tmp < 0F) tmp = 0F;
        isExtraNumberModified[EB] = baseExtraNumbers[EB] != MathUtils.floor(tmp);
        extraNumbers[EB] = MathUtils.floor(tmp);
    }
    
    protected void applyPowersToExtraMagic() {
        isExtraNumberModified[EM] = false;
        float tmp = baseExtraNumbers[EM];
        // ???
        if (tmp < 0F) tmp = 0F;
        isExtraNumberModified[EM] = baseExtraNumbers[EM] != MathUtils.floor(tmp);
        extraNumbers[EM] = MathUtils.floor(tmp);
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        calculateExtraCardDamage(mo);
    }
    
    public void calculateExtraCardDamage(AbstractMonster mo) {
        applyPowersToExtraBlock();
        applyPowersToExtraMagic();
        isExtraNumberModified[ED] = false;
        if (!isMultiDamage) {
            float tmp = baseExtraNumbers[ED];
            tmp = applyExtraDamage(tmp, mo);
            isExtraNumberModified[ED] = baseExtraNumbers[ED] != MathUtils.floor(tmp);
            extraNumbers[ED] = MathUtils.floor(tmp);
        } else {
            List<AbstractMonster> monsterList = currRoom().monsters.monsters;
            extraMultiDamage = new int[monsterList.size()];
            for (int i = 0; i < extraMultiDamage.length; i++) {
                float tmp = baseExtraNumbers[ED];
                tmp = applyExtraDamage(tmp, monsterList.get(i));
                isExtraNumberModified[ED] = baseExtraNumbers[ED] != MathUtils.floor(tmp);
                extraMultiDamage[i] = MathUtils.floor(tmp);
            }
            extraNumbers[ED] = extraMultiDamage[0];
        }
    }
    
    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (isEDUpgraded())
            setEDModified(true);
        if (isEBUpgraded())
            setEBModified(true);
        if (isEMUpgraded())
            setEMModified(true);
    }
    
    @Override
    public final void upgrade() {
        if (canUpgrade()) {
            upgradeNumbers();
            upgradeTexts();
            selfUpgrade();
        }
    }
    
    protected void upgradeNumbers() {
        if (prop.getCost(true) != prop.getCost(false)) 
            upgradeBaseCost(prop.getCost(true));
        if (prop.getDamage(true) > 0) 
            upgradeDamage(prop.getDamage(true));
        if (prop.getBlock(true) > 0) 
            upgradeBlock(prop.getBlock(true));
        if (prop.getMagic(true) > 0) 
            upgradeMagicNumber(prop.getMagic(true));
        if (prop.getExtraDamage(true) > 0) 
            upgradeED(prop.getExtraDamage(true));
        if (prop.getExtraBlock(true) > 0) 
            upgradeEB(prop.getExtraBlock(true));
        if (prop.getExtraMagic(true) > 0) 
            upgradeEM(prop.getExtraMagic(true));
    }
    
    public void upgradeED(int amount) {
        baseExtraNumbers[ED] += amount;
        extraNumbers[ED] = baseExtraNumbers[ED];
        upgradedExtraNumber[ED] = true;
    }
    
    public void upgradeEB(int amount) {
        baseExtraNumbers[EB] += amount;
        extraNumbers[EB] = baseExtraNumbers[EB];
        upgradedExtraNumber[EB] = true;
    }
    
    public void upgradeEM(int amount) {
        baseExtraNumbers[EM] += amount;
        extraNumbers[EM] = baseExtraNumbers[EM];
        upgradedExtraNumber[EM] = true;
    }
    
    protected void upgradeTexts() {
        upgradeName();
        if (!UPGRADE_DESC[0].isEmpty()) {
            updateDescription(UPGRADE_DESC[0]);
        }
    }
    
    protected void updateDescription(String newDescription) {
        rawDescription = newDescription;
        initializeDescription();
    }
    
    protected void selfUpgrade() {}
    
    @Override
    public final void use(AbstractPlayer p, AbstractMonster m) {
        play(p, m);
    }
    
    public abstract void play(AbstractCreature s, AbstractCreature t);
    
    protected void useCustomBg(String locator) {
        if (!Gdx.files.internal("LunarAssets/imgs/cardui/custom/" + locator).exists()) {
            log("custom bg [" + locator + "] does not exist");
            return;
        }
        String type = this.type.name().toLowerCase();
        setBackgroundTexture("LunarAssets/imgs/cardui/custom/" + locator + "/bg_" + type + "_512.png", 
                "LunarAssets/imgs/cardui/custom/" + locator + "/bg_" + type + "_1024.png");
        setOrbTexture("LunarAssets/imgs/cardui/custom/" + locator + "/card_cost.png", 
                "LunarAssets/imgs/cardui/custom/" + locator + "/card_orb.png");
    }
    
    public void setEDModified(boolean modified) {
        isExtraNumberModified[ED] = modified;
    }
    
    public void setEBModified(boolean modified) {
        isExtraNumberModified[EB] = modified;
    }
    
    public void setEMModified(boolean modified) {
        isExtraNumberModified[EM] = modified;
    }
    
    public boolean isEDModified() {
        return isExtraNumberModified[ED];
    }
    
    public boolean isEBModified() {
        return isExtraNumberModified[EB];
    }
    
    public boolean isEMModified() {
        return isExtraNumberModified[EM];
    }
    
    public boolean isEDUpgraded() {
        return upgradedExtraNumber[ED];
    }
    
    public boolean isEBUpgraded() {
        return upgradedExtraNumber[EB];
    }
    
    public boolean isEMUpgraded() {
        return upgradedExtraNumber[EM];
    }
    
    public int getBaseED() {
        return baseExtraNumbers[ED];
    }
    
    public int getED() {
        return extraNumbers[ED];
    }
    
    public int getBaseEB() {
        return baseExtraNumbers[EB];
    }
    
    public int getEB() {
        return extraNumbers[EB];
    }
    
    public int getBaseEM() {
        return baseExtraNumbers[EM];
    }
    
    public int getEM() {
        return extraNumbers[EM];
    }
    
    @Override
    public void log(Object what) {
        LunarMod.LogInfo("[" + prop.localname + "]> " + what);
    }
    
    protected void atbTmpAction(Runnable action) {
        addToBot(new QuickAction(action));
    }
    
    protected void attTmpAction(Runnable action) {
        addToTop(new QuickAction(action));
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage, DamageInfo.DamageType type,
                                                   AbstractGameAction.AttackEffect effect, Consumer<DamageInfo> c, DamageInfoTag tags) {
        CustomDmgInfo info = crtDmgInfo(s, damage, type);
        InfoTagHelper.PutTags(info, tags);
        if (c != null) c.accept(info);
        return new NullableSrcDamageAction(t, info, effect);
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage,
                                                   DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        return DamageAction(t, s, damage, type, effect, null, (InfoTagHelper) null);
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage, AbstractGameAction.AttackEffect effect,
                                                   Consumer<DamageInfo> c, DamageInfoTag tags) {
        return DamageAction(t, s, damage, damageTypeForTurn, effect, c, tags);
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage, AbstractGameAction.AttackEffect effect, 
                                                   Consumer<DamageInfo> c) {
        return DamageAction(t, s, damage, damageTypeForTurn, effect, c, (InfoTagHelper) null);
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage, AbstractGameAction.AttackEffect effect) {
        return DamageAction(t, s, damage, damageTypeForTurn, effect, null, (InfoTagHelper) null);
    }
}