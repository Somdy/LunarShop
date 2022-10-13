package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.items.abstracts.LunarRelic;

public class Crystal extends LunarRelic {
    private int mod;
    
    public Crystal() {
        super(51);
        mod = 2;
        presetInfo(s -> createInfo(s, mod));
    }
    
    @Override
    public void refreshStats() {
        mod = 1 + stack;
    }
    
    @Override
    public int modifyAttack(int origin) {
        origin += mod;
        return super.modifyAttack(origin);
    }
}