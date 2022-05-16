package rs.lunarshop.actions.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.UIStrings;
import rs.lazymankits.abstracts.LMCustomGameAction;
import rs.lazymankits.listeners.UseCardListener;
import rs.lunarshop.core.LunarMod;

public class BanCardAction extends LMCustomGameAction {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("BanCardAction"));
    public static final String[] TEXT = uiStrings.TEXT;
    private final int turns;
    private final boolean fromMonster;
    private final AbstractCard card;
    private final String msg;
    
    public BanCardAction(int turns, boolean fromMonster, AbstractCard card, String msg) {
        this.turns = turns;
        this.fromMonster = fromMonster;
        this.card = card;
        this.msg = msg == null ? TEXT[0] : msg;
        actionType = ActionType.CARD_MANIPULATION;
    }
    
    public BanCardAction(int turns, AbstractCard card, String msg) {
        this(turns, true, card, msg);
    }
    
    @Override
    public void update() {
        isDone = true;
        int banTurns = turns;
        if (fromMonster) banTurns++;
        if (card != null) {
            UseCardListener.AddCustomUnplayableCard(card, banTurns, (c, p, m) -> {
                c.cantUseMessage = msg;
                return false;
            }, true);
        }
    }
}