package rs.lunarshop.interfaces.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface GetPurgeableCardRelic {
    default boolean canPurgeCard(AbstractCard card) {
        return true;
    }
}