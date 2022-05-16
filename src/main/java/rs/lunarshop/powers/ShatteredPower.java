package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.powers.ArmorModifierPower;
import rs.lunarshop.items.relics.lunar.Shattering;
import rs.lunarshop.subjects.AbstractLunarPower;

public class ShatteredPower extends AbstractLunarPower implements ArmorModifierPower {
    public static final String POWER_ID = LunarMod.Prefix("ShatteredPower");
    
    public ShatteredPower(AbstractCreature owner) {
        super(POWER_ID, "shattered", PowerType.DEBUFF, owner);
        setValues(3);
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount > 0 && !owner.isDeadOrEscaped()) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public int modifyArmor(int origin) {
        return origin - Shattering.ArmorReduction;
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, Shattering.ArmorReduction);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ShatteredPower(owner);
    }
}