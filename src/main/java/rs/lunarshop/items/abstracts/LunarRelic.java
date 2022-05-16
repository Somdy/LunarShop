package rs.lunarshop.items.abstracts;

import rs.lunarshop.subjects.lunarprops.LunarItemID;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.subjects.AbstractLunarRelic;

public class LunarRelic extends AbstractLunarRelic {
    
    protected LunarRelic(LunarItemID itemID, int stack) {
        super(itemID, LunarClass.LUNAR, stack);
        setStackable(true);
    }
    
    public LunarRelic(LunarItemID itemID) {
        this(itemID, 1);
    }
    
}