package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lunarshop.items.abstracts.LegacyRelic;

public class LifeSavings extends LegacyRelic {
    private static final int BASE_GOLD_GAIN = 10;
    private static final int GAIN_PER_STACK = 5;
    private int goldGain;
    
    public LifeSavings() {
        super(81);
        goldGain = BASE_GOLD_GAIN;
    }
    
    @Override
    public void refreshStats() {
        goldGain = BASE_GOLD_GAIN + (stack - 1) * GAIN_PER_STACK;
    }
    
    @Override
    public void onPlayerEndTurn() {
        addToBot(new RelicAboveCreatureAction(cpr(), this));
        addToBot(new GainGoldAction(goldGain));
    }
}