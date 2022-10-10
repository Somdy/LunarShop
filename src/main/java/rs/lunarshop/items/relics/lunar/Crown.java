package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.actions.common.TargetGainGoldAction;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Crown extends LunarRelic {
    private int golds;
    private float hpPercent;
    
    public Crown() {
        super(1);
        golds = 18;
        hpPercent = 0.5F;
    }
    
    @Override
    public void refreshStats() {
        golds = 18 + 6 * (stack - 1);
        hpPercent = (float) (0.5F * Math.pow(0.25F + 1, (stack - 1)));
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], golds, SciPercent(hpPercent));
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        float baseChance = 0.3F;
        if (rollCloverLuck(baseChance) && info.owner != null) {
            addToBot(new RelicAboveCreatureAction(info.owner, this));
            addToBot(new TargetGainGoldAction(info.owner, target, golds));
        }
        super.onAttack(info, damageAmount, target);
    }
    
    @Override
    public void onLoseHp(int damageAmount) {
        addToBot(new RelicAboveCreatureAction(cpr(), this));
        addToBot(new QuickAction(() -> {
            int hpLoss = (cpr().maxHealth - cpr().currentHealth + damageAmount);
            cpr().loseGold(MathUtils.floor(hpLoss * hpPercent));
        }));
        super.onLoseHp(damageAmount);
    }
}