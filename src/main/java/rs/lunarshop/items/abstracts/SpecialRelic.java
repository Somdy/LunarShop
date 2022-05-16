package rs.lunarshop.items.abstracts;

import org.jetbrains.annotations.NotNull;
import rs.lunarshop.subjects.lunarprops.LunarItemID;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.subjects.AbstractLunarRelic;

public class SpecialRelic extends AbstractLunarRelic {
    
    protected SpecialRelic(@NotNull LunarItemID itemID, int stack) {
        super(itemID, LunarClass.SPECIAL, stack);
        setStackable(true);
    }
    
    public SpecialRelic(@NotNull LunarItemID itemID) {
        this(itemID, 1);
    }
}