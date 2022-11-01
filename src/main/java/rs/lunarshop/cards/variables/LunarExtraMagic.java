package rs.lunarshop.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lunarshop.abstracts.AbstractLunarCard;
import rs.lunarshop.core.LunarMod;

public class LunarExtraMagic extends DynamicVariable {
    public static final String KEY = LunarMod.Prefix("EM");
    
    @Override
    public String key() {
        return KEY;
    }
    
    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractLunarCard)
            return ((AbstractLunarCard) card).isEMModified();
        return false;
    }
    
    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractLunarCard)
            return ((AbstractLunarCard) card).getEM();
        return 0;
    }
    
    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractLunarCard)
            return ((AbstractLunarCard) card).getBaseEM();
        return 0;
    }
    
    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractLunarCard)
            return ((AbstractLunarCard) card).isEMUpgraded();
        return false;
    }
}