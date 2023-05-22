package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class SlowdownPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("SlowdownPower");
    private float slowRate;
    private float damageGive;
    private float damageTake;
    
    public SlowdownPower(AbstractCreature owner, float percent, int turns) {
        super(POWER_ID, "slow_m", PowerType.DEBUFF, owner);
        setValues(turns);
        if (percent > 1F) percent = 1F;
        slowRate = percent;
        calcVars();
    }
    
    private void calcVars() {
        if (slowRate > 0) {
            damageGive = slowRate / 2F;
            damageTake = slowRate;
            updateDescription();
        }
    }
    
    public void stackSlow(float percent) {
        if (percent > slowRate) {
            slowRate = slowRate + slowRate / (1F + percent);
            if (slowRate > 1F) slowRate = 1F;
            calcVars();
        }
    }
    
    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (amount > 0 && damageGive > 0) {
            damage = damage - damage * damageGive;
        }
        return super.atDamageFinalGive(damage, type);
    }
    
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (amount > 0 && damageTake > 0) {
            damage = damage + damage * damageTake;
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
        setAmtValue(0, SciPercent(damageGive));
        setAmtValue(1, SciPercent(damageTake));
        setAmtValue(2, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new SlowdownPower(owner, slowRate, amount);
    }
}