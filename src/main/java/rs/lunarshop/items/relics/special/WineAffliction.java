package rs.lunarshop.items.relics.special;

import rs.lunarshop.items.abstracts.SpecialRelic;
import rs.lunarshop.powers.TonicPower;

public class WineAffliction extends SpecialRelic {
    private int debuff;
    
    public WineAffliction() {
        super(58);
        debuff = 3;
    }
    
    @Override
    public void refreshStats() {
        debuff = 3 * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], debuff / 3);
    }
    
    private boolean notInTonic() {
        return cpr().powers.isEmpty() || !cpr().hasPower(TonicPower.POWER_ID);
    }
    
    @Override
    public int modifyArmor(int origin) {
        if (notInTonic()) origin -= debuff;
        return super.modifyArmor(origin);
    }
    
    @Override
    public int modifyRegen(int origin) {
        if (notInTonic()) {
            if (origin > 0) {
                if (origin - debuff >= 0)
                    origin -= debuff;
                else origin = 0;
            }
        }
        return super.modifyRegen(origin);
    }
    
    @Override
    public int modifyAttack(int origin) {
        if (notInTonic()) origin -= debuff;
        return super.modifyAttack(origin);
    }
}