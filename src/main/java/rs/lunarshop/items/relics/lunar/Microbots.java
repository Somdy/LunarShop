package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Microbots extends LunarRelic {
    private int threshold;
    private boolean negated;
    
    public Microbots() {
        super(26);
        setTurnUse(2);
        threshold = 2;
        negated = false;
    }
    
    @Override
    public void refreshStats() {
        threshold = 1 + stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], threshold);
    }
    
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (canActivate() && info.type == DamageInfo.DamageType.NORMAL && damageAmount < threshold && damageAmount > 0) {
            damageAmount = 0;
            flash();
            negated = true;
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (negated) {
            negated = false;
            deplete();
        }
    }
}