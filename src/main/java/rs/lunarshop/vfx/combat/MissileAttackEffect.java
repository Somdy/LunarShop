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
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;

public class MissileAttackEffect extends LMCustomGameEffect implements LunarUtils {
    private final Hitbox target;
    private Vector2 dest;
    private final Vector2 src;
    private Vector2 pos;
    private Vector2 acc;
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
    
    public MissileAttackEffect(Hitbox target, Vector2 src, float time) {
        this.target = target;
        this.src = src;
        this.time = time;
        scale = Settings.scale;
        missile = new Hitbox(52F, 13F);
        proj = ImageMaster.loadImage("LunarAssets/imgs/vfx/missile.png");
        color = Color.YELLOW.cpy();
    }
    
    public MissileAttackEffect(AbstractCreature target, float sX, float sY, float time) {
        this(target.hb, new Vector2(sX, sY), time);
    }
    
    public MissileAttackEffect setColor(Color color) {
        this.color = color;
        return this;
    }
    
    public MissileAttackEffect build() {
        trails = new ArrayList<>();
        collided = false;
        turned = false;
        targetRot = rotation = 0F;
        initVars();
        return this;
    }
    
    void initVars() {
        target.move(target.cX + MathUtils.random(-target.width / 3F, target.width / 3F), 
                target.cY + MathUtils.random(-target.height / 4F, target.height / 4F));
        pos = new Vector2(src);
        dest = new Vector2(pos);
        v = new Vector2(Math.abs(target.cX - pos.x) / time, Math.abs(target.cY - pos.y) / time);
        if (target.cX < src.x && v.x > 0)
            v.x *= -1F;
        if (target.cY < src.y && v.y > 0)
            v.y *= -1F;
        acc = new Vector2(v.x * MathUtils.random(-4F, -2F), Math.abs(v.y) * MathUtils.random(8F, 6F));
        missile.move(pos.x, pos.y);
        flipX = src.x > target.cX;
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
        dest.x += (v.x + acc.x) * Gdx.graphics.getDeltaTime();
        dest.y += (v.y + acc.y) * Gdx.graphics.getDeltaTime();
        pos.x = LunarMathUtils.lerp(pos.x, dest.x, Gdx.graphics.getDeltaTime() * 5F);
        pos.y = LunarMathUtils.lerp(pos.y, dest.y, Gdx.graphics.getDeltaTime() * 5F);
        missile.move(pos.x, pos.y);
        if (rotation != targetRot)
            rotation = LunarMathUtils.lerp(targetRot, rotation, Gdx.graphics.getDeltaTime() * 5F);
    }
    
    void updateAcc() {
        boolean oX = (target.cX > src.x && pos.x < src.x) || (target.cX < src.x && pos.x > src.x);
        if (oX) {
            acc.x = LunarMathUtils.lerp(acc.x, 0F, Gdx.graphics.getDeltaTime() * 2F);
            if (Math.abs(acc.x) <= 0.1F) {
                acc.x = 0F;
                acc.y /= 2F;
            }
        }
        updateTurningLogic();
    }
    
    void updateTurningLogic() {
        if (!turned) {
            turned = (flipX && pos.x < src.x) || (!flipX && pos.x > src.x);
            if (turned) {
                acc.y = 0F;
                v = new Vector2(Math.abs(target.cX - pos.x) / (time * 0.75F), 
                        Math.abs(target.cY - pos.y) / (time * 0.75F));
                if (target.cX < src.x && v.x > 0)
                    v.x *= -1F;
                if (target.cY < pos.y && v.y > 0)
                    v.y *= -1F;
                calcRotation();
            }
        }
    }
    
    void calcRotation() {
        Vector2 i = new Vector2(target.cX - pos.x, 0F);
        Vector2 tmp = new Vector2(target.cX - pos.x, target.cY - pos.y);
        double angle = Math.acos((i.x * tmp.x + i.y * tmp.y) / (i.len() * tmp.len()));
        targetRot = (float) Math.toDegrees(angle);
        if (flipX)
            targetRot = 360F - targetRot;
        if (target.cY < pos.y)
            targetRot *= -1F;
    }
    
    void updateCollision() {
        if (missile.intersects(target)) {
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