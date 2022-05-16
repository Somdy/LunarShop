package rs.lunarshop.vfx.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.utils.LunarMathUtils;
import rs.lunarshop.utils.LunarUtils;

public class EssenceSpinningEffect extends LMCustomGameEffect implements LunarUtils {
    private EssenceCoreEffect core;
    private final Texture cube;
    private Hitbox hb;
    private Vector2 dest;
    protected Vector2 pos;
    protected float radius;
    protected float revolution;
    protected float scale;
    private final float angV;
    private float v;
    protected boolean reverse;
    protected boolean built;
    
    protected EssenceSpinningEffect(EssenceCoreEffect core, Color color, boolean behind) {
        this.core = core;
        this.color = color;
        this.renderBehind = behind;
        cube = ImageMaster.loadImage("LunarAssets/imgs/vfx/command_essence/cube.png");
        hb = new Hitbox(27F, 27F);
        revolution = 0F;
        angV = 2F;
        reverse = false;
        built = false;
    }
    
    public EssenceSpinningEffect build() {
        built = true;
        initPosition();
        return this;
    }
    
    void initPosition() {
        Vector2 datum = new Vector2((float) (radius * Math.cos(revolution)), (float) (radius * Math.sin(revolution)));
//        datum.rotate(revolution);
        datum.add(core.hb.cX, core.hb.cY);
        pos = new Vector2(datum);
    }
    
    void keep() {
        calcVars();
        pos.x = dest.x;
        pos.y = dest.y;
        hb.move(pos.x, pos.y);
    }
    
    @Override
    public void update() {
        if (!built) return;
        revolution += Gdx.graphics.getDeltaTime() * angV;
        rotation += Gdx.graphics.getDeltaTime() * (radius > 0 ? radius : 2F) * 2F;
        if (revolution > 360F)
            revolution = 0F;
        calcVars();
        pos.x = LunarMathUtils.lerp(pos.x, dest.x, Gdx.graphics.getDeltaTime() * v);
        pos.y = LunarMathUtils.lerp(pos.y, dest.y, Gdx.graphics.getDeltaTime() * v);
        hb.move(pos.x, pos.y);
    }
    
    void calcVars() {
        Vector2 datum = new Vector2((float) (radius * Math.cos(revolution)), (float) (radius * Math.sin(revolution)));
//        datum.rotate(revolution);
        datum.add(core.hb.cX, core.hb.cY);
        dest = new Vector2(datum);
        float rV = (float) Math.toRadians(angV);
        v = rV * radius;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        if (!built) return;
        hb.render(sb);
        sb.setColor(color);
        float oS = scale;
        oS += scale(core.scale / (1 - scale + 1));
        sb.draw(cube, hb.x, hb.y, 13.5F, 13.5F, 27F, 27F, oS, oS,
                rotation, 0, 0, 27, 27, false, false);
    }
    
    @Override
    public void dispose() {
        cube.dispose();
    }
}