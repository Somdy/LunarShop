package rs.lunarshop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import rs.lunarshop.core.LunarMod;

import java.util.HashMap;
import java.util.Map;

public class LunarFont {
    private static FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private static Map<String, FreeTypeFontGenerator> fontGeneratorMap = new HashMap<>();
    private static FileHandle fontFile;
    private static float fontScale = 1F;
    
    public static BitmapFont ROR_TIP_HEADER_FONT;
    public static BitmapFont ROR_TIP_BODY_FONT;
    public static BitmapFont ROR_RELIC_TITLE_FONT;
    public static BitmapFont ROR_RELIC_SUBTL_FONT;
    public static BitmapFont ROR_RELIC_TEXT_FONT;
    
    public static void Initialize() {
        long time = System.currentTimeMillis();
        fontGeneratorMap.clear();
        switch (Settings.language) {
            case ZHS:
            case ZHT:
                fontFile = Gdx.files.internal("LunarAssets/fonts/CN_NotoSansCJKsc-Regular.otf");
                break;
            case ENG:
                fontFile = Gdx.files.internal("LunarAssets/fonts/EN_BOMBARD_.ttf");
                break;
            default:
                fontFile = null;
        }
        param.gamma = 2.0F;
        param.borderGamma = 2.0F;
        param.borderStraight = true;
        param.borderColor = Color.WHITE;
        param.borderWidth = 0.5F;
//        param.shadowColor = Color.DARK_GRAY;
//        param.shadowOffsetX = 1;
//        param.shadowOffsetY = 1;
        param.spaceX = 1;
        ROR_TIP_HEADER_FONT = prepFont(24F, true);
        ROR_TIP_BODY_FONT = prepFont(22F, true);
        param.spaceX = 5;
        ROR_RELIC_TITLE_FONT = prepFont(48, true);
        ROR_RELIC_SUBTL_FONT = prepFont(36, true);
        param.spaceX = 1;
        ROR_RELIC_TEXT_FONT = prepFont(26, true);
        
        LunarMod.LogInfo("Lunar fonts initialized: " + (System.currentTimeMillis() - time) + " ms");
    }
    
    public static BitmapFont prepFont(float size, boolean isLinearFiltering) {
        FreeTypeFontGenerator g;
        if (fontGeneratorMap.containsKey(fontFile.path())) {
            g = fontGeneratorMap.get(fontFile.path());
        } else {
            g = new FreeTypeFontGenerator(fontFile);
            fontGeneratorMap.put(fontFile.path(), g);
        }
        if (Settings.BIG_TEXT_MODE)
            size *= 1.2F;
        return prepFont(g, size, isLinearFiltering);
    }
    
    private static BitmapFont prepFont(FreeTypeFontGenerator g, float size, boolean isLinearFiltering) {
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.size = Math.round(size * fontScale * Settings.scale);
        p.gamma = param.gamma;
        p.spaceX = param.spaceX;
        p.spaceY = param.spaceY;
        p.borderColor = param.borderColor;
        p.borderStraight = param.borderStraight;
        p.borderWidth = param.borderWidth;
        p.borderGamma = param.borderGamma;
        p.shadowColor = param.shadowColor;
        p.shadowOffsetX = param.shadowOffsetX;
        p.shadowOffsetY = param.shadowOffsetY;
        if (isLinearFiltering) {
            p.minFilter = Texture.TextureFilter.Linear;
            p.magFilter = Texture.TextureFilter.Linear;
        } else {
            p.minFilter = Texture.TextureFilter.Nearest;
            p.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        }
        g.scaleForPixelHeight(p.size);
        BitmapFont font = g.generateFont(p);
        font.setUseIntegerPositions(!isLinearFiltering);
        (font.getData()).markupEnabled = true;
        if (LocalizedStrings.break_chars != null)
            (font.getData()).breakChars = LocalizedStrings.break_chars.toCharArray();
        (font.getData()).fontFile = fontFile;
        return font;
    }
}
