package rs.lunarshop.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.actions.common.ApplySlowdownAction;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public class GlacialPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("GlacialPower");
    
    public GlacialPower(AbstractCreature owner, int turns) {
        super(POWER_ID, "glacial", PowerType.BUFF, owner);
        setValues(turns);
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        if (!owner.isDeadOrEscaped() && amount > 0) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != owner && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
            int back = MathUtils.floor(damageAmount * 0.2F);
            if (back > 0) {
                addToTop(new DamageAction(info.owner, new DamageInfo(owner, back, 
                        DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
            }
            addToTop(new ApplySlowdownAction(info.owner, owner, 0.4F, 1));
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new GlacialPower(owner, amount);
    }
}