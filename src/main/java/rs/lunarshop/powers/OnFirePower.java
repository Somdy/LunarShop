package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.utils.InfoTagHelper;

public class OnFirePower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("OnFirePower");
    private final boolean refreshLastingTime;
    
    public OnFirePower(AbstractCreature owner, int damageAmount, int turns, boolean refreshLastingTime) {
        super(POWER_ID, "onfire", PowerType.DEBUFF, owner);
        setValues(turns, damageAmount);
        this.refreshLastingTime = refreshLastingTime;
        stackable = this.refreshLastingTime;
        ID += refreshLastingTime;
        isExtraAmtFixed = false;
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!owner.isDeadOrEscaped() && amount > 0) {
            if (extraAmt > 0) {
                addToBot(damage(owner, extraAmt, AbstractGameAction.AttackEffect.FIRE, InfoTagHelper.FIRE));
            }
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, extraAmt);
        setAmtValue(1, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new OnFirePower(owner, amount, extraAmt, refreshLastingTime);
    }
}