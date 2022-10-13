package rs.lunarshop.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface CritModifierInterface {
    
    default float modifyCritChance(float origin) {
        return origin;
    }
    
    default float modifyCritChance(float origin, AbstractCreature hitTarget) {
        return modifyCritChance(origin);
    }
    
    default float modifyCritMult(float origin) {
        return origin;
    }
    
    default float modifyCritMult(float origin, AbstractCreature hitTarget) {
        return modifyCritMult(origin);
    }
}