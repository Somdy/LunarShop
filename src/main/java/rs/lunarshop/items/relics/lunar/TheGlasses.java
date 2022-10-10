package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.PotencyHelper;

public class TheGlasses extends LunarRelic {
    private float critRate;
    
    public TheGlasses() {
        super(38);
        critRate = 0.1F;
    }
    
    @Override
    public void refreshStats() {
        critRate = 0.1F * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(critRate));
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL && info.output > 0
                && rollCloverLuck(critRate)) {
            info.output = MathUtils.ceil(info.output * 1.5F);
        }
        super.preModifyDamage(info, who);
    }
    
    @Override
    public boolean canSpawnForShopping(int shopType) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1) && super.canSpawnForShopping(shopType);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1);
    }
}