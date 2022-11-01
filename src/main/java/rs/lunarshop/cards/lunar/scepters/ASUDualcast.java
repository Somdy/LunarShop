package rs.lunarshop.cards.lunar.scepters;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.abstracts.AbstractLunarCard;

public class ASUDualcast extends AbstractLunarCard {
    public ASUDualcast() {
        super(5, CardTarget.NONE);
        useCustomBg("vanilla_blue");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new AnimateOrbAction(1));
        addToBot(new EvokeWithoutRemovingOrbAction(1));
        addToBot(new AnimateOrbAction(magicNumber - 1));
        addToBot(new EvokeOrbAction(magicNumber - 1));
    }
    
    @Override
    protected void selfUpgrade() {
        shuffleBackIntoDrawPile = true;
    }
}