package rs.lunarshop.interfaces;

import rs.lunarshop.enums.EssCallerType;
import rs.lunarshop.subjects.AbstractCommandEssence;

public interface EssenceCaller {
    void onEssenceUsedUp(AbstractCommandEssence essence);
    
    EssCallerType getType();
}