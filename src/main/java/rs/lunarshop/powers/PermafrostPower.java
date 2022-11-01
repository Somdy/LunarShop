package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.AttackModifierInterface;

public class PermafrostPower extends AbstractLunarPower implements AttackModifierInterface {
    public static final String POWER_ID = LunarMod.Prefix("PermafrostPower");
    
    public PermafrostPower(AbstractCreature owner, int turns, int reduction) {
        super(POWER_ID, "permafrost", PowerType.DEBUFF, owner);
        setValues(turns, reduction);
        isExtraAmtFixed = false;
        stackable = false;
        updateDescription();
    }
    
    @Override
    public void atEndOfRound() {
        if (amount > 0) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public int modifyAttack(int origin) {
        if (extraAmt > 0) {
            origin -= extraAmt;
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
        return new PermafrostPower(owner, amount, extraAmt);
    }
}