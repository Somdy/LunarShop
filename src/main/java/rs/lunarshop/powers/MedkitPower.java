package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class MedkitPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("MedkitPower");
    public static boolean OnHeal;
    
    public MedkitPower(AbstractCreature owner, int heals, int turns) {
        super(POWER_ID, "medkit", PowerType.BUFF, owner);
        setValues(turns, heals / 2);
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (amount > 0 && extraAmt > 0 && !owner.isDeadOrEscaped()) {
            addToBot(new HealAction(owner, owner, extraAmt));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        } else {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        OnHeal = true;
    }
    
    @Override
    public void onRemove() {
        super.onRemove();
        OnHeal = false;
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, extraAmt);
        setAmtValue(1, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MedkitPower(owner, extraAmt * 2, amount);
    }
}