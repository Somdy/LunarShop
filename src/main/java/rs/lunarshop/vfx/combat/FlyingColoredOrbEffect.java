package rs.lunarshop.vfx.combat;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;
import org.jetbrains.annotations.NotNull;

public class FlyingColoredOrbEffect extends FlyingOrbEffect {
    public FlyingColoredOrbEffect(float sX, float sY, float dX, float dY, @NotNull Color color) {
        super(sX, sY);
        ReflectionHacks.setPrivate(this, FlyingOrbEffect.class, "target", new Vector2(dX, dY));
        this.color = color.cpy();
    }
    
    public FlyingColoredOrbEffect(float sX, float sY, @NotNull Color color) {
        super(sX, sY);
        this.color = color.cpy();
    }
}