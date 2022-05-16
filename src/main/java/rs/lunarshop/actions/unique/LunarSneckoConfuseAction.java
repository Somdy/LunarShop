package rs.lunarshop.actions.unique;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomGameAction;

public class LunarSneckoConfuseAction extends LMCustomGameAction {
    
    public LunarSneckoConfuseAction(int max) {
        this.amount = max;
        actionType = ActionType.CARD_MANIPULATION;
    }
    
    @Override
    public void update() {
        isDone = true;
        manipulateCards(cpr().drawPile);
        manipulateCards(cpr().discardPile);
        manipulateCards(cpr().hand);
        manipulateCards(cpr().exhaustPile);
    }
    
    private void manipulateCards(@NotNull CardGroup group) {
        for (AbstractCard card : group.group) {
            if (card.cost >= 0) {
                int cost = cardRandomRng().random(0, amount);
                if (card.cost != cost) {
                    card.cost = cost;
                    card.costForTurn = card.cost;
                    card.isCostModified = true;
                }
                card.freeToPlayOnce = false;
            }
        }
    }
}