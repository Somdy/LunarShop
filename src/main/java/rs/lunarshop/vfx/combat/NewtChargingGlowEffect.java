package rs.lunarshop.vfx.combat;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisParticle;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.utils.LunarMathUtils;

import java.util.ArrayList;
import java.util.List;

public class NewtChargingGlowEffect extends LMCustomGameEffect {
    
    private final TextureAtlas.AtlasRegion CORE = ImageMaster.GLOW_SPARK_2;
    private final TextureAtlas.AtlasRegion CORE_COVER = ImageMaster.GLOW_SPARK_2;
    private final Vector2 pos;
    private int count;
    private float particleTimer;
    private float targetScale;
    private boolean glowing;
    private boolean hiding;
    private final Color glowColor;
    private final List<HemokinesisParticle> particles;
    
    public NewtChargingGlowEffect(float tX, float tY, @NotNull Color color) {
        pos = new Vector2(tX, tY);
        count = 1;
        particleTimer = 5F;
        targetScale = scale = 1.5F * Settings.scale;
        glowing = false;
        this.color = color.cpy();
        glowColor = color.cpy();
        particles = new ArrayList<>();
    }
    
    public NewtChargingGlowEffect setHiding(boolean hiding) {
        this.hiding = hiding;
        return this;
    }
    
    public void addCount(int num) {
        count += num;
        targetScale = scale + 0.5F * Settings.scale;
    }
    
    @Override
    public void update() {
        if (hiding) return;
        updateParticles();
        rotation += Gdx.graphics.getDeltaTime() * 15F;
        if (Math.abs(targetScale - scale) > 0.5F) {
            scale = LunarMathUtils.lerp(scale, targetScale, Gdx.graphics.getDeltaTime() * 1.5F);
            if (Math.abs(targetScale - scale) <= 0.5F)
                scale = targetScale;
        }
        if (glowing && glowColor.a != 1F) {
            glowColor.a = LunarMathUtils.lerp(glowColor.a, 1F, Gdx.graphics.getDeltaTime() * (2F + count / 3F));
            if (Math.abs(1F - glowColor.a) <= 0.01F) {
                glowing = false;
                glowColor.a = 1F;
            }
        } else {
            glowColor.a = LunarMathUtils.lerp(glowColor.a, 0.4F, Gdx.graphics.getDeltaTime() * (2F + count / 3F));
            if (Math.abs(0.4F - glowColor.a) <= 0.01F) {
                glowing = true;
                glowColor.a = 0.4F;
            }
        }
    }
    
    void updateParticles() {
        if (!particles.isEmpty()) {
            List<HemokinesisParticle> temp = new ArrayList<>();
            for (HemokinesisParticle p : particles) {
                p.update();
                if (p.isDone && !temp.contains(p)) temp.add(p);
            }
            if (!temp.isEmpty()) {
                addCount(temp.size());
                particles.removeAll(temp);
                temp.clear();
            }
        }
        if (Math.abs(particleTimer - 0F) > 0.1F) {
            particleTimer -= Gdx.graphics.getDeltaTime();
            if (Math.abs(particleTimer - 0F) <= 0.1F)
                particleTimer = 0F;
        }
        if (particleTimer == 0F) {
            Color parc = color.cpy();
            parc.r += MathUtils.random(0.1F, 0.5F);
            addParticles(1, parc);
            particleTimer = 2F;
        }
    }
    
    public void addParticles(int num, Color particleColor) {
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                Vector2 from = new Vector2(MathUtils.random(CORE.packedWidth * 4 * Settings.scale,
                        5 * CORE.packedWidth * Settings.scale), 0F);
                from.rotate(MathUtils.random(1F + num * 5F, 359F + num * 10F));
                from.add(pos);
                HemokinesisParticle particle = new HemokinesisParticle(from.x, from.y, pos.x, pos.y, false);
                ReflectionHacks.setPrivate(particle, AbstractGameEffect.class, "color", particleColor.cpy());
                particles.add(particle);
            }
        }
    }
    
    @Override
    public void render(@NotNull SpriteBatch sb) {
        if (hiding) return;
        if (!particles.isEmpty()) {
            particles.forEach(p -> p.render(sb));
        }
        sb.setColor(color);
        sb.draw(CORE, pos.x - CORE.packedWidth / 2F, pos.y - CORE.packedHeight / 2F, 
                CORE.packedWidth / 2F, CORE.packedHeight / 2F, CORE.packedWidth, CORE.packedHeight, 
                scale, scale, rotation);
        sb.setColor(glowColor);
        sb.draw(CORE_COVER, pos.x - CORE_COVER.packedWidth / 2F, pos.y - CORE_COVER.packedHeight / 2F,
                CORE_COVER.packedWidth / 2F, CORE_COVER.packedHeight / 2F, 
                CORE_COVER.packedWidth, CORE_COVER.packedHeight,
                scale, scale, -rotation);
    }
    
    @Override
    public void dispose() {
        
    }
}