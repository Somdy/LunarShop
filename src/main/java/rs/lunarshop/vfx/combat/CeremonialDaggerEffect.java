package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.LManager;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.items.relics.lunar.CeremonialDagger;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CeremonialDaggerEffect extends LMCustomGameEffect implements LunarUtils {
    private final List<CeremonialDaggerOnionSkin> skins = new ArrayList<>();
    private CeremonialDagger dagger;
    private Vector2 source;
    private Vector2 v;
    private final CustomDmgInfo info;
    private final Hitbox hb;
    private final Texture img;
    private float time;
    private boolean built;
    private boolean flipX;
    private boolean flipY;
    private boolean collided;
    
    public CeremonialDaggerEffect(Vector2 source, AbstractCreature target, CustomDmgInfo info) {
        this.source = source;
        this.target = target;
        this.info = info;
        hb = new Hitbox(128, 30);
        img = LunarImageMst.Dagger;
        time = 1.5F;
        built = false;
        collided = false;
    }
    
    public CeremonialDaggerEffect(CeremonialDaggerPreEffect preEffect) {
        this(preEffect.location, preEffect.target, preEffect.info);
        this.dagger = preEffect.dagger;
        setColor(Color.PURPLE.cpy());
        setTime(0.5F);
        color.a = 0.6F;
        scale = preEffect.scale;
        build();
    }
    
    private void calcVars() {
        Vector2 direct = new Vector2(target.hb.cX - source.x, target.hb.cY - source.y);
        Vector2 dist = new Vector2(target.hb.cX - source.x, 0);
        double angle = Math.acos((direct.x * dist.x + direct.y * dist.y) / (direct.len() * dist.len()));
        rotation = (float) Math.toDegrees(angle);
        if (target.hb.cY < source.y && rotation < 180)
            rotation *= -1F;
        time = (float) (time * LManager.gameFps.getCurrFps());
        v = new Vector2(direct.x / time, direct.y / time);
        flipX = target.hb.cX < source.x;
        flipY = target.hb.cY > source.y;
        hb.move(source.x, source.y);
    }
    
    public CeremonialDaggerEffect setColor(Color color) {
        this.color = color;
        return this;
    }
    
    public CeremonialDaggerEffect setTime(float time) {
        this.time = time;
        return this;
    }
    
    public CeremonialDaggerEffect build() {
        calcVars();
        built = true;
        return this;
    }
    
    private void relocateTarget() {
        if (areMstrBasicallyDead()) {
            isDone = true;
            return;
        }
        Optional<AbstractMonster> opt = getRandom(getAllLivingMstrs(), cardRandomRng());
        opt.ifPresent(m -> {
            this.target = m;
            this.source = new Vector2(hb.cX, hb.cY);
            calcVars();
        });
    }
    
    @Override
    public void update() {
        if (!built) build();
        if (collided) {
            color.a = 0F;
            updateSkins();
            if (skins.isEmpty()) {
                isDone = true;
            }
            return;
        }
        if (!target.isDeadOrEscaped()) {
            updateSkins();
            hb.move(hb.cX + v.x, hb.cY + v.y);
            hb.update();
            updateCollision();
        } else {
            relocateTarget();
        }
    }
    
    private void updateCollision() {
        if (hb.intersects(target.hb)) {
            collided = true;
            if (dagger != null) {
                dagger.preLoadDaggers();
            }
            if (!target.isDeadOrEscaped()) {
                LMSK.AddToTop(new NullableSrcDamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }
    
    private void updateSkins() {
        if (duration % LManager.gameFps.getCurrFps() == 0 && !collided)
            skins.add(new CeremonialDaggerOnionSkin(new Vector2(hb.cX, hb.cY), color, rotation, flipX, flipY));
        for (CeremonialDaggerOnionSkin s : skins) {
            s.update();
        }
        skins.removeIf(s -> s.isDone && s.finished);
    }
    
    @Override
    public void render(SpriteBatch sb) {
        hb.render(sb);
        renderSkins(sb);
        sb.setColor(color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_TRUE);
        sb.draw(img, hb.cX, hb.cY, 64F, 15F, 128F, 30F, Settings.scale, Settings.scale,
                rotation, 0, 0, 128, 30, flipX, flipY);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    private void renderSkins(SpriteBatch sb) {
        for (CeremonialDaggerOnionSkin s : skins) {
            s.render(sb);
        }
    }
    
    @Override
    public void dispose() {}
}