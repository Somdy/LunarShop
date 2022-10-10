package rs.lunarshop.items.relics.special;

import rs.lunarshop.items.abstracts.SpecialRelic;

public class DioConsumed extends SpecialRelic {
    public DioConsumed() {
        super(57);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], stack);
    }
}