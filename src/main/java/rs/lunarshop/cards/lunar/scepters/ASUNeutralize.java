package rs.lunarshop.cards.lunar.scepters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.lunarshop.abstracts.AbstractLunarCard;

public class ASUNeutralize extends AbstractLunarCard {
    public ASUNeutralize() {
        super(3, CardTarget.ENEMY);
        useCustomBg("vanilla_green");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, damage, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new ApplyPowerAction(t, s, new WeakPower(t, magicNumber, false)));
        addToBot(new ApplyPowerAction(t, s, new StrengthPower(t, -getEM())));
    }
}