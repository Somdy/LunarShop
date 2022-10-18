package rs.lunarshop.items.relics.planet;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.abstracts.PlanetRelic;

public class Planula extends PlanetRelic {
    private int heals;
    
    public Planula() {
        super(20);
        heals = 10;
    }
    
    @Override
    public void refreshStats() {
        heals = 10 + 5 * (stack - 1);
    }
    
    @Override
    public void onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new HealAction(cpr(), cpr(), heals, 0.01F));
        }
        super.onLoseHp(damageAmount);
    }
}