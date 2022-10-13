package rs.lunarshop.items.relics.planet;

import rs.lunarshop.items.abstracts.PlanetRelic;

public class Pearl extends PlanetRelic {
    public Pearl() {
        super(54);
    }
    
    public Pearl(int stack) {
        super(54, stack);
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