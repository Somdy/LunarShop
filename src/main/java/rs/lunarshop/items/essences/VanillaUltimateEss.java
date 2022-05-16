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
import rs.lunarshop.enums.EssCallerType;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.subjects.AbstractCommandEssence;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.PotencyHelper;

import java.util.ArrayList;
import java.util.List;

public class VanillaUltimateEss extends AbstractCommandEssence {
    
    public VanillaUltimateEss() {
        super(LunarMod.Prefix("VanillaUltimateEss"));
        core = Color.RED.cpy();
        glow = Color.SCARLET.cpy();
        init();
    }
    
    @Override
    public void init() {
        super.init();
        List<AbstractRelic> tmp = new ArrayList<>(RelicLibrary.commonList);
        tmp.addAll(RelicLibrary.uncommonList);
        tmp.addAll(RelicLibrary.rareList);
        tmp.addAll(RelicLibrary.specialList);
        tmp.addAll(RelicLibrary.bossList);
        tmp.addAll(RelicLibrary.shopList);
        distinctList(tmp, r -> {
            boolean has = LMSK.Player().hasRelic(r.relicId) && !AchvHelper.IsAchvUnlocked(AchvID.LunarBandit);
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
        return LunarRarity.MYTHIC.coins() * 5;
    }
    
    @Override
    protected void updateTips(List<PowerTip> tips) {
        tips.add(new PowerTip(TEXT[0], TEXT[1]));
    }
    
    @Override
    public boolean canSpawn(EssCallerType type) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1);
    }
}