package rs.lunarshop.cards.lunar.scepters;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.tools.HandCardManipulator;
import rs.lazymankits.actions.utility.SimpleHandCardSelectBuilder;
import rs.lunarshop.abstracts.AbstractLunarCard;

import java.util.Optional;

public class ASUSurvivor extends AbstractLunarCard {
    public ASUSurvivor() {
        super(2, CardTarget.SELF);
        useCustomBg("vanilla_green");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        addToBot(new GainBlockAction(s, s, block));
        addToBot(new SimpleHandCardSelectBuilder(c -> true)
                .setMsg(MSG[0]).setAmount(magicNumber)
                .setCanPickZero(false).setAnyNumber(false)
                .setManipulator(new HandCardManipulator() {
                    @Override
                    public boolean manipulate(AbstractCard card, int index) {
                        attTmpAction(() -> {
                            Optional<AbstractMonster> opt = getRandom(getAllExptMstrs(m -> 
                                    !m.isDeadOrEscaped() && card.canUse(cpr(), m)), cardRandomRng());
                            opt.ifPresent(m -> card.use(cpr(), m));
                        });
                        addToTop(new DiscardSpecificCardAction(card));
                        return true;
                    }
                }));
    }
}