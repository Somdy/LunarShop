package rs.lunarshop.abstracts;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public abstract class AbstractLunarOrb extends AbstractOrb {
    private static final float ORB_WAVY_DIST = 0.04F;
    private static final float vfxIntervalMin = 0.1F;
    private static final float vfxIntervalMax = 0.4F;
    
    public AbstractLunarOrb(Texture orbImg) {
        super();
        img = orbImg;
    }
    
    @Override
    public void update() {
        super.update();
    }
    
    @Override
    public void updateAnimation() {
        super.updateAnimation();
    }
    
    @Override
    public void render(SpriteBatch sb) {
        c.a /= 2F;
        sb.setColor(c);
        sb.draw(img, cX - 48F, cY - 48F + bobEffect.y, 48F, 48F, 96F, 96F,
                scale + MathUtils.sinDeg(angle) * ORB_WAVY_DIST * Settings.scale, scale, angle,
                0, 0, 96, 96, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_TRUE);
        sb.draw(img, cX - 48F, cY - 48F + bobEffect.y, 48F, 48F, 96F, 96F,
                scale, scale + MathUtils.sinDeg(angle) * ORB_WAVY_DIST * Settings.scale, -angle,
                0, 0, 96, 96, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hb.render(sb);
    }
}