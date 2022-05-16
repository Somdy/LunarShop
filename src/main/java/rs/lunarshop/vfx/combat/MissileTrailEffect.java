package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarMathUtils;

public class MissileTrailEffect extends LMCustomGameEffect {
    protected Hitbox hb;
    protected Color color;
    protected Texture img;
    protected float scale;
    
    public MissileTrailEffect(Vector2 pos, Color color) {
        hb = new Hitbox(12F, 12F);
        hb.move(pos.x, pos.y);
        this.color = color.cpy();
        img = LunarImageMst.BlurDot;
        scale = Settings.scale;
    }
    
    @Override
    public void update() {
        color.a = LunarMathUtils.lerp(color.a, 0F, Gdx.graphics.getDeltaTime() * 1.5F);
        scale = LunarMathUtils.lerp(scale, 0F, Gdx.graphics.getDeltaTime() * 1.5F);
        if (color.a <= 0F)
            isDone = true;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, hb.x, hb.y, 6F, 6F, hb.width, hb.height, scale, scale, 
                0F, 0, 0, 12, 12, false, false);
    }
    
    @Override
    public void dispose() {
        
    }
}