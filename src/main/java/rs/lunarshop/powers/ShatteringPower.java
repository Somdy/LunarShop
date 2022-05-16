package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.Shattering;
import rs.lunarshop.subjects.AbstractLunarPower;

public class ShatteringPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("ShatteringPower");
    
    public ShatteringPower(AbstractCreature owner) {
        super(POWER_ID, "shattering", PowerType.DEBUFF, owner);
        setValues(1);
        updateDescription();
    }
    
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount >= Shattering.SHATTERED_NEED && cprHasLunarRelic(ItemID.Shattering)) {
            flash();
            addToTop(new ApplyPowerAction(owner, owner, new ShatteredPower(owner)));
            addToTop(new RelicAboveCreatureAction(owner, cpr().getRelic(ItemID.Shattering.internalID)));
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public void onInitialApplication() {
        if (owner.powers.stream().anyMatch(p -> p instanceof ShatteredPower)) {
            warn("Applying shattering to " + owner.name + " having been shattered");
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    @Override
    public String preSetDescription() {
        int left = Shattering.SHATTERED_NEED - amount;
        setAmtValue(0, left);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ShatteringPower(owner);
    }
}