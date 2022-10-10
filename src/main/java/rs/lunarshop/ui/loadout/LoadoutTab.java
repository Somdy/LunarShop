package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.Prefs;
import rs.lunarshop.config.LoadoutConfig;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarMathUtils;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadoutTab implements LunarUtils {
    public static final int GENERAL_TAB_W = 290;
    public static final int GENERAL_TAB_H = 49;
    public static final float GAP_DIST = 5 * Settings.scale;
    private static final float LEAST_BUFFER_TIME = 0.5F;
    protected static final Texture FRAME_TOP = LunarImageMst.InnerFrameTop;
    protected static final Texture FRAME_MID = LunarImageMst.InnerFrameMid;
    protected static final Texture FRAME_BOT = LunarImageMst.InnerFrameBot;
    public final String localname;
    public final LoadoutManager manager;
    protected List<LoadoutOption> options = new ArrayList<>();
    protected List<String> selectedValues = new ArrayList<>();
    protected int value;
    protected Texture image;
    protected Hitbox hb;
    protected Hitbox tabArea;
    protected Hitbox targetArea;
    protected boolean inspectingTab;
    protected boolean hovered;
    protected int imageW;
    protected int imageH;
    protected float scale;
    protected float targetScale;
    protected float animSpeed;
    protected float bufferTime;
    protected float frameTopRenderY;
    protected float frameMidRenderY;
    protected float frameBotRenderY;
    protected float frameMidRenderHeight;
    protected Vector2 pos;
    protected Vector2 dest;
    
    public LoadoutTab(LoadoutManager manager, String localname, Texture image, int imageW, int imageH, float width, float height, float scale) {
        this.localname = localname;
        this.manager = manager;
        this.image = image;
        this.imageW = imageW;
        this.imageH = imageH;
        hb = new Hitbox(scale(width), scale(height) + GAP_DIST);
        pos = new Vector2(hb.x, hb.y);
        dest = new Vector2(hb.x, hb.y);
        this.scale = this.targetScale = scale;
    }
    
    protected void init() {}
    
    protected int getLoadoutValue() {
        return value;
    }
    
    protected List<String> getLoadoutValues() {
        return new ArrayList<>(selectedValues);
    }
    
    public void setLocation(float cX, float cY) {
        hb.move(cX, cY);
        pos.set(hb.x, hb.y);
        dest.set(hb.x, hb.y);
    }
    
    public void translate(float tX, float tY) {
        dest = new Vector2(tX, tY);
        this.animSpeed = LoadoutManager.TAB_ANIM_SPEED;
    }
    
    public void update() {
        updateAnimation();
        hb.translate(pos.x, pos.y);
        hb.update();
        hovered = hb.hovered;
        if (hb.justHovered) {
            bufferTime = 0;
            playSound("UI_HOVER");
        }
        updateBufferTime();
        if (tabArea != null) {
            tabArea.update();
            boolean hoveringArea = tabArea.hovered || hovered;
            if (hoveringArea)
                inspectingTab = true;
            if (!hoveringArea && inspectingTab && bufferTime < 0)
                inspectingTab = false;
            updateInnerFrameRenderArea();
        }
        options.forEach(LoadoutOption::update);
        for (LoadoutOption opt : options) {
            if (opt.hb.y >= hb.y)
                opt.isHidden = true;
        }
        if (inspectingTab || hovered) {
            updateOnHovering();
        } else {
            updateOnUnhovering();
        }
    }
    
    private void updateInnerFrameRenderArea() {
        if (tabArea.height > 0) {
            frameBotRenderY = tabArea.y - scale(LunarImageMst.INNER_FRAME_H / 2F);
            frameMidRenderY = frameBotRenderY + scale(LunarImageMst.INNER_FRAME_H);
            float topLine = hb.y - scale(GAP_DIST);
            frameMidRenderHeight = topLine - (frameMidRenderY + LunarImageMst.INNER_FRAME_H);
            frameTopRenderY = frameMidRenderY + frameMidRenderHeight;
        }
    }
    
    private void updateBufferTime() {
        if (bufferTime >= 0) {
            bufferTime += Gdx.graphics.getDeltaTime();
            if (bufferTime >= LEAST_BUFFER_TIME)
                bufferTime = -1;
        }
    }
    
    private void updateAnimation() {
        if (pos.x != dest.x) {
            pos.x = LunarMathUtils.lerp(pos.x, dest.x, Gdx.graphics.getDeltaTime() * animSpeed);
        }
        if (pos.y != dest.y) {
            pos.y = LunarMathUtils.lerp(pos.y, dest.y, Gdx.graphics.getDeltaTime() * animSpeed);
        }
        if (scale != targetScale) {
            scale = LunarMathUtils.lerp(scale, targetScale, Gdx.graphics.getDeltaTime() * animSpeed);
        }
        if (targetArea != null && tabArea != null) {
            LunarMathUtils.lerpHitbox(tabArea, targetArea, Gdx.graphics.getDeltaTime() * animSpeed);
        }
    }
    
    public void render(SpriteBatch sb) {
        options.forEach(opt -> opt.render(sb));
        renderInnerFrame(sb);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(image, hb.cX - imageW / 2F, hb.cY - imageH / 2F, imageW / 2F, imageH / 2F, imageW, imageH, 
                scale, scale, 0F, 0, 0, imageW, imageH, false, false);
        if (tabArea != null) tabArea.render(sb);
        renderGlow(sb);
        hb.render(sb);
    }
    
    protected void renderInnerFrame(SpriteBatch sb) {
        if (tabArea != null && tabArea.height > 0) {
            sb.setColor(Color.WHITE.cpy());
            if (inspectingTab) {
                sb.draw(FRAME_TOP, hb.cX - scale(LunarImageMst.INNER_FRAME_W) / 2F, frameTopRenderY,
                        scale(LunarImageMst.INNER_FRAME_W), LunarImageMst.INNER_FRAME_H);
            }
            if (hb.y - frameBotRenderY > LunarImageMst.INNER_FRAME_H) {
                sb.draw(FRAME_MID, hb.cX - scale(LunarImageMst.INNER_FRAME_W) / 2F, frameMidRenderY,
                        scale(LunarImageMst.INNER_FRAME_W), frameMidRenderHeight);
            }
            sb.draw(FRAME_BOT, hb.cX - scale(LunarImageMst.INNER_FRAME_W) / 2F, frameBotRenderY,
                    scale(LunarImageMst.INNER_FRAME_W), LunarImageMst.INNER_FRAME_H);
        }
    }
    
    protected void renderGlow(SpriteBatch sb) {
        if (inspectingTab) {
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LunarImageMst.LoadoutTabGlow, hb.cX - LunarImageMst.TAB_GLOW_W / 2F, hb.cY - LunarImageMst.TAB_GLOW_H / 2F,
                    LunarImageMst.TAB_GLOW_W / 2F, LunarImageMst.TAB_GLOW_H / 2F, 
                    LunarImageMst.TAB_GLOW_W, LunarImageMst.TAB_GLOW_H, scale, scale, 0F, 0, 0,
                    LunarImageMst.TAB_GLOW_W, LunarImageMst.TAB_GLOW_H, false, false);
        }
    }
    
    protected void putOption(LoadoutOption option) {
        if (!options.contains(option))
            options.add(option);
    }
    
    protected abstract void updateOnHovering();
    protected abstract void updateOnUnhovering();
    protected abstract void saveConfig(LoadoutConfig config);
    protected abstract void loadConfig(Prefs pref);
}