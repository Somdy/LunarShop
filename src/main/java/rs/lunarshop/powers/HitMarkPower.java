package rs.lunarshop.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.core.LunarMod;

public class HitMarkPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("HitMarkPower");
    
    public HitMarkPower(AbstractCreature owner, int amount) {
        super(POWER_ID, "hitmark", PowerType.BUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    @Override
    public String preSetDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new HitMarkPower(owner, amount);
    }
}