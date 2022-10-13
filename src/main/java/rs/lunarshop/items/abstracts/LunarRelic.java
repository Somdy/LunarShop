package rs.lunarshop.items.abstracts;

import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.abstracts.AbstractLunarRelic;

public class LunarRelic extends AbstractLunarRelic {
    protected LunarRelic(int lunarID, int stack) {
        super(lunarID, LunarClass.LUNAR, stack);
        setStackable(true);
    }
    
    public LunarRelic(int lunarID) {
        this(lunarID, 1);
    }
    
}