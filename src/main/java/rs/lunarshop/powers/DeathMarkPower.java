package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.DeathMark;
import rs.lunarshop.subjects.AbstractLunarPower;

public class DeathMarkPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("DeathMarkPower");
    
    public DeathMarkPower(AbstractCreature owner, int amount) {
        super(POWER_ID, "deathmark", PowerType.DEBUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        damage = damage + damage * 0.5F;
        return super.atDamageFinalReceive(damage, type);
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount > 0 && !owner.isDeadOrEscaped()) {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public void onRemove() {
        boolean success = DeathMark.RemoveDeath(owner);
        if (!success)
            Log("Failed to remove " + owner.name + " from death marks");
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new DeathMarkPower(owner, amount);
    }
}