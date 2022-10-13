package rs.lunarshop.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarAchievement;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;

public class AchvGrid implements LunarUtils {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("AchvGrid"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float SPACE = 200F * Settings.scale;
    private static Color headerColor;
    private static Color targetHeaderColor;
    private static List<AbstractLunarAchievement> achievements;
    private AbstractLunarAchievement hoveredAchv;
    
    public void init() {
        if (achievements == null) {
            achievements = new ArrayList<>();
            achievements.addAll(AchvManager.GetAllAchv());
        }
        achievements.forEach(AbstractLunarAchievement::refreshStatus);
        headerColor = Color.WHITE.cpy();
        targetHeaderColor = headerColor.cpy();
    }
    
    public void update() {
        updateHeaderColor();
        for (AbstractLunarAchievement a : achievements) {
            a.update();
            if (a.hb.hovered && hoveredAchv != a)
                hoveredAchv = a;
        }
    }
    
    private void updateHeaderColor() {
        if (hoveredAchv != null) {
            targetHeaderColor = hoveredAchv.data.tier.color.cpy();
        }
        headerColor.lerp(targetHeaderColor, Gdx.graphics.getDeltaTime());
    }
    
    public void renderHeaderInStats(SpriteBatch sb, float screenX, float renderY) {
        FontHelper.renderSmartText(sb, FontHelper.charTitleFont, TEXT[0], screenX + scale(50F), 
                renderY + scale(850F), 9999F, scale(32F), headerColor.cpy());
    }
    
    public void renderInStats(SpriteBatch sb, float renderY) {
        for (int i = 0; i < achievements.size(); i++) {
            AbstractLunarAchievement a = achievements.get(i);
            a.render(sb, scale(560F) + (i % 5) * SPACE, renderY - (i / 5) * SPACE + scale(680F));
        }
    }
}