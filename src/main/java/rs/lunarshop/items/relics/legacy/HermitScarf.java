package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lunarshop.items.abstracts.LegacyRelic;

public class HermitScarf extends LegacyRelic {
    private static final float BASE_DODGE_CHANCE = 0.1F;
    private static final float CHANCE_PER_STACK = 0.05F;
    private static final float DODGE_CHANCE_LIMIT = 0.35F;
    private float chance;
    
    public HermitScarf() {
        super(85);
        chance = BASE_DODGE_CHANCE;
    }
    
    @Override
    public void refreshStats() {
        chance = BASE_DODGE_CHANCE + (stack - 1) * CHANCE_PER_STACK;
        if (chance > DODGE_CHANCE_LIMIT)
            chance = DODGE_CHANCE_LIMIT;
    }
    
    @Override
    public int onLoseHpLast(int damageAmount) {
        if (rollCloverLuck(chance)) {
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            damageAmount = 0;
        }
        return super.onLoseHpLast(damageAmount);
    }
}