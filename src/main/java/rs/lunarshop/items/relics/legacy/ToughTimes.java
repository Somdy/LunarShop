package rs.lunarshop.items.relics.legacy;

import rs.lunarshop.items.abstracts.LegacyRelic;

public class ToughTimes extends LegacyRelic {
    private static final int BASE_ARMOR = 10;
    private static final int ARMOR_PER_STACK = 5;
    private int armorMod;
    
    public ToughTimes() {
        super(84);
        armorMod = BASE_ARMOR;
    }
    
    @Override
    public void refreshStats() {
        armorMod = BASE_ARMOR + (stack - 1) * ARMOR_PER_STACK;
    }
    
    @Override
    public int modifyArmor(int origin) {
        origin += armorMod;
        return super.modifyArmor(origin);
    }
}