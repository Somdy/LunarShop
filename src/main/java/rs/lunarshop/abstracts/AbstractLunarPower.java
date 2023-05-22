package rs.lunarshop.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.lazymankits.abstracts.DamageInfoTag;
import rs.lazymankits.abstracts.LMCustomPower;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lunarshop.utils.InfoTagHelper;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.PatternHelper;

import java.util.Arrays;
import java.util.regex.Matcher;

public abstract class AbstractLunarPower extends LMCustomPower implements LunarUtils {
    private static final PowerStrings MountPwrStrings = CardCrawlGame.languagePack.getPowerStrings("LunarPower");
    
    public final PowerStrings powerStrings;
    protected String NAME;
    protected String[] DESCRIPTIONS;
    protected String owner_name;
    protected String[] crt_names;
    protected String[] amts;
    
    public AbstractLunarPower(String POWER_ID, String img, PowerType type, AbstractCreature owner) {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        ID = POWER_ID;
        name = NAME;
        this.type = type;
        crt_names = new String[3];
        amts = new String[3];
        Arrays.fill(crt_names, "missing_name");
        Arrays.fill(amts, "missing_value");
        this.owner = owner;
        loadImg(img);
    }
    
    protected void setValues(int amount) {
        super.setValues(owner, null, amount);
        setOwnerName();
    }
    
    protected void setValues(int amount, int extraAmt) {
        super.setValues(owner, null, amount, extraAmt);
        setOwnerName();
    }
    
    protected void setValues(AbstractCreature source, int amount) {
        super.setValues(owner, source, amount);
        setOwnerName();
    }
    
    protected void setValues(AbstractCreature source, int amount, int extraAmt) {
        super.setValues(owner, source, amount, extraAmt);
        setOwnerName();
    }
    
    protected void setOwnerName() {
        owner_name = owner.isPlayer ? cpr().getLocalizedCharacterName() : owner.name;
    }
    
    protected void setAmtValue(int slot, int value) {
        if (slot > amts.length - 1)
            slot = amts.length - 1;
        amts[slot] = String.valueOf(value);
    }
    
    protected void setAmtValue(int slot, float value) {
        if (slot > amts.length - 1)
            slot = amts.length - 1;
        amts[slot] = String.valueOf(value);
    }
    
    protected void setCrtName(int slot, String value) {
        if (slot > crt_names.length - 1)
            slot = crt_names.length - 1;
        crt_names[slot] = value;
    }
    
    @Override
    public void updateDescription() {
        description = preSetDescription();
        description = checkWithPatterns(description);
    }
    
    protected String checkWithPatterns(String origin) {
        origin = checkOwnerName(origin);
        origin = checkAmtValue(origin);
        origin = checkCrtValue(origin);
        return origin;
    }
    
    private String checkAmtValue(String origin) {
        final Matcher matcher = PatternHelper.POWER_AMT.matcher(origin);
        while (matcher.find() && matcher.groupCount() >= 2) {
            int slot = Integer.parseInt(matcher.group(2));
            if (slot <= amts.length - 1) {
                origin = origin.replace(matcher.group(0), amts[slot]);
            }
        }
        return origin;
    }
    
    private String checkCrtValue(String origin) {
        final Matcher matcher = PatternHelper.POWER_TARGET.matcher(origin);
        while (matcher.find() && matcher.groupCount() >= 2) {
            int slot = Integer.parseInt(matcher.group(2));
            if (slot <= crt_names.length - 1) {
                origin = origin.replace(matcher.group(0), crt_names[slot]);
            }
        }
        return origin;
    }
    
    private String checkOwnerName(String origin) {
        final Matcher matcher = PatternHelper.POWER_NAME.matcher(origin);
        if (matcher.find())
            origin = origin.replace(matcher.group(0), owner_name);
        return origin;
    }
    
    @Override
    protected TextureAtlas getPowerAtlas() {
        return new TextureAtlas(Gdx.files.internal("LunarAssets/imgs/powers/powers.atlas"));
    }
    
    public abstract String preSetDescription();
    
    protected NullableSrcDamageAction damage(AbstractCreature t, AbstractCreature s, int damage, DamageInfo.DamageType type,
                                             AbstractGameAction.AttackEffect effect, DamageInfoTag tags) {
        CustomDmgInfo info = crtDmgInfo(s, damage, type);
        InfoTagHelper.PutTags(info, tags);
        return new NullableSrcDamageAction(t, info, effect);
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, AbstractCreature s, int damage,
                                             DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        return damage(t, s, damage, type, effect, (InfoTagHelper) null);
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, int damage, AbstractGameAction.AttackEffect effect,
                                             DamageInfoTag tags) {
        return damage(t, source, damage, DamageInfo.DamageType.THORNS, effect, tags);
    }
    
    protected NullableSrcDamageAction damage(AbstractCreature t, int damage, AbstractGameAction.AttackEffect effect) {
        return damage(t, source, damage, DamageInfo.DamageType.THORNS, effect, (InfoTagHelper) null);
    }
}