package rs.lunarshop.items.relics.planet;

import com.badlogic.gdx.math.MathUtils;
import rs.lunarshop.items.abstracts.PlanetRelic;

public class PerfectPearl extends PlanetRelic {
    private int baseAmt;
    
    public PerfectPearl() {
        super(55);
        popupTierBg = 3;
        baseAmt = 2;
    }
    
    public PerfectPearl(int stack) {
        super(55, stack);
        baseAmt = 2 + MathUtils.ceil(2 * 1.2F * (stack - 1));
    }
    
    @Override
    public void refreshStats() {
        baseAmt = 2 + MathUtils.ceil(2 * 1.2F * (stack - 1));
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
        cpr().decreaseMaxHealth(stack * 10);
    }
    
    @Override
    public int modifyArmor(int origin) {
        return origin + baseAmt;
    }
    
    @Override
    public int modifyAttack(int origin) {
        return origin + baseAmt;
    }
    
    @Override
    public int modifyRegen(int origin) {
        return origin + baseAmt;
    }
}