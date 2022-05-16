package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public class RachisPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("RachisPower");
    private float damageBuff;
    
    public RachisPower(AbstractCreature owner, int amount) {
        super(POWER_ID, "powerbuff", PowerType.BUFF, owner);
        setValues(amount);
        checkDamageBuff();
        updateDescription();
        autoIndex();
    }
    
    public RachisPower(AbstractCreature owner) {
        this(owner, 1);
    }
    
    private void autoIndex() {
        this.ID = this.ID + GameActionManager.turn;
    }
    
    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        if (!owner.isDeadOrEscaped() && amount > 0) {
            damage += damage * damageBuff;
        }
        return super.atDamageFinalGive(damage, type);
    }
    
    @Override
    public void atEndOfRound() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
    
    private void checkDamageBuff() {
        damageBuff = 0.5F * amount;
    }
    
    @Override
    public String preSetDescription() {
        checkDamageBuff();
        setAmtValue(0, SciPercent(damageBuff));
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new RachisPower(owner, amount);
    }
}