package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.subjects.AbstractLunarEquipment;

public class DrownedGesture extends LunarRelic {
    private float coolMult;
    
    public DrownedGesture() {
        super(4);
        coolMult = 0.7F;
    }
    
    @Override
    public void refreshStats() {
        coolMult = (float) (0.7F * Math.pow((1 - 0.1F), stack - 1));
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(1 - coolMult));
    }
    
    @Override
    public int onEqmtStartCooldown(AbstractLunarEquipment equipment, int cooldown) {
        cooldown = MathUtils.ceil(cooldown * coolMult);
        if (!equipment.isUseAutoActivate())
            equipment.setUseAutoActivate(true);
        return super.onEqmtStartCooldown(equipment, cooldown);
    }
}