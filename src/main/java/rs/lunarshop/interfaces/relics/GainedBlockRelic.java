package rs.lunarshop.interfaces.relics;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface GainedBlockRelic {
    /**
     * triggers after the target creature gained block, {@code currentBlock} has been increased
     * @param blockAmt the origin block amount
     * @param modifiedBlockAmt the final block amount, may be modified
     * @param newBlock true if the creature had no block before
     */
    void onBlockGained(AbstractCreature who, int blockAmt, float modifiedBlockAmt, boolean newBlock);
}