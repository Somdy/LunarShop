package rs.lunarshop.cards.lunar.scepters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.abstracts.AbstractLunarCard;

public class ASUEruption extends AbstractLunarCard {
    public ASUEruption() {
        super(6, CardTarget.ENEMY);
        useCustomBg("vanilla_purple");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new ChangeStanceAction("Wrath"));
        atbTmpAction(() -> {
            applyPowers();
            if (t instanceof AbstractMonster)
                calculateCardDamage((AbstractMonster) t);
            addToTop(DamageAction(t, s, damage, AbstractGameAction.AttackEffect.FIRE));
        });
    }
}