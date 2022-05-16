package rs.lunarshop.items.abstracts;

import rs.lunarshop.subjects.lunarprops.LunarItemID;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.subjects.AbstractLunarEquipment;

public class LunarEquipment extends AbstractLunarEquipment {
    
    public LunarEquipment(LunarItemID itemID, int cooldown) {
        super(itemID, LunarClass.LUNAR, 0, cooldown);
    }
    
}