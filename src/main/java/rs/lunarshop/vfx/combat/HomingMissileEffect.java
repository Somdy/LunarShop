package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.LunarMathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HomingMissileEffect extends LMCustomGameEffect {
    private AbstractCreature target;
    private Vector2 dest;
    private final Vector2 src;
    private Vector2 pos;
    private Vector2 acc;
    private Consumer<Vector2> accMod;
    private Vector2 v;
    private final Hitbox missile;
    private List<MissileTrailEffect> trails;
    protected Color color;
    protected Texture proj;
    private final float time;
    private float targetRot;
    private float rotation;
    public boolean collided;
    private boolean flipX;
    private boolean turned;
    
    public HomingMissileEffect(AbstractCreature target, Vector2 src, float time) {
        this.target = target;
        this.src = src;
        this.time = time;
        scale = Settings.scale;
        missile = new Hitbox(52F, 13F);
        proj = ImageMaster.loadImage("LunarAssets/imgs/vfx/missile.png");
        color = Color.YELLOW.cpy();
    }
    
    public HomingMissileEffect(AbstractCreature target, float sX, float sY, float time) {
        this(target, new Vector2(sX, sY), time);
    }
    
    public HomingMissileEffect setColor(Color color) {
        this.color = color;
        return this;
    }
    
    public HomingMissileEffect setAccMod(Consumer<Vector2> accMod) {
        this.accMod = accMod;
        return this;
    }
    
    public HomingMissileEffect setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }
    
    public HomingMissileEffect build() {
        trails = new ArrayList<>();
        collided = false;
        turned = false;
        targetRot = rotation = 0F;
        initVars();
        return this;
    }
    
    void initVars() {
        pos = new Vector2(src);
        dest = new Vector2(pos);
        v = new Vector2(Math.abs(target.hb.cX - pos.x) / time, Math.abs(target.hb.cY - pos.y) / time);
        if (target.hb.cX < src.x && v.x > 0)
            v.x *= -1F;
        if (target.hb.cY < src.y && v.y > 0)
            v.y *= -1F;
        acc = new Vector2(v.x * MathUtils.random(-4F, -2F), Math.abs(v.y) * MathUtils.random(8F, 6F));
        missile.move(pos.x, pos.y);
        flipX = src.x > target.hb.cX;
    }
    
    @Override
    public void update() {
        if (!collided) {
            updateFlying();
            updateCollision();
        }
        updateTrails();
    }
    
    void updateFlying() {
        updateAcc();
        float dt = Gdx.graphics.getDeltaTime();
        dest.x += acc.x != 0 ? acc.x * dt : v.x * dt;
        dest.y += acc.y != 0 ? acc.y * dt : v.y * dt;
        pos.x = LunarMathUtils.lerp(pos.x, dest.x, Gdx.graphics.getDeltaTime() * 5F);
        pos.y = LunarMathUtils.lerp(pos.y, dest.y, Gdx.graphics.getDeltaTime() * 5F);
        missile.move(pos.x, pos.y);
        updateRotation();
        if (rotation != targetRot)
            rotation = LunarMathUtils.lerp(targetRot, rotation, Gdx.graphics.getDeltaTime() * 5F);
        flipX = pos.x > target.hb.cX;
    }
    
    void updateAcc() {
        acc.x = LunarMathUtils.lerp(acc.x, 0F, Gdx.graphics.getDeltaTime() * 2F);
        acc.y = LunarMathUtils.lerp(acc.y, 0F, Gdx.graphics.getDeltaTime() * 2F);
        if (acc.x == 0 && acc.y == 0 && !turned)
            updateTurningLogic();
    }
    
    void updateTurningLogic() {
        turned = true;
        v = new Vector2(Math.abs(target.hb.cX - pos.x) / (time * 0.75F),
                Math.abs(target.hb.cY - pos.y) / (time * 0.75F));
        if (target.hb.cX < src.x && v.x > 0)
            v.x *= -1F;
        if (target.hb.cY < pos.y && v.y > 0)
            v.y *= -1F;
    }
    
    void updateRotation() {
        Vector2 datum = new Vector2(1, 0);
        Vector2 direction = new Vector2(dest.x - pos.x, dest.y - pos.y);
        
    }
    
    void updateCollision() {
        if (missile.intersects(target.hb)) {
            collided = true;
            color.a = 0F;
            LunarMod.addToTop(new VFXAction(new ExplosionSmallEffect(missile.cX, missile.cY)));
        }
    }
    
    void updateTrails() {
        if (!trails.isEmpty()) {
            for (MissileTrailEffect t : trails) {
                t.update();
            }
        }
        trails.removeIf(t -> t.isDone);
        if (!collided) {
            float oX = flipX ? missile.x + missile.width : missile.x;
            float oY = missile.cY;
            trails.add(new MissileTrailEffect(new Vector2(oX, oY), Color.ORANGE.cpy()));
        }
        if (collided && trails.isEmpty())
            isDone = true;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        if (!trails.isEmpty()) {
            for (MissileTrailEffect t : trails) {
                t.render(sb);
            }
        }
        missile.render(sb);
        sb.setColor(color);
        float oX = flipX ? 50F : 2F;
        sb.draw(proj, missile.x, missile.y, oX, 6.5F, 52F, 13F, scale, scale, rotation,
                0, 0, 52, 13, flipX, false);
    }
    
    @Override
    public void dispose() {
        proj.dispose();
    }
}