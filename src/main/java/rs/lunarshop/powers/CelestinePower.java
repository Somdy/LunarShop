package rs.lunarshop.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.interfaces.powers.CardProofPower;
import rs.lunarshop.actions.common.ApplySlowdownAction;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarPower;

public class CelestinePower extends AbstractLunarPower implements CardProofPower {
    public static final String POWER_ID = LunarMod.Prefix("CelestinePower");
    
    public CelestinePower(AbstractCreature owner, int amount) {
        super(POWER_ID, "celestine", PowerType.BUFF, owner);
        setValues(amount);
        updateDescription();
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == owner && damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && target != owner) {
            addToBot(new ApplySlowdownAction(target, owner, 0.6F, amount));
        }
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public String preSetDescription() {
        setAmtValue(0, amount);
        return DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new CelestinePower(owner, amount);
    }
    
    @Override
    public boolean canPlayerUseCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        return m != owner;
    }
}