package rs.lunarshop.localizations;

import com.badlogic.gdx.graphics.Color;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.utils.LunarTip;
import rs.lunarshop.utils.LunarUtils;

public class LunarTipLocals implements LunarUtils {
    public String ID;
    public LunarLocalTip[] TIPS;
    
    public LunarTip getTip(int slot, Color hColor, Color bColor) {
        if (slot > TIPS.length - 1) {
            warn("NO TIP AT INDEX OF [" + slot + "]");
            slot = TIPS.length - 1;
        }
        LunarLocalTip tip = TIPS[slot];
        return new LunarTip(tip.title, tip.body, hColor, bColor);
    }
    
    public LunarTip getTip(int slot) {
        if (slot > TIPS.length - 1) {
            warn("NO TIP AT INDEX OF [" + slot + "]");
            slot = TIPS.length - 1;
        }
        LunarLocalTip tip = TIPS[slot];
        return new LunarTip(tip.title, tip.body);
    }
    
    @NotNull
    public static LunarTipLocals MockingTip() {
        LunarTipLocals locals = new LunarTipLocals();
        locals.ID = "MISSING";
        LunarLocalTip localTip = new LunarLocalTip();
        localTip.title = "MISSING_TITLE";
        localTip.body = "MISSING_DESCRIPTION";
        locals.TIPS = new LunarLocalTip[]{localTip};
        return locals;
    }
    
    protected static class LunarLocalTip {
        protected String title;
        protected String body;
    }
}