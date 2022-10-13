package rs.lunarshop.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarPower;
import rs.lunarshop.utils.DamageInfoTag;

public class HelfirePower extends AbstractLunarPower {
    public static final String POWER_ID = LunarMod.Prefix("HelfirePower");
    private final boolean enemy;
    
    public HelfirePower(AbstractCreature owner, int turns, boolean enemy) {
        super(POWER_ID, "helfire", PowerType.DEBUFF, owner);
        setValues(turns);
        this.enemy = enemy;
        checkDamage();
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        if (amount > 0 && !owner.isDeadOrEscaped()) {
            checkDamage();
            addToBot(damage(owner, null, extraAmt, DamageInfo.DamageType.HP_LOSS, 
                    AbstractGameAction.AttackEffect.FIRE, DamageInfoTag.FIRE));
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }
    
    private void checkDamage() {
        int damage;
        if (enemy) {
            damage = MathUtils.round(owner.maxHealth * 0.1F);
        } else {
            damage = MathUtils.round(owner.maxHealth * 0.025F);
        }
        extraAmt = damage;
    }
    
    @Override
    public String preSetDescription() {
        checkDamage();
        setAmtValue(0, extraAmt);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new HelfirePower(owner, amount, enemy);
    }
}