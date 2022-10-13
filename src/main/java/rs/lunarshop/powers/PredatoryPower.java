package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.AttackModifierInterface;
import rs.lunarshop.items.relics.lunar.PredatoryMask;

public class PredatoryPower extends AbstractLunarPower implements AttackModifierInterface {
    public static final String POWER_ID = LunarMod.Prefix("PredatoryPower");
    
    public PredatoryPower(AbstractCreature owner, int str, int turns) {
        super(POWER_ID, "buffoncrit", PowerType.BUFF, owner);
        setValues(turns, str);
        stackable = false;
        isExtraAmtFixed = false;
        updateDescription();
    }
    
    @Override
    public void stackExtraAmount(int stackAmount) {
        if (extraAmt + stackAmount > PredatoryMask.MaxStr) return;
        super.stackExtraAmount(stackAmount);
    }
    
    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        if (extraAmt > 0 && !owner.isDeadOrEscaped()) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public int modifyAttack(int origin) {
        if (extraAmt > 0 && !owner.isDeadOrEscaped()) {
            origin += extraAmt;
        }
        return AttackModifierInterface.super.modifyAttack(origin);
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, extraAmt);
        setAmtValue(1, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new PredatoryPower(owner, extraAmt, amount);
    }
}