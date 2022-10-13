package rs.lunarshop.items.equipments;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import rs.lunarshop.items.abstracts.LunarEquipment;

public class CrowdFunder extends LunarEquipment {
    private static final int base = 18;
    private static final float multiplier = 1.5F;
    private int goldCost;
    private float damageMult;
    private boolean isOn;
    
    public CrowdFunder() {
        super(30, 0);
        isFunder = true;
        setTargetRequired(false);
        goldCost = base;
        damageMult = 0F;
        isOn = false;
        presetInfo(this::setInfo);
    }
    
    @Override
    public void refreshStats() {
        goldCost = base + (currFloor() > base ? MathUtils.ceil(currFloor() * multiplier) : 0);
        damageMult = goldCost * (1.5F + currFloor() * 0.01F) / 100F;
    }
    
    private void setInfo(String[] s) {
        if (isOn) {
            s[0] = createInfo(DESCRIPTIONS[1], goldCost, SciPercent(damageMult));
        } else {
            s[0] = createInfo(DESCRIPTIONS[2]);
        }
    }
    
    @Override
    protected void use() {
        super.use();
        isOn = !isOn;
        updateExtraTips();
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        if (info.owner == cpr() && isOn && cpr().gold > goldCost && info.output > 0 && who != null) {
            info.output = info.output + MathUtils.ceil(info.output * damageMult);
            cpr().loseGold(goldCost);
            addToBot(new VFXAction(new FlickCoinEffect(info.owner.hb.cX, info.owner.hb.cY, who.hb.cX, who.hb.cY), 0.3F));
        }
        super.preModifyDamage(info, who);
    }
}