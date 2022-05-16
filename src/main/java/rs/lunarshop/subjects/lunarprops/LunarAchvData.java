package rs.lunarshop.subjects.lunarprops;

import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.AchvTier;
import rs.lunarshop.localizations.AchvLocales;

public class LunarAchvData {
    public final int key;
    public final AchvTier tier;
    public String title;
    public String description;
    
    public LunarAchvData(int key, AchvTier tier) {
        this.key = key;
        this.tier = tier;
        init();
    }
    
    void init() {
        AchvLocales.SingleData data = LunarMod.LocalePack.getAchvLocales(key);
        title = data.getTitle();
        description = data.getDescription();
    }
}