package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.MathHelper;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.CeremonialDagger;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarMathUtils;
import rs.lunarshop.utils.LunarUtils;

public class CeremonialDaggerPreEffect extends LMCustomGameEffect implements LunarUtils {
    protected CeremonialDagger dagger;
    protected AbstractCreature target;
    protected CustomDmgInfo info;
    private final Texture imgTarget;
    protected final Vector2 location;
    private final Color colorTgt;
    private float targetRot;
    protected float scale;
    private boolean throwDagger;
    private boolean delayed;
    
    public CeremonialDaggerPreEffect(CeremonialDagger dagger, AbstractCreature target, float offsetX, CustomDmgInfo info) {
        this.target = target;
        this.info = info;
        this.dagger = dagger;
        imgTarget = LunarImageMst.Dagger;
        location = new Vector2(dagger.currentX + offsetX, dagger.hb.y - scale(MathUtils.random(55F, 75F)));
        colorTgt = Color.PURPLE.cpy();
        colorTgt.a = 0F;
        rotation = -90;
        throwDagger = false;
        scale = 0.6F;
        duration = startingDuration = 0.5F;
        calcVars();
    }
    
    public CeremonialDaggerPreEffect(CeremonialDagger dagger, float offsetX) {
        this.dagger = dagger;
        imgTarget = LunarImageMst.Dagger;
        location = new Vector2(dagger.currentX + offsetX, dagger.hb.y - scale(MathUtils.random(55F, 75F)));
        colorTgt = Color.PURPLE.cpy();
        colorTgt.a = 0F;
        rotation = -90;
        throwDagger = false;
        scale = 0.6F;
        duration = startingDuration = 0.5F;
        delayed = true;
    }
    
    private void calcVars() {
        Vector2 direct = new Vector2(target.hb.cX - location.x, target.hb.cY - location.y);
        Vector2 dist = new Vector2(target.hb.cX - location.x, 0);
        double angle = Math.acos((direct.x * dist.x + direct.y * dist.y) / (direct.len() * dist.len()));
        targetRot = (float) Math.toDegrees(angle);
        if (target.hb.cY < location.y && targetRot < 180)
            targetRot *= -1F;
    }
    
    public CeremonialDaggerPreEffect load(AbstractCreature target, CustomDmgInfo info) {
        this.target = target;
        this.info = info;
        calcVars();
        delayed = false;
        return this;
    }
    
    @Override
    public void update() {
        if (delayed) {
            colorTgt.a = LunarMathUtils.lerp(colorTgt.a, 0.6F, Gdx.graphics.getDeltaTime());
            return;
        }
        if (!throwDagger) {
            colorTgt.a = MathHelper.fadeLerpSnap(colorTgt.a, 0.6F);
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (Math.abs(targetRot - rotation) > 1F && !throwDagger) {
            rotation += rotD() * Gdx.graphics.getDeltaTime() * 75F;
        }
        if (Math.abs(targetRot - rotation) <= 1F) {
            throwDagger();
        }
        if (throwDagger) {
            colorTgt.a = MathHelper.fadeLerpSnap(colorTgt.a, 0F);
            if (colorTgt.a <= 0F)
                isDone = true;
        }
    }
    
    private float rotD() {
        return targetRot > rotation ? 1F : -1F;
    }
    
    private void throwDagger() {
        if (!throwDagger) {
            throwDagger = true;
            LunarMod.addToTop(new VFXAction(new CeremonialDaggerEffect(this)));
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        renderTgtImg(sb);
    }
    
    private void renderTgtImg(SpriteBatch sb) {
        sb.setColor(colorTgt);
        sb.draw(imgTarget, location.x, location.y, 64F, 15F, 128F, 30F, scale, scale,
                rotation, 0, 0, 128, 30, false, false);
    }
    
    @Override
    public void dispose() {}
}