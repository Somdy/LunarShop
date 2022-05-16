package rs.lunarshop.interfaces.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface FreePlayRelic {
    boolean canFreeToPlay(AbstractCard card);
}