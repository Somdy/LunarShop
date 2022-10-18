package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.planet.Spleen;
import rs.lunarshop.abstracts.AbstractLunarPower;

public class BleedingPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("BleedingPower");
    
    public BleedingPower(AbstractCreature owner, int amount) {
        super(POWER_ID, "bleeding", PowerType.DEBUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    void checkDamage() {
        int base = 1;
        if (Spleen.On()) {
            base += owner.currentHealth * Spleen.BleedBuff();
            base *= 2;
        }
        extraAmt = base;
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        checkDamage();
        if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL && amount > 0) {
            addToBot(new LoseHPAction(owner, null, extraAmt));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
            updateDescription();
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        updateDescription();
    }
    
    @Override
    public String preSetDescription() {
        checkDamage();
        setAmtValue(0, extraAmt);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new BleedingPower(owner, amount);
    }
}