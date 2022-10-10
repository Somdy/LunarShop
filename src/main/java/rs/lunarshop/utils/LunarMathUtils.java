package rs.lunarshop.utils;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.helpers.Hitbox;
import org.jetbrains.annotations.NotNull;

public class LunarMathUtils {
    private static final boolean SSFMO = Loader.isModLoaded("superfastmode");
    
    public static float lerp(float from, float to, float progress) {
        float result = SSFMO ? from + (to - from) * progress : MathUtils.lerp(from, to, progress);
        if (Math.abs(from - to) <= 0.01F)
            result = to;
        return result;
    }
    
    public static void lerpHitbox(@NotNull Hitbox from, @NotNull Hitbox to, float progress) {
        if (from.x != to.x) {
            from.x = lerp(from.x, to.x, progress);
        }
        if (from.y != to.y) {
            from.y = lerp(from.y, to.y, progress);
        }
        if (from.width != to.width) {
            from.width = lerp(from.width, to.width, progress);
        }
        if (from.height != to.height) {
            from.height = lerp(from.height, to.height, progress);
        }
        from.cX = from.x + from.width / 2F;
        from.cY = from.y + from.height / 2F;
    }
}