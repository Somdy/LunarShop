package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.localizations.LunarTipLocals;
import rs.lunarshop.utils.LunarMathUtils;
import rs.lunarshop.utils.LunarTip;
import rs.lunarshop.utils.LunarTipHelper;
import rs.lunarshop.utils.LunarUtils;

public abstract class LoadoutOption implements LunarUtils {
    protected LoadoutTab tab;
    protected Texture image;
    protected Hitbox hb;
    protected boolean hovered;
    protected boolean isHidden;
    protected boolean selected;
    protected int imageW;
    protected int imageH;
    protected float scale;
    protected float targetScale;
    protected float animSpeed;
    protected Vector2 pos;
    protected Vector2 dest;
    protected int valueInt;
    protected String valueStr;
    protected LunarTip tip;
    
    public LoadoutOption(LoadoutTab tab, Texture image, int imageW, int imageH, float width, float height, float scale) {
        this.tab = tab;
        this.image = image;
        this.imageW = imageW;
        this.imageH = imageH;
        this.hb = new Hitbox(scale(width), scale(height));
        this.scale = this.targetScale = scale;
        pos = new Vector2(hb.x, hb.y);
        dest = new Vector2(hb.x, hb.y);
        tip = new LunarTip("", "");
    }
    
    public void setLocation(float cX, float cY) {
        hb.move(cX, cY);
        pos.set(hb.x, hb.y);
        dest.set(hb.x, hb.y);
        log("Set location at [" + cX + ", " + cY + "]");
    }
    
    public void move(float x, float y) {
        hb.translate(x, y);
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
        if (hovered && !isHidden) {
            if (!tab.inspectingTab)
                tab.inspectingTab = true;
            if (InputHelper.justClickedLeft) {
                hb.clickStarted = true;
            }
            if (hb.clicked) {
                hb.clicked = false;
                onLeftClick();
            }
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
    }
    
    private void onLeftClick() {
        selfOnLeftClick();
        tab.updateManagerValues();
        tab.manager.saveConfig();
    }
    
    protected float getRenderHeight(float originHeight) {
        float diff = tab.hb.y + scale(5F) - hb.y;
        return Math.min(diff, originHeight);
    }
    
    protected int getSrcRenderHeight(int originSrcHeight) {
        int diff = (int) (tab.hb.y + scale(5F) - hb.y);
        return Math.min(diff, originSrcHeight);
    }
    
    public void render(SpriteBatch sb) {
        if (isHidden) return;
        sb.setColor(Color.WHITE.cpy());
        sb.draw(image, hb.cX - imageW / 2F, hb.cY - imageH / 2F, imageW / 2F, imageH / 2F, imageW, 
                getRenderHeight(imageH), scale, scale, 0F, 0, imageH - getSrcRenderHeight(imageH), imageW, 
                getSrcRenderHeight(imageH), false, false);
        if (hovered && tip.hasTip()) {
            renderTip();
        }
        hb.render(sb);
    }
    
    public void unselect() {
        selected = false;
    }
    
    protected void renderTip() {
        LunarTipHelper.RenderGenericTip(LunarTipHelper.MouseOffsetLeftX(hb.x, hb.width / 2F), hb.cY, tip);
    }
    
    protected void makeLunarTip(@NotNull LunarTipLocals builder, int slot, Color hColor, Color bColor) {
        LunarTip tip = builder.getTip(slot, hColor, bColor);
        this.tip.copy(tip);
    }
    
    protected void makeLunarTip(LunarTip tip) {
        this.tip.copy(tip);
    }
    
    protected void makeLunarTip(String header, String body, Color hColor, Color bColor) {
        makeLunarTip(new LunarTip(header, body, hColor, bColor));
    }
    
    protected abstract void selfOnLeftClick();
}