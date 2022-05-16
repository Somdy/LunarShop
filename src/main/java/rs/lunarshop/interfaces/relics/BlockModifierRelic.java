package rs.lunarshop.interfaces.relics;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface BlockModifierRelic {
    default float onGainBlock(AbstractCreature target, float blockAmt) {
        return blockAmt;
    }
    
    default float onLoseBlock(AbstractCreature target, float loseAmt) {
        return loseAmt;
    }
}