package rs.lunarshop.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface InstantOnUseCardInterface {
    void instantOnUseCard(AbstractCard card, AbstractPlayer p, AbstractMonster m);
}