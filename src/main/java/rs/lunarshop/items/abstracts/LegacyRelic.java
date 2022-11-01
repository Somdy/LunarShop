package rs.lunarshop.items.abstracts;

import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.enums.LunarClass;

public class LegacyRelic extends AbstractLunarRelic {
    protected LegacyRelic(int lunarID, int stack) {
        super(lunarID, LunarClass.LEGACY, stack);
    }
    
    public LegacyRelic(int lunarID) {
        super(lunarID, LunarClass.LEGACY, 1);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return false;
    }
}