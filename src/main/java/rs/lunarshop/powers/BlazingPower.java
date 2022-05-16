package rs.lunarshop.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public class BlazingPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("BlazingPower");
    
    public BlazingPower(AbstractCreature owner, int amount) {
        super(POWER_ID, "blazing", PowerType.BUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == owner && damageAmount > 0 && target != owner && amount > 0) {
            addToBot(new ApplyPowerAction(target, owner, new OnFirePower(target, 
                    MathUtils.floor(damageAmount / 2F), amount, false)));
        }
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new BlazingPower(owner, amount);
    }
}