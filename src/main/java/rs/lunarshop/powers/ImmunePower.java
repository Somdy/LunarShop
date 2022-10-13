package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.listeners.ApplyPowerListener;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class ImmunePower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("ImmunePower");
    private int aID;
    
    public ImmunePower(AbstractCreature owner, int turns) {
        super(POWER_ID, "immune", PowerType.BUFF, owner);
        setValues(turns);
        updateDescription();
    }
    
    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }
    
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return 0;
    }
    
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        return 0;
    }
    
    @Override
    public int onLoseHp(int damageAmount) {
        return 0;
    }
    
    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        aID = ApplyPowerListener.AddNewManipulator("Immune".length(), 0, e -> owner.hasPower(POWER_ID), 
                (p, s, t) -> {
            if (t == owner && owner.hasPower(POWER_ID) && isPowerTypeOf(p, PowerType.DEBUFF)) {
                flashWithoutSound();
                p = null;
            }
            return p;
                });
    }
    
    @Override
    public void onRemove() {
        super.onRemove();
        ApplyPowerListener.RemoveManipulator(aID);
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0] + (amount > 0 ? DESCRIPTIONS[1] : "");
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new ImmunePower(owner, amount);
    }
}