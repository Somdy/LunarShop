package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Fungus extends LunarRelic {
    private static final int BASE_REGEN_AMT = 2;
    private int regen;
    private boolean attackedOnce;
    private boolean canTrigger;
    
    public Fungus() {
        super(66);
        regen = BASE_REGEN_AMT;
        attackedOnce = false;
        canTrigger = false;
    }
    
    @Override
    public void refreshStats() {
        regen = BASE_REGEN_AMT + stack - 1;
    }
    
    @Override
    public void atTurnStart() {
        attackedOnce = false;
    }
    
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (isCardTypeOf(targetCard, AbstractCard.CardType.ATTACK))
            attackedOnce = true;
    }
    
    @Override
    public void onPlayerEndTurn() {
        canTrigger = !attackedOnce;
    }
    
    @Override
    public int modifyRegen(int origin) {
        if (canTrigger) {
            origin += regen;
        }
        return super.modifyRegen(origin);
    }
}
