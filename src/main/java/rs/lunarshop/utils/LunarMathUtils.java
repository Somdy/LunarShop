package rs.lunarshop.utils;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;

public class LunarMathUtils {
    private static final boolean SSFMO = Loader.isModLoaded("superfastmode");
    
    public static float lerp(float from, float to, float progress) {
        if (SSFMO) 
            return from + (to - from) * progress;
        return MathUtils.lerp(from, to, progress);
    }
}