package rs.lunarshop.cards.lunar.scepters;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.CalmStance;
import rs.lunarshop.abstracts.AbstractLunarCard;

public class ASUVigilance extends AbstractLunarCard {
    public ASUVigilance() {
        super(7, CardTarget.SELF);
        useCustomBg("vanilla_purple");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        addToBot(new ChangeStanceAction("Calm"));
        if (upgraded && cpr().stance.ID.equals(CalmStance.STANCE_ID)) {
            addToBot(new GainBlockAction(s, s, getEB()));
        }
    }
}