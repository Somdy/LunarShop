package rs.lunarshop.items.relics.planet;

import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.SpecialRelic;

public class Pearl extends SpecialRelic {
    public Pearl() {
        super(ItemID.Pearl);
    }
    
    public Pearl(int stack) {
        super(ItemID.Pearl, stack);
    }
    
    @Override
    protected void onStackAmt(int amt, boolean stacking) {
        if (!stacking) return;
        for (int i = 0; i < amt; i++) {
            cpr().increaseMaxHp(10, true);
        }
    }
    
    @Override
    public void onEquip() {
        cpr().increaseMaxHp(10 * stack, true);
    }
    
    @Override
    public void onUnequip() {
        super.onUnequip();
        cpr().decreaseMaxHealth(stack * 10);
    }
}