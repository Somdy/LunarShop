package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.items.abstracts.LunarRelic;

public class RoseBuckler extends LunarRelic {
    private int armor;
    
    public RoseBuckler() {
        super(22);
        armor = 5;
    }
    
    @Override
    public void refreshStats() {
        armor = 5 * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], armor);
    }
    
    @Override
    public int modifyArmor(int origin) {
        return origin + armor;
    }
}