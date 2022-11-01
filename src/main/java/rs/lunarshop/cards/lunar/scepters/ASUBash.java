package rs.lunarshop.cards.lunar.scepters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import rs.lunarshop.abstracts.AbstractLunarCard;
import rs.lunarshop.patches.mechanics.DamageInfoField;

public class ASUBash extends AbstractLunarCard {
    public ASUBash() {
        super(1, CardTarget.ENEMY);
        useCustomBg("vanilla_red");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(DamageAction(t, s, damage, AbstractGameAction.AttackEffect.BLUNT_HEAVY, 
                i -> DamageInfoField.armorEfficiency.set(i, 0.5F)));
        addToBot(new ApplyPowerAction(t, s, new VulnerablePower(t, magicNumber, false)));
    }
}