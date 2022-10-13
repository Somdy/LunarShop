package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.ArmorModifierInterface;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class ElephantPower extends AbstractLunarPower implements ArmorModifierInterface {
    public static final String POWER_ID = LunarMod.Prefix("ElephantPower");
    
    public ElephantPower(AbstractCreature owner, int armor, int turns) {
        super(POWER_ID, "elephant", PowerType.BUFF, owner);
        setValues(armor, turns);
        updateDescription();
    }
    
    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }
    
    @Override
    public int modifyArmor(int origin) {
        if (amount > 0 && extraAmt > 0) {
            origin += amount;
        }
        return ArmorModifierInterface.super.modifyArmor(origin);
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ElephantPower(owner, amount, extraAmt);
    }
}