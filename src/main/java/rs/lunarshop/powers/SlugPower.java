package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.RegenModifierInterface;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class SlugPower extends AbstractLunarPower implements RegenModifierInterface {
    public static final String POWER_ID = LunarMod.Prefix("SlugPower");
    
    public SlugPower(AbstractCreature owner, int amount, int turns) {
        super(POWER_ID, "medkit", PowerType.BUFF, owner);
        setValues(turns, amount);
        isExtraAmtFixed = false;
        updateDescription();
    }
    
    @Override
    public int modifyRegen(int origin) {
        if (amount > 0 && !owner.isDeadOrEscaped() && extraAmt > 0) {
            origin += extraAmt;
        }
        return RegenModifierInterface.super.modifyRegen(origin);
    }
    
    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, extraAmt);
        setAmtValue(1, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new SlugPower(owner, amount, extraAmt);
    }
}