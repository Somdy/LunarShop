package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import rs.lazymankits.abstracts.LMCustomGameEffect;

public class FireIgniteEffect extends LMCustomGameEffect {
    private float x;
    private float y;
    
    public FireIgniteEffect(float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    @Override
    public void update() {
        for (int i = 0; i < 25; i++) {
            AbstractDungeon.effectsQueue.add(new FireBurstParticleEffect(this.x, this.y));
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.x, this.y, color.cpy()));
        }
        isDone = true;
    }
    
    @Override
    public void render(SpriteBatch sb) {
        
    }
    
    @Override
    public void dispose() {
        
    }
}