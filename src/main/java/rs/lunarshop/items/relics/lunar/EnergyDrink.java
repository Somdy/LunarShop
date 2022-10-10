package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lunarshop.items.abstracts.LunarRelic;

public class EnergyDrink extends LunarRelic {
    private static final int BASE_BLOCK = 2;
    private int block;
    
    public EnergyDrink() {
        super(67);
        block = BASE_BLOCK;
        counter = 0;
    }
    
    @Override
    public void refreshStats() {
        block = BASE_BLOCK * stack;
    }
    
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (isCardTypeOf(targetCard, AbstractCard.CardType.SKILL)) {
            counter++;
        }
    }
    
    @Override
    public void onPlayerEndTurn() {
        if (counter >= 3) {
            int blockAmt = block * (counter / 3);
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new GainBlockAction(cpr(), blockAmt));
        }
        counter = 0;
    }
}