package rs.lunarshop.items.abstracts;

import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.abstracts.AbstractLunarRelic;

public class PlanetRelic extends AbstractLunarRelic {
    protected PlanetRelic(int lunarID, int stack) {
        super(lunarID, LunarClass.PLANET, stack);
        setStackable(true);
    }
    
    public PlanetRelic(int lunarID) {
        this(lunarID, 1);
    }
}