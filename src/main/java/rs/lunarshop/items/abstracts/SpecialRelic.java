package rs.lunarshop.items.abstracts;

import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.subjects.AbstractLunarRelic;

public class SpecialRelic extends AbstractLunarRelic {
    protected SpecialRelic(int lunarID, int stack) {
        super(lunarID, LunarClass.SPECIAL, stack);
        setStackable(true);
    }
    
    public SpecialRelic(int lunarID) {
        this(lunarID, 1);
    }
}