package rs.lunarshop.items.relics.legacy;

import rs.lunarshop.items.abstracts.LegacyRelic;

public class EnergyCell extends LegacyRelic {
    private static final int BASE_MOD = 3;
    private static final int MOD_PER_STACK = 1;
    private int mod;
    private boolean damaged;
    
    public EnergyCell() {
        super(88);
        mod = BASE_MOD;
    }
    
    @Override
    public void refreshStats() {
        mod = BASE_MOD + (stack - 1) * MOD_PER_STACK;
    }
    
    @Override
    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && !damaged) {
            damaged = true;
            beginLongPulse();
        }
    }
    
    @Override
    public void onVictory() {
        damaged = false;
        stopPulse();
    }
    
    @Override
    public void atPreBattle() {
        damaged = false;
        stopPulse();
    }
    
    @Override
    public int modifyAttack(int origin) {
        if (damaged) origin += mod;
        return super.modifyAttack(origin);
    }
    
    @Override
    public int modifyRegen(int origin) {
        if (damaged) origin += mod;
        return super.modifyRegen(origin);
    }
}