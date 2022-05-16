package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.PotencyHelper;

public class Knurl extends LunarRelic {
    private int hp;
    private int regen;
    
    public Knurl() {
        super(ItemID.Knurl);
        regen = 1;
    }
    
    @Override
    public void refreshStats() {
        hp = 20 * stack;
        regen = stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], hp, regen);
    }
    
    @Override
    protected void onStackAmt(int amt, boolean stacking) {
        if (!stacking) return;
        for (int i = 0; i < amt; i++) {
            cpr().increaseMaxHp(20, true);
        }
    }
    
    @Override
    public void onEquip() {
        super.onEquip();
        cpr().increaseMaxHp(20 * stack, true);
    }
    
    @Override
    public void onUnequip() {
        super.onUnequip();
        int loss = 20 * stack;
        if (loss > cpr().maxHealth)
            loss = cpr().maxHealth - 1;
        cpr().decreaseMaxHealth(loss);
    }
    
    @Override
    public int modifyRegen(int origin) {
        return origin + regen;
    }
    
    @Override
    public boolean canSpawnForShopping(int shopType) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV6) && super.canSpawnForShopping(shopType);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV6);
    }
}