package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.PotencyHelper;
import rs.lunarshop.utils.mechanics.CritHelper;

public class TheGlasses extends LunarRelic {
    private float critRate;
    
    public TheGlasses() {
        super(38);
        critRate = 0.1F;
        presetInfo(s -> createInfo(s, SciPercent(CritHelper.GetChance(cpr()))));
    }
    
    @Override
    public void refreshStats() {
        critRate = 0.1F * stack;
    }
    
    @Override
    public float modifyCritChance(float origin) {
        origin += critRate;
        return super.modifyCritChance(origin);
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