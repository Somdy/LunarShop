package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lazymankits.abstracts.LMCustomGameEffect;

public class MeteoriteRainEffect extends LMCustomGameEffect {
    private Vector2 land;
    private Vector2 acc;
    private Vector2 v;
    private Vector2 pos;
    private float rotation;
    private Texture img;
    
    public MeteoriteRainEffect(Vector2 land, boolean behind) {
        this.land = land;
        rotation = MathUtils.random(0, 20F);
        img = ImageMaster.loadImage("LunarAssets/imgs/vfx/Meteorite.png");
        loadArguments(behind);
    }
    
    public MeteoriteRainEffect(float srcX, float tgtX, float tgtY) {
        this(new Vector2(tgtX, tgtY), srcX > tgtX);
    }
    
    private void loadArguments(boolean behind) {
        float t = Settings.FAST_MODE ? 3F : 6F;
        acc = new Vector2(0F, 0.98F);
        float startY = (float) ((acc.y * Math.pow(t, 2)) / 2F);
        float startX = behind ? (land.x + Settings.WIDTH) : (land.x - Settings.WIDTH);
        pos = new Vector2(startX, startY);
        float vX = (land.x - startX) / t;
        v = new Vector2(vX, 0F);
        duration = 0F;
//        float factor = (float) LManager.gameFps.getCurrFps();
//        acc = new Vector2(0F, 0);
//        float t = (land.sub(from).len() * timeScale) / factor;
//        acc.x = (Math.abs((land.x - from.x)) / t) / factor;
//        acc.y = (Math.abs((land.y - from.y)) / t) / factor;
//        if (land.x > from.x && acc.x < 0 || land.x < from.x && acc.x > 0)
//            acc.x *= -1F;
//        v = new Vector2(0, 0F);
//        startingDuration = duration = t;
    }
    
    @Override
    public void update() {
        duration += Gdx.graphics.getDeltaTime();
        updateRotation();
        updatePosition();
        updateLanding();
    }
    
    private void updateLanding() {
        float diff = land.sub(pos).len();
        if (diff < 0.5F) {
            isDone = true;
            updateCollisionLogic();
        }
    }
    
    private void updateCollisionLogic() {
        
    }
    
    private void updatePosition() {
        v.y = acc.y * duration;
        pos.x += v.x;
        pos.y -= v.y;
    }
    
    private void updateRotation() {
        rotation += Gdx.graphics.getDeltaTime() * 10F * ((land.sub(pos).len()) / 100F);
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(img, pos.x, pos.y, 64F, 64F, 128F, 128F, Settings.scale, Settings.scale, 
                rotation, 0, 0, 128, 128, false, false);
    }
    
    @Override
    public void dispose() {
        img.dispose();
    }
}