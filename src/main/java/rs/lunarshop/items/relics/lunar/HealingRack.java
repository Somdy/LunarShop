package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.PotencyHelper;

public class HealingRack extends LunarRelic {
    private float healBuff;
    
    public HealingRack() {
        super(43);
        healBuff = 0.5F;
        presetInfo(s -> createInfo(s, SciPercent(healBuff)));
    }
    
    @Override
    public void refreshStats() {
        healBuff = 0.5F + 0.25F * (stack - 1);
    }
    
    @Override
    public int onPlayerHeal(int healAmount) {
        healAmount += healAmount * healBuff;
        return super.onPlayerHeal(healAmount);
    }
    
    @Override
    public boolean canSpawnForShopping(int shopType) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV4) && super.canSpawnForShopping(shopType);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV4);
    }
}