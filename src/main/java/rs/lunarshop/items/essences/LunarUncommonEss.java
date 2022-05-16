package rs.lunarshop.items.essences;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.PowerTip;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.EssCallerType;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.subjects.AbstractCommandEssence;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.ItemSpawner;

import java.util.List;

public class LunarUncommonEss extends AbstractCommandEssence {
    
    public LunarUncommonEss() {
        super(LunarMod.Prefix("LunarUncommonEss"));
        core = Color.LIME.cpy();
        glow = Color.GREEN.cpy();
        init();
    }
    
    @Override
    public void init() {
        super.init();
        List<AbstractLunarRelic> tmp = ItemSpawner.PopulateLimitedRelicLists(r -> !r.props.getRarity().above(LunarRarity.UNCOMMON));
        relics.clear();
        tmp.forEach(r -> relics.add(r.makeCopy()));
    }
    
    @Override
    public int getPrice() {
        return LunarRarity.UNCOMMON.coins() * 3;
    }
    
    @Override
    protected void updateTips(List<PowerTip> tips) {
        tips.add(new PowerTip(TEXT[0], TEXT[1]));
    }
    
    @Override
    public boolean canSpawn(EssCallerType type) {
        return type == EssCallerType.LunarShop;
    }
}