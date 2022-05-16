package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.interfaces.relics.FreePlayRelic;
import rs.lunarshop.items.abstracts.LunarRelic;

public class AlienHead extends LunarRelic implements FreePlayRelic {
    private int cardsNeed;
    private boolean active;
    
    public AlienHead() {
        super(ItemID.AlienHead);
        cardsNeed = 6;
        counter = cardsNeed;
        active = false;
    }
    
    @Override
    public void refreshStats() {
        cardsNeed = 7 - stack;
        if (cardsNeed <= 0) cardsNeed = 1;
    }
    
    @Override
    protected void onStackAmt(int amt, boolean stacking) {
        if (stacking) {
            counter -= amt;
            if (counter <= 0) {
                counter = 0;
                active = true;
            }
        }
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], cardsNeed);
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.SKILL) && counter > 0 && !active) {
            counter--;
            if (counter <= 0) {
                beginLongPulse();
                counter = 0;
                active = true;
            }
        } else if (active && counter <= 0) {
            counter = cardsNeed;
            active = false;
            stopPulse();
        }
    }
    
    @Override
    public boolean canFreeToPlay(AbstractCard card) {
        return active;
    }
}