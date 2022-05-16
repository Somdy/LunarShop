package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.interfaces.relics.BlockModifierRelic;
import rs.lunarshop.items.abstracts.LunarRelic;

public class HopooFeather extends LunarRelic implements BlockModifierRelic {
    private int least;
    
    public HopooFeather() {
        super(ItemID.HopooFeather);
        least = 4;
    }
    
    @Override
    public void refreshStats() {
        least = 2 + 2 * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], least);
    }
    
    @Override
    public float onGainBlock(AbstractCreature target, float blockAmt) {
        if (target == cpr() && blockAmt < least)
            blockAmt = least;
        return BlockModifierRelic.super.onGainBlock(target, blockAmt);
    }
}