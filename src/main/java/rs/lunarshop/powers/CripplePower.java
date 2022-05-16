package rs.lunarshop.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.powers.ArmorModifierPower;
import rs.lunarshop.subjects.AbstractLunarPower;

public class CripplePower extends AbstractLunarPower implements ArmorModifierPower {
    public static final String POWER_ID = LunarMod.Prefix("CripplePower");
    
    public CripplePower(AbstractCreature owner, int amount) {
        super(POWER_ID, "cripple", PowerType.DEBUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    @Override
    public int modifyArmor(int origin) {
        return origin - 10 * amount;
    }
    
    @Override
    public String preSetDescription() {
        int loss = 10 * amount;
        setAmtValue(0, loss);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new CripplePower(owner, amount);
    }
}