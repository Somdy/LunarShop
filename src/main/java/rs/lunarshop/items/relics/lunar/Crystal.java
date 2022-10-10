package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.items.abstracts.LunarRelic;

public class Crystal extends LunarRelic {
    private int mod;
    
    public Crystal() {
        super(51);
        mod = 2;
    }
    
    @Override
    public void refreshStats() {
        mod = 1 + stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], mod);
    }
    
    @Override
    public int modifyAttack(int origin) {
        origin += mod;
        return super.modifyAttack(origin);
    }
}