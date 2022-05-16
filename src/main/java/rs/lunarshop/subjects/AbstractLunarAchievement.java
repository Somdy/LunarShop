package rs.lunarshop.subjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.AchvTier;
import rs.lunarshop.subjects.lunarprops.LunarAchvData;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

public class AbstractLunarAchievement implements LunarUtils {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("LunarAchievement"));
    public static final String[] TEXT = uiStrings.TEXT;
    public final LunarAchvData data;
    public String title;
    public String description;
    private final TextureAtlas.AtlasRegion portrait;
    private TextureAtlas.AtlasRegion bg;
    private TextureAtlas.AtlasRegion frame;
    public Hitbox hb;
    public boolean unlocked;
    private float scale;
    
    public AbstractLunarAchievement(@NotNull LunarAchvData data) {
        this.data = data;
        this.title = data.title;
        this.description = data.description;
        hb = new Hitbox(scale(160F), scale(160F));
        portrait = LunarImageMst.FindAchvItem(data.key);
        scale = Settings.scale;
        checkUnlocked();
        loadAssets();
    }
    
    void checkUnlocked() {
        unlocked = AchvHelper.IsAchvUnlocked(data.key);
        if (!unlocked) {
            description = TEXT[1];
            if (data.tier == AchvTier.LEGEND) title = TEXT[0];
        }
    }
    
    void loadAssets() {
        switch (data.tier) {
            case RARE:
                bg = LunarImageMst.BG_RARE;
                frame = LunarImageMst.F_RARE;
                break;
            case LEGEND:
                bg = LunarImageMst.BG_LEGEND;
                frame = LunarImageMst.F_LEGEND;
                break;
            default:
                bg = LunarImageMst.BG_COMMON;
                frame = LunarImageMst.F_COMMON;
        }
    }
    
    public void refreshStatus() {
        checkUnlocked();
    }
    
    public void update() {
        if (hb != null) {
            hb.update();
            scale = hb.hovered ? scale(1.1F) : scale(1F);
            if (hb.hovered)
                TipHelper.renderGenericTip(InputHelper.mX + scale(100F), InputHelper.mY, title, description);
        }
    }
    
    public void render(SpriteBatch sb, float x, float y) {
        boolean useShader = false;
        if (!unlocked) {
            ShaderHelper.setShader(sb, ShaderHelper.Shader.GRAYSCALE);
            useShader = true;
        }
        sb.setColor(Color.WHITE.cpy());
        sb.draw(bg, x - bg.packedWidth / 2F, y - bg.packedHeight / 2F, bg.packedWidth / 2F, bg.packedHeight / 2F, 
                bg.packedWidth, bg.packedHeight, scale, scale, 0F);
        sb.draw(portrait, x - portrait.packedWidth / 2F, y - portrait.packedHeight / 2F, portrait.packedWidth / 2F, 
                portrait.packedHeight / 2F, portrait.packedWidth, portrait.packedHeight, 
                scale, scale, 0F);
        sb.draw(frame, x - frame.packedWidth / 2F, y - frame.packedHeight / 2F, frame.packedWidth / 2F, 
                frame.packedHeight / 2F, frame.packedWidth, frame.packedHeight, scale, scale, 0F);
        hb.move(x, y);
        hb.render(sb);
        if (useShader) {
            sb.end();
            sb.setShader(null);
            sb.begin();
        }
    }
    
    public void renderAtBroadcast(@NotNull SpriteBatch sb, float x, float y, float scale, Color color) {
        sb.setColor(color);
        sb.draw(bg, x - bg.packedWidth / 2F, y - bg.packedHeight / 2F, bg.packedWidth / 2F, bg.packedHeight / 2F,
                bg.packedWidth, bg.packedHeight, scale, scale, 0F);
        sb.draw(portrait, x - portrait.packedWidth / 2F, y - portrait.packedHeight / 2F, portrait.packedWidth / 2F,
                portrait.packedHeight / 2F, portrait.packedWidth, portrait.packedHeight,
                scale, scale, 0F);
        sb.draw(frame, x - frame.packedWidth / 2F, y - frame.packedHeight / 2F, frame.packedWidth / 2F,
                frame.packedHeight / 2F, frame.packedWidth, frame.packedHeight, scale, scale, 0F);
        hb.move(x, y);
        hb.render(sb);
    }
    
    public AbstractLunarAchievement copy() {
        return new AbstractLunarAchievement(data);
    }
}