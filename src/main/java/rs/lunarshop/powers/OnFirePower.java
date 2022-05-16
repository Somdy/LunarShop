package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public class OnFirePower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("OnFirePower");
    private final boolean refreshLastingTime;
    
    public OnFirePower(AbstractCreature owner, int amount, int turns, boolean refreshLastingTime) {
        super(POWER_ID, "onfire", PowerType.DEBUFF, owner);
        setValues(turns, amount);
        this.refreshLastingTime = refreshLastingTime;
        stackable = this.refreshLastingTime;
        ID += refreshLastingTime;
        isExtraAmtFixed = false;
        updateDescription();
    }
    
    @Override
    public void stackPower(int stackAmount) {
        if (!stackable) return;
        super.stackPower(stackAmount);
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!owner.isDeadOrEscaped() && amount > 0) {
            if (extraAmt > 0)
                addToBot(new NullableSrcDamageAction(cpr(), convert(new DamageInfo(null, extraAmt, 
                        DamageInfo.DamageType.THORNS)), AbstractGameAction.AttackEffect.FIRE));
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