package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class MalachitePower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("MalachitePower");
    
    public MalachitePower(AbstractCreature owner, int amount) {
        super(POWER_ID, "malachite", PowerType.BUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == owner && target != owner && damageAmount > 0 && amount > 0) {
            addToBot(new ApplyPowerAction(target, owner, new HealingDisabledPower(target, amount)));
        }
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MalachitePower(owner, amount);
    }
}