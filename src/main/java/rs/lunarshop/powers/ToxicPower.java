package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.core.LunarMod;

public class ToxicPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("ToxicPower");
    private static final int LASTING_TURNS = 3;
    
    public ToxicPower(AbstractCreature owner, AbstractCreature source, int damage) {
        super(POWER_ID, "poison", PowerType.DEBUFF, owner);
        setValues(source, LASTING_TURNS, damage);
        isExtraAmtFixed = false;
        checkDamage();
        updateDescription();
    }
    
    private void checkDamage() {
        if (extraAmt > owner.currentHealth)
            extraAmt = owner.currentHealth - 1;
    }
    
    @Override
    public void atEndOfRound() {
        if (amount > 0) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        } else {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public void atStartOfTurn() {
        if (extraAmt > 0) {
            flash();
            checkDamage();
            addToBot(damage(owner, extraAmt, AbstractGameAction.AttackEffect.POISON));
        }
    }
    
    @Override
    public int getMaxAmount() {
        return LASTING_TURNS;
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, extraAmt);
        setAmtValue(1, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ToxicPower(owner, source, extraAmt);
    }
}