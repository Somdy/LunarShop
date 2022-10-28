package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LegacyRelic;

public class Lopper extends LegacyRelic {
    private static final float BASE_HP_THRESHOLD = 0.5F;
    private static final float THRESHOLD_PER_STACK = 0.2F;
    private static final float CRIT_MULT = 2F;
    private float hpThreshold;
    
    public Lopper() {
        super(77);
        hpThreshold = BASE_HP_THRESHOLD;
        presetInfo(s -> createInfo(s, SciPercent(hpThreshold)));
    }
    
    @Override
    public void refreshStats() {
        hpThreshold = BASE_HP_THRESHOLD + (stack - 1) * THRESHOLD_PER_STACK;
    }
    
    @Override
    public float modifyCritChance(float origin, AbstractCreature hitTarget) {
        if (hitTarget.currentHealth < hitTarget.maxHealth * hpThreshold)
            origin = 1F;
        return super.modifyCritChance(origin, hitTarget);
    }
    
    @Override
    public float modifyCritMult(float origin) {
        return CRIT_MULT;
    }
}