package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.interfaces.InstantOnUseCardInterface;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Afterburner extends LunarRelic implements InstantOnUseCardInterface {
    private static final float BASE_CHANCE = 0.25F;
    private static final int BASE_FUNC = 2;
    
    public Afterburner() {
        super(70);
        counter = BASE_FUNC;
    }
    
    @Override
    public void refreshStats() {
        counter = BASE_FUNC + stack - 1;
    }
    
    @Override
    public void instantOnUseCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        if (rollCloverLuck(BASE_CHANCE) && counter > 0 && card != null) {
            flash();
            counter--;
            card.use(p, m);
        }
    }
    
    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        counter = BASE_FUNC;
    }
}