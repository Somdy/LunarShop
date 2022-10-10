package rs.lunarshop.vfx.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.enums.ShopRelicEffect;
import rs.lunarshop.utils.LunarUtils;

import java.lang.reflect.Field;

public class LunarStoreRelicEffect extends LMCustomGameEffect implements LunarUtils {
    private AbstractRelic relic;
    private ShopRelicEffect effect;
    private boolean pause;
    private BobEffect bob;
    private float anySpeed;
    private float transparency;
    
    public LunarStoreRelicEffect(AbstractRelic relic, ShopRelicEffect effect) {
        pause = true;
        this.relic = relic;
        this.effect = effect;
        init();
    }
    
    private void init() {
        switch (effect) {
            case FLOATING:
                bob = new BobEffect(1.5F);
                try {
                    Field timer = BobEffect.class.getDeclaredField("timer");
                    timer.setAccessible(true);
                    timer.set(bob, 0);
                } catch (Exception ignored) {}
                break;
            case VISUALIZING:
                duration = MathUtils.random(0.0F, 359.0F);
                anySpeed = 0.5F;
                transparency = 1F;
                break;
            case SPARKLING:
                anySpeed = 1.5F;
                break;
        }
    }
    
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    
    @Override
    public void update() {
        if (pause) return;
        switch (effect) {
            case FLOATING:
                bob.update();
                relic.currentY += bob.y * 0.025F;
                relic.hb.move(relic.currentX, relic.currentY);
                break;
            case VISUALIZING:
                if (relic.flashTimer <= 0) {
                    transparency = MathUtils.sin(duration) / 2F;
                    relic.flashTimer = transparency;
                    duration += Gdx.graphics.getDeltaTime() * anySpeed;
                }
                break;
            case SPARKLING:
                if (anySpeed > 0) {
                    anySpeed -= Gdx.graphics.getDeltaTime();
                }
                if (anySpeed <= 0F) {
                    for (int i = 0; i < MathUtils.random(2, 4); i++) {
                        effectToList(new ShineSparkleEffect(relic.hb));
                    }
                    anySpeed = MathUtils.random(0.05F, 0.1F);
                }
                break;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        
    }
    
    @Override
    public void dispose() {
        
    }
}