package rs.lunarshop.items.abstracts;

import org.jetbrains.annotations.NotNull;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.subjects.lunarprops.LunarItemID;

public class PlanetRelic extends AbstractLunarRelic {
    protected PlanetRelic(@NotNull LunarItemID itemID, int stack) {
        super(itemID, LunarClass.PLANET, stack);
        setStackable(true);
    }
    
    public PlanetRelic(@NotNull LunarItemID itemID) {
        this(itemID, 1);
    }
}