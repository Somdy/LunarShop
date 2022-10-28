package rs.lunarshop.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

public class LunarSweepingBeamEffect extends SweepingBeamEffect {
    public LunarSweepingBeamEffect(float sX, float sY, boolean isFlipped, Color color) {
        super(sX, sY, isFlipped);
        this.color = color.cpy();
    }
}