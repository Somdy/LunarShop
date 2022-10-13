package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class HealingDisabledPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("HealingDisabledPower");
    
    public HealingDisabledPower(AbstractCreature owner, int turns) {
        super(POWER_ID, "healing_disabled", PowerType.DEBUFF, owner);
        setValues(0, turns);
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (extraAmt > 0 && !owner.isDeadOrEscaped()) {
            extraAmt--;
            if (extraAmt <= 0)
                addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public int onHeal(int healAmount) {
        return 0;
    }
    
    @Override
    public String preSetDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new HealingDisabledPower(owner, amount);
    }
}