package rs.lunarshop.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public final class TonicDebuffPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("TonicDebuffPower");
    
    public TonicDebuffPower(AbstractCreature owner) {
        super(POWER_ID, "tonicdebuff", PowerType.BUFF, owner);
        setValues(-1);
        updateDescription();
    }
    
    @Override
    public int onHeal(int healAmount) {
        healAmount = MathUtils.floor(healAmount * 0.25F);
        return super.onHeal(healAmount);
    }
    
    @Override
    public String preSetDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new TonicDebuffPower(owner);
    }
}