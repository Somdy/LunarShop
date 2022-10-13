package rs.lunarshop.ui.cmdpicker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

public class ItemContainer implements LunarUtils {
    public static final int CONTAINER_W = 80;
    public static final int CONTAINER_H = 80;
    
    protected AbstractRelic relic;
    protected Hitbox hb;
    protected Vector2 pos;
    protected Vector2 dest;
    protected Color color;
    protected boolean hovered;
    protected boolean leftClicked;
    protected boolean rightClicked;
    protected boolean selected;
    protected float scale;
    
    private ItemContainer(AbstractRelic relic) {
        this.relic = relic;
        hb = new Hitbox(scale(CONTAINER_W), scale(CONTAINER_H));
        color = Color.WHITE.cpy();
        scale = 1F;
    }
    
    @NotNull
    protected static ItemContainer GetContainer(AbstractRelic relic, float startX, float startY, float targetX, float targetY) {
        ItemContainer ic = new ItemContainer(relic);
        ic.pos = new Vector2(startX, startY);
        ic.dest = new Vector2(targetX, targetY);
        ic.hb.translate(ic.pos.x, ic.pos.y);
        return ic;
    }
    
    @NotNull
    protected static ItemContainer GetContainer(AbstractRelic relic, float startX, float startY) {
        return GetContainer(relic, startX, startY, startX, startY);
    }
    
    public void update(boolean updateRelic) {
        updateHbLogic(updateRelic);
        updateRelic(updateRelic);
    }
    
    private void updateHbLogic(boolean updateRelic) {
        hb.update();
        if (pos.x != dest.x) {
            pos.x = MathHelper.scrollSnapLerpSpeed(pos.x, dest.x);
        }
        if (pos.y != dest.y) {
            pos.y = MathHelper.scrollSnapLerpSpeed(pos.y, dest.y);
        }
        hb.translate(pos.x, pos.y);
        hovered = hb.hovered;
        scale = 1F;
        if (hovered && updateRelic) {
            if (InputHelper.justClickedLeft) {
                hb.clickStarted = true;
            }
            if (InputHelper.justClickedRight) {
                rightClicked = true;
            }
            if (hb.clicked) {
                hb.clicked = false;
                leftClicked = true;
            }
        }
    }
    
    private void updateRelic(boolean updateRelic) {
        if (relic != null) {
            relic.targetX = hb.cX;
            relic.targetY = hb.cY;
            if (relic.currentX != relic.targetX) {
                relic.currentX = relic.targetX;
            }
            if (relic.currentY != relic.targetY) {
                relic.currentY = relic.targetY;
            }
            relic.hb.update();
            relic.update();
            if (hovered && !relic.hb.hovered && updateRelic)
                relic.hb.hovered = true;
            if (rightClicked) {
                CardCrawlGame.relicPopup.open(relic);
                repositionMouseOnRelicPopup();
                rightClicked = false;
            }
        }
    }
    
    protected void updateDest(float x, float y) {
        dest.add(x, y);
    }
    
    public void pickupRelic() {
        if (relic != null) {
            log(LMSK.Player().name + " picked up relic: " + relic.name);
            currRoom().spawnRelicAndObtain(pos.x, pos.y, relic.makeCopy());
        }
    }
    
    public int cleanseRelic() {
        int stack = 0;
        if (relic != null) {
            log(LMSK.Player().name + " purifying the relic: " + relic.name);
            LMSK.Player().loseRelic(relic.relicId);
            if (relic instanceof AbstractLunarRelic)
                stack = ((AbstractLunarRelic) relic).getStack();
        }
        return stack;
    }
    
    public void render(SpriteBatch sb) {
        hb.render(sb);
        color = (hovered || selected) ? Color.WHITE.cpy() : Color.DARK_GRAY.cpy();
        if (selected) {
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_TRUE);
            sb.setColor(color);
            sb.draw(LunarImageMst.CE_Container, hb.cX - CONTAINER_W / 2F, hb.cY - CONTAINER_H / 2F, 
                    CONTAINER_W / 2F, CONTAINER_H / 2F, 
                    CONTAINER_W, CONTAINER_H, scale(scale), scale(scale), 0F, 0, 0, 
                    CONTAINER_W, CONTAINER_H, false, false);
            sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            sb.setColor(color);
            sb.draw(LunarImageMst.CE_Container, hb.cX - CONTAINER_W / 2F, hb.cY - CONTAINER_H / 2F, 
                    CONTAINER_W / 2F, CONTAINER_H / 2F,
                    CONTAINER_W, CONTAINER_H, scale(scale), scale(scale), 0F, 0, 0,
                    CONTAINER_W, CONTAINER_H, false, false);
        }
        if (relic != null) relic.renderWithoutAmount(sb, Color.WHITE.cpy());
    }
}