package rs.lunarshop.items.relics.legacy;

import rs.lunarshop.items.abstracts.LegacyRelic;

public class Vial extends LegacyRelic {
    private static final int BASE_REGEN = 2;
    private int regenMod;
    
    public Vial() {
        super(89);
        regenMod = BASE_REGEN;
    }
    
    @Override
    public void refreshStats() {
        regenMod = BASE_REGEN + (stack - 1);
    }
    
    @Override
    public int modifyRegen(int origin) {
        origin += regenMod;
        return super.modifyRegen(origin);
    }
}