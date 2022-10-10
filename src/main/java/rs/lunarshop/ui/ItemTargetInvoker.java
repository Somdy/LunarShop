package rs.lunarshop.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemTargetInvoker implements LunarUtils {
    private static Color enemyColor = Color.RED.cpy();
    private static Color friendColor = Color.LIME.cpy();
    private static Color unhoveredColor = Color.GRAY.cpy();
    private static boolean isHidden = true;
    private AbstractLunarRelic item;
    private List<AbstractCreature> inopts;
    private AbstractCreature hoveredCreature;
    private Predicate<AbstractCreature> expt;
    private Consumer<AbstractCreature> func;
    private Vector2 controlPoint;
    private Vector2 start;
    private Vector2[] points;
    private float arrowScale;
    private float arrowScaleTimer;
    
    public void active(AbstractLunarRelic item, Consumer<AbstractCreature> func, Predicate<AbstractCreature> expt) {
        this.item = item;
        log("[" + item.prop.localID + "] uses target mode at [" + item.hb.cX + ", " + item.hb.cY + "]");
        this.start = new Vector2(item.currentX, item.currentY);
        this.func = func;
        this.expt = expt;
        init();
    }
    
    private void init() {
        isHidden = false;
        inopts = new ArrayList<>();
        GameCursor.hidden = true;
        points = new Vector2[20];
        for (int i = 0; i < points.length; i++) points[i] = new Vector2();
    }
    
    public void close() {
        isHidden = true;
        clearPostUsed();
    }
    
    public void execute(@NotNull AbstractCreature t) {
        log("[" + item.prop.localID + "] executes final action at " + t.name);
        func.accept(t);
        clearPostUsed();
    }
    
    private void clearPostUsed() {
        inopts.clear();
    }
    
    private void updateInvokerMode() {
        if (InputHelper.justClickedRight || AbstractDungeon.isScreenUp || AbstractDungeon.player.hoveredCard != null
                || InputHelper.mY > Settings.HEIGHT - scale(80F) || InputHelper.mY < scale(140F)) {
            GameCursor.hidden = false;
            close();
        }
        hoveredCreature = null;
        List<AbstractCreature> options = getAllExptCreatures(expt);
        inopts.addAll(options);
        for (AbstractCreature c : options) {
            if (!c.isDying && c.hb.hovered) {
                hoveredCreature = c;
                break;
            }
        }
        if (InputHelper.justClickedLeft) {
            InputHelper.justClickedLeft = false;
            if (hoveredCreature != null) {
                execute(hoveredCreature);
            }
            GameCursor.hidden = false;
            close();
        }
    }
    
    private void renderPointer(SpriteBatch sb) {
        float x = InputHelper.mX;
        float y = InputHelper.mY;
        controlPoint = new Vector2(start.x - (x - start.y) / 4F, start.y + (y - start.y - scale(40F)) / 2F);
        if (hoveredCreature == null) {
            arrowScale = Settings.scale;
            arrowScaleTimer = 0F;
            sb.setColor(unhoveredColor);
        } else {
            arrowScaleTimer += Gdx.graphics.getDeltaTime();
            if (arrowScaleTimer > 1F) {
                arrowScaleTimer = 1F;
            }
            arrowScale = Interpolation.elasticOut.apply(Settings.scale, scale(1.2F), arrowScaleTimer);
            sb.setColor(isFriendlyTarget() ? friendColor : enemyColor);
        }
        Vector2 tmp = new Vector2(controlPoint.x - x, controlPoint.y - y);
        tmp.nor();
        drawCurvedLine(sb, start, new Vector2(x, y), controlPoint);
        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128F, y - 128F, 128F, 128F,
                256F, 256F, arrowScale, arrowScale, tmp.angle() + 90F,
                0, 0, 256, 256, false, false);
    }
    
    private boolean isFriendlyTarget() {
        return hoveredCreature.isPlayer;
    }
    
    private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {
        float radius = 7F * Settings.scale;
        for (int i = 0; i < points.length - 1; i++) {
            float angle;
            points[i] = Bezier.quadratic(points[i], i / 20F, start, control, end, new Vector2());
            radius += scale(0.4F);
            if (i != 0) {
                Vector2 tmp = new Vector2((points[i - 1]).x - (points[i]).x, (points[i - 1]).y - (points[i]).y);
                angle = tmp.nor().angle() + 90F;
            } else {
                Vector2 tmp = new Vector2(controlPoint.x - (points[i]).x, controlPoint.y - (points[i]).y);
                angle = tmp.nor().angle() + 270F;
            }
            sb.draw(ImageMaster.TARGET_UI_CIRCLE, points[i].x - 64F, points[i].y - 64F,
                    64F, 64F, 128F, 128F, radius / 18F, radius / 18F, angle,
                    0, 0, 128, 128, false, false);
        }
    }
    
    public void render(SpriteBatch sb) {
        if (!isHidden) {
            renderPointer(sb);
            if (hoveredCreature != null) {
                hoveredCreature.renderReticle(sb);
            }
        }
    }
    
    public void update() {
        if (!isHidden)
            updateInvokerMode();
    }
}