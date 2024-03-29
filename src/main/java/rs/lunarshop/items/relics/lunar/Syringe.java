package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Syringe extends LunarRelic {
    private AbstractCard.CardType lastType;
    private int mod;
    private boolean trigger;
    
    public Syringe() {
        super(52);
        lastType = null;
        mod = 3;
        trigger = false;
        presetInfo(s -> createInfo(s, mod));
    }
    
    @Override
    public void refreshStats() {
        mod = 3 * stack;
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        lastType = null;
        trigger = false;
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (lastType == AbstractCard.CardType.ATTACK) {
            trigger = isCardTypeOf(card, lastType);
        }
        lastType = card.type;
    }
    
    @Override
    public int modifyAttack(int origin) {
        if (trigger) origin += mod;
        return super.modifyAttack(origin);
    }
}