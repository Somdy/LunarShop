package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lunarshop.items.abstracts.LegacyRelic;

public class Shopper extends LegacyRelic {
    private static final float BASE_GOLD_MULT = 1.25F;
    private static final float MULT_PER_STACK = 0.15F;
    private float goldMult;
    
    public Shopper() {
        super(80);
        goldMult = BASE_GOLD_MULT;
        presetInfo(s -> createInfo(s, SciPercent(goldMult)));
    }
    
    @Override
    public void refreshStats() {
        goldMult = BASE_GOLD_MULT + (stack - 1) * MULT_PER_STACK;
    }
    
    @Override
    public float modifyGoldReward(float goldAmt, AbstractRoom room) {
        goldAmt = goldAmt * goldMult;
        return super.modifyGoldReward(goldAmt, room);
    }
}