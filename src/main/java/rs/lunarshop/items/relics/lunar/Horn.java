package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.subjects.AbstractLunarEquipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Horn extends LunarRelic {
    private static final float NO_RARE_CHANCE = 0.2F;
    private int cardsToPlay;
    
    public Horn() {
        super(ItemID.Horn);
        cardsToPlay = 2;
    }
    
    @Override
    public void refreshStats() {
        cardsToPlay = stack + 1;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], cardsToPlay);
    }
    
    @Override
    public void afterEqmtActivated(AbstractLunarEquipment equipment) {
        addToBot(new RelicAboveCreatureAction(cpr(), this));
        addToBot(new QuickAction(() -> {
            int rares = (int) cpr().masterDeck.group.stream()
                    .filter(c -> isCardTypeOf(c, AbstractCard.CardType.ATTACK)
                            && isCardRarityOf(c, AbstractCard.CardRarity.RARE))
                    .count();
            List<AbstractCard> tmp = new ArrayList<>(cpr().masterDeck.group);
            tmp.removeIf(c -> !isCardTypeOf(c, AbstractCard.CardType.ATTACK));
            if (rares > 0 && rares < tmp.size() && rollCloverBadLuck(NO_RARE_CHANCE))
                tmp.removeIf(c -> isCardRarityOf(c, AbstractCard.CardRarity.RARE));
            for (int i = 0; i < cardsToPlay; i++) {
                Optional<AbstractCard> opt = getRandom(tmp, cardRandomRng());
                opt.ifPresent(c -> {
                    AbstractCard copy = c.makeSameInstanceOf();
                    copy.purgeOnUse = true;
                    copy.applyPowers();
                    addToBot(new NewQueueCardAction(copy, true, true, true));
                });
            }
        }));
    }
}