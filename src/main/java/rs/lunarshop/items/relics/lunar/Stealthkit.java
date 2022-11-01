package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Stealthkit extends LunarRelic {
    private static final float BASE_THRESHOLD = 0.15F;
    private float threshold;
    private boolean hasTriggeredOnce;
    
    public Stealthkit() {
        super(41);
        threshold = BASE_THRESHOLD;
        counter = 0;
        hasTriggeredOnce = false;
        setBattleUse(3);
        presetInfo(s -> {
            if (canActivate()) {
                int maxHp = MathUtils.floor(cpr().maxHealth * threshold);
                s[0] = createInfo(DESCRIPTIONS[1], maxHp);
            } else {
                s[0] = DESCRIPTIONS[2];
            }
        });
    }
    
    @Override
    public void refreshStats() {
        threshold = 1 - 1 / (1 + BASE_THRESHOLD * stack);
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (canActivate() && cpr().currentHealth <= MathUtils.floor(cpr().maxHealth * threshold)) {
            hasTriggeredOnce = true;
            if (damageAmount > counter) {
                addToBot(new RelicAboveCreatureAction(cpr(), this));
                damageAmount ^= counter;
                counter ^= damageAmount;
                damageAmount ^= counter;
            }
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public void onVictory() {
        counter = 0;
        if (hasTriggeredOnce) {
            hasTriggeredOnce = false;
            deplete();
        }
    }
}
