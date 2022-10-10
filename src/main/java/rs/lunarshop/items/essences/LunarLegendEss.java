package rs.lunarshop.items.essences;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.PowerTip;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.EssCallerType;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.subjects.AbstractCommandEssence;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.ItemSpawner;
import rs.lunarshop.utils.PotencyHelper;

import java.util.List;

public class LunarLegendEss extends AbstractCommandEssence {
    
    public LunarLegendEss() {
        super(LunarMod.Prefix("LunarLegendEss"));
        core = Color.RED.cpy();
        glow = Color.FIREBRICK.cpy();
        init();
    }
    
    @Override
    public void init() {
        super.init();
        List<AbstractLunarRelic> tmp = ItemSpawner.PopulateLimitedRelicLists(r -> !r.prop.getRarity().above(LunarRarity.LEGEND));
        relics.clear();
        tmp.forEach(r -> relics.add(r.makeCopy()));
    }
    
    @Override
    public int getPrice() {
        return LunarRarity.LEGEND.coins() * 5;
    }
    
    @Override
    protected void updateTips(List<PowerTip> tips) {
        tips.add(new PowerTip(TEXT[0], TEXT[1]));
    }
    
    @Override
    public boolean canSpawn(EssCallerType type) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV2);
    }
}