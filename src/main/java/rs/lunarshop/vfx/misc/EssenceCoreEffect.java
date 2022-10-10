package rs.lunarshop.vfx.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.utils.LunarMathUtils;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;

public class EssenceCoreEffect extends LMCustomGameEffect implements LunarUtils {
    private Color coreColor;
    private Color glowColor;
    private Texture core;
    private Texture glow;
    protected Hitbox hb;
    private List<EssenceSpinningEffect> cubes;
    protected float scale;
    private float glowRate;
    private boolean glowing;
    private boolean pause;
    
    public EssenceCoreEffect(Color coreColor, Color glowColor, float glowRate) {
        this.coreColor = coreColor.cpy();
        this.glowColor = glowColor.cpy();
        this.glowRate = glowRate;
        core = ImageMaster.loadImage("LunarAssets/imgs/vfx/command_essence/core.png");
        glow = ImageMaster.loadImage("LunarAssets/imgs/vfx/command_essence/glow.png");
        hb = new Hitbox(64F, 64F);
        cubes = new ArrayList<>();
        scale = 1F;
        glowing = false;
        addCubes();
    }
    
    private void addCubes() {
        cubes.add(cube(0, false));
        cubes.add(cube(1, false));
        cubes.add(cube(2, MathUtils.randomBoolean()));
        cubes.add(cube(3, false));
        cubes.add(cube(3, true));
        cubes.add(cube(4, MathUtils.randomBoolean()));
    }
    
    EssenceSpinningEffect cube(int slot, boolean opposite) {
        EssenceSpinningEffect cube = new EssenceSpinningEffect(this, glowColor.cpy(), slot <= 1);
        float radius = scale(0F + 24F * slot);
        float scale = slot == 0 ? scale(1.25F) : scale(1F - 0.1F * slot);
        float startR = MathUtils.random(20F) + MathUtils.random((21F + slot * 5), 180F);
        cube.radius = radius;
        cube.scale = scale;
        cube.revolution = opposite ? (startR < 180F ? 180F + startR : startR - 180F) : startR;
        cube.reverse = MathUtils.randomBoolean();
        cube.build();
        return cube;
    }
    
    public EssenceCoreEffect setScale(float scale) {
        this.scale = scale;
        return this;
    }
    
    public EssenceCoreEffect translate(float x, float y) {
        hb.translate(x, y);
        return this;
    }
    
    public EssenceCoreEffect move(float cX, float cY) {
        hb.move(cX, cY);
        return this;
    }
    
    public EssenceCoreEffect setPause(boolean pause) {
        this.pause = pause;
        return this;
    }
    
    @Override
    public void update() {
        if (pause) {
            if (!cubes.isEmpty()) {
                cubes.forEach(EssenceSpinningEffect::keep);
            }
            return;
        }
        if (glowColor.a > 0 && !glowing) {
            glowColor.a = LunarMathUtils.lerp(glowColor.a, 0.4F, Gdx.graphics.getDeltaTime() * glowRate);
            if (Math.abs(glowColor.a - 0.4F) <= 0.05F) {
                glowColor.a = 0.4F;
                glowing = true;
            }
        } else {
            glowColor.a = LunarMathUtils.lerp(glowColor.a, 1F, Gdx.graphics.getDeltaTime() * glowRate);
            if (Math.abs(glowColor.a - 1F) <= 0.05F) {
                glowColor.a = 1F;
                glowing = false;
            }
        }
        if (!cubes.isEmpty()) {
            cubes.forEach(c -> {
                c.update();
            });
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        if (!cubes.isEmpty()) {
            for (EssenceSpinningEffect c : cubes) {
                if (c.renderBehind)
                    c.render(sb);
            }
        }
        hb.render(sb);
        sb.setColor(glowColor);
        sb.draw(glow, hb.x, hb.y, 32F, 32F, 64F, 64F, scale, scale,
                0F, 0, 0, 64, 64, false, false);
        sb.setColor(coreColor);
        sb.draw(core, hb.x, hb.y, 32F, 32F, 64F, 64F, scale, scale,
                0F, 0, 0, 64, 64, false, false);
        if (!cubes.isEmpty()) {
            for (EssenceSpinningEffect c : cubes) {
                if (!c.renderBehind)
                    c.render(sb);
            }
        }
    }
    
    @Override
    public void dispose() {
        core.dispose();
        glow.dispose();
        if (!cubes.isEmpty())
            cubes.forEach(EssenceSpinningEffect::dispose);
    }
}