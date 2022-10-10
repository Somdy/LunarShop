package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.items.abstracts.LunarRelic;

public class Steak extends LunarRelic {
    private static final int HP_BUFF = 2;
    
    public Steak() {
        super(65);
        counter = HP_BUFF;
    }
    
    @Override
    public void refreshStats() {
        counter = stack * HP_BUFF;
    }
    
    @Override
    protected void onStackAmt(int amt, boolean stacking) {
        if (!stacking) return;
        for (int i = 0; i < amt; i++) {
            cpr().increaseMaxHp(HP_BUFF, true);
        }
    }
    
    @Override
    public void onEquip() {
        cpr().increaseMaxHp(HP_BUFF * stack, true);
    }
    
    @Override
    public void onUnequip() {
        cpr().decreaseMaxHealth(HP_BUFF * stack);
    }
}
