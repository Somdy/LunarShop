package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lunarshop.items.abstracts.LunarRelic;

public class TougherTimes extends LunarRelic {
    private float chance;
    
    public TougherTimes() {
        super(34);
        chance = 0.13F;
    }
    
    @Override
    public void refreshStats() {
        chance = 1 - 1 / (1 + 0.15F * stack);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1]);
    }
    
    @Override
    public int onLoseHpLast(int damageAmount) {
        if (rollStaticLuck(chance)) {
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            damageAmount = 0;
        }
        return super.onLoseHpLast(damageAmount);
    }
}