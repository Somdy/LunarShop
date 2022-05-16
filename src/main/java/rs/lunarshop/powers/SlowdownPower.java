package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public class SlowdownPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("SlowdownPower");
    private float slowRate;
    private float damageRe;
    private float damageTa;
    
    public SlowdownPower(AbstractCreature owner, float percent, int turns) {
        super(POWER_ID, "slow_m", PowerType.DEBUFF, owner);
        setValues(turns);
        slowRate = percent;
        calcVars();
    }
    
    private void calcVars() {
        if (slowRate > 0) {
            damageRe = slowRate / 2F;
            damageTa = slowRate / 2F;
            updateDescription();
        }
    }
    
    public void stackSlow(float percent) {
        if (percent > slowRate) {
            slowRate = slowRate + slowRate * (1 + percent);
            calcVars();
        }
    }
    
    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (amount > 0 && damageRe > 0) {
            damage = damage - damage * damageRe;
        }
        return super.atDamageFinalGive(damage, type);
    }
    
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (amount > 0 && damageTa > 0) {
            damage = damage + damage * damageTa;
        }
        return super.atDamageFinalReceive(damage, type);
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount > 0 && !owner.isDeadOrEscaped()) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, SciPercent(damageRe));
        setAmtValue(1, SciPercent(damageTa));
        setAmtValue(2, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new SlowdownPower(owner, slowRate, amount);
    }
}