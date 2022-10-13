package rs.lunarshop.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.equipments.FakeWine;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.utils.ItemHelper;

public final class TonicPower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("TonicPower");
    
    public TonicPower(AbstractCreature owner, int turns) {
        super(POWER_ID, "tonicbuff", PowerType.BUFF, owner);
        setValues(turns);
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (amount > 0 && !owner.isDeadOrEscaped()) {
            int heal = MathUtils.ceil((owner.maxHealth - owner.currentHealth) * 0.2F);
            addToBot(new HealAction(owner, owner, heal));
            addToBot(new GainBlockAction(owner, owner, 35));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        damage = damage * 1.75F;
        return super.atDamageFinalGive(damage, type);
    }
    
    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        addToTop(new HealAction(cpr(), cpr(), MathUtils.ceil(cpr().currentHealth * 0.2F)));
    }
    
    @Override
    public void onRemove() {
        super.onRemove();
//        addToBot(new ApplyPowerAction(owner, owner, new TonicDebuffPower(owner)));
        if (ItemHelper.RollCloverBadLuck(ItemHelper.GetProp(15), 0.2F)) {
            FakeWine.IncreaseLoss();
        }
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new TonicPower(owner, amount);
    }
}