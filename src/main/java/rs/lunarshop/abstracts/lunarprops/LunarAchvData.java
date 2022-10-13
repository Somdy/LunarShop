package rs.lunarshop.abstracts.lunarprops;

import rs.lunarshop.enums.AchvTier;
import rs.lunarshop.localizations.AchvLocals;
import rs.lunarshop.localizations.LunarLocalLoader;

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
        AchvLocals.SingleData data = LunarLocalLoader.GetAchvLocales(key);
        title = data.getTitle();
        description = data.getDescription();
    }
}