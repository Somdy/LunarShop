package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.utils.LunarImageMst;

public class CeremonialDaggerOnionSkin extends LMCustomGameEffect {
    private Vector2 where;
    private Texture img;
    private boolean[] flips;
    protected boolean finished;
    
    protected CeremonialDaggerOnionSkin(Vector2 where, @NotNull Color color, float rotation, @NotNull boolean... flips) {
        this.where = where;
        this.color = color.cpy();
        this.color.a = 0.4F;
        this.rotation = rotation;
        this.flips = new boolean[2];
        this.flips[0] = flips[0];
        this.flips[1] = flips[1];
        finished = false;
        img = LunarImageMst.Dagger;
        duration = startingDuration = 0.4F;
    }
    
    @Override
    public void update() {
        super.update();
        if (isDone) {
            color.a = 0F;
            finished = true;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, where.x, where.y, 64F, 15F, 128F, 30F, Settings.scale, Settings.scale, 
                rotation, 0, 0, 128, 30, flips[0], flips[1]);
    }
    
    @Override
    public void dispose() {}
}