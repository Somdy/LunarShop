package rs.lunarshop.items.essences;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.abstracts.AbstractCommandEssence;
import rs.lunarshop.utils.AchvHelper;

import java.util.ArrayList;
import java.util.List;

public class VanillaUncommonEss extends AbstractCommandEssence {
    
    public VanillaUncommonEss() {
        super(LunarMod.Prefix("VanillaUncommonEss"));
        core = Color.SKY.cpy();
        glow = Color.BLUE.cpy();
        init();
    }
    
    @Override
    public void init() {
        super.init();
        List<AbstractRelic> tmp = new ArrayList<>(RelicLibrary.uncommonList);
        distinctList(tmp, r -> {
            boolean has = LMSK.Player().hasRelic(r.relicId) && !AchvHelper.IsAchvUnlocked(AchvID.RichInCoins);
            boolean modded = r instanceof CustomRelic;
            return has || modded;
        });
        if (tmp.isEmpty())
            tmp.add(new Circlet());
        relics.clear();
        tmp.forEach(r -> relics.add(r.makeCopy()));
    }
    
    @Override
    public int getPrice() {
        return LunarRarity.UNCOMMON.coins() * 2;
    }
    
    @Override
    protected void updateTips(List<PowerTip> tips) {
        tips.add(new PowerTip(TEXT[0], TEXT[1]));
    }
}