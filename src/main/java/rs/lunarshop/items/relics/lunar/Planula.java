package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Planula extends LunarRelic {
    private int heals;
    
    public Planula() {
        super(ItemID.Planula);
        heals = 10;
    }
    
    @Override
    public void refreshStats() {
        heals = 10 + 5 * (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], heals);
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