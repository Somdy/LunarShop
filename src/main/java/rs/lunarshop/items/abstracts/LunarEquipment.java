package rs.lunarshop.items.abstracts;

import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.abstracts.AbstractLunarEquipment;

public class LunarEquipment extends AbstractLunarEquipment {
    public LunarEquipment(int lunarID, int cooldown) {
        super(lunarID, LunarClass.LUNAR, 0, cooldown);
    }
}