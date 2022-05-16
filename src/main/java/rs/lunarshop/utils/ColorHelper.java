package rs.lunarshop.utils;

import com.badlogic.gdx.graphics.Color;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.enums.LunarRarity;

public final class ColorHelper {
    public static final Color White = Color.WHITE.cpy();
    public static final Color Lunar = create(0, 114, 231);
    public static final Color Planet = Color.YELLOW.cpy();
    
    private static final Color Common = Color.WHITE.cpy();
    private static final Color Uncommon = Color.GREEN.cpy();
    private static final Color Rare = create(255, 69, 0);
    private static final Color Legend = create(238, 0, 0);
    private static final Color Mythic = create(139, 0, 139);
    private static final Color Unreal = create(245, 255, 240, 0.5F);
    
    private static Color create(int r, int g, int b) {
        return new Color(r / 255F, g / 255F, b / 255F, 1);
    }
    
    private static Color create(int r, int g, int b, float a) {
        return new Color(r / 255F, g / 255F, b / 255F, a);
    }
    
    public static Color GetRarityColor(@NotNull LunarRarity rarity) {
        switch (rarity) {
            case COMMON:
                return Common;
            case UNCOMMON:
                return Uncommon;
            case RARE:
                return Rare;
            case LEGEND:
                return Legend;
            case MYTHIC:
                return Mythic;
            default:
                return Unreal;
        }
    }
}