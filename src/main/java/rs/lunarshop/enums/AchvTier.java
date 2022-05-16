package rs.lunarshop.enums;

import com.badlogic.gdx.graphics.Color;
import rs.lazymankits.utils.LMSK;

public enum AchvTier {
    COMMON(0, LMSK.Color(80, 247, 63)), 
    RARE(1, LMSK.Color(70, 176, 244)), 
    LEGEND(2, LMSK.Color(245, 104, 63));
    
    public final int tier;
    public final Color color;
    
    AchvTier(int tier, Color color) {
        this.tier = tier;
        this.color = color;
    }
}