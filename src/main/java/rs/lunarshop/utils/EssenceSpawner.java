package rs.lunarshop.utils;

import com.megacrit.cardcrawl.random.Random;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.EssenceCaller;
import rs.lunarshop.items.essences.*;
import rs.lunarshop.abstracts.AbstractCommandEssence;

import java.util.ArrayList;
import java.util.List;

public class EssenceSpawner {
    
    @NotNull
    public static AbstractCommandEssence GetRndLunarEssence(Random rng, EssenceCaller caller) {
        List<AbstractCommandEssence> tmp = new ArrayList<>();
        tmp.add(new LunarUncommonEss());
        tmp.add(new LunarRareEss());
        tmp.add(new LunarLegendEss());
        tmp.removeIf(e -> !e.canSpawn(caller.getType()));
        return LMSK.GetRandom(tmp, rng).get().setCaller(caller);
    }
    
    @NotNull
    public static AbstractCommandEssence GetRndVanillaEssence(Random rng, EssenceCaller caller) {
        List<AbstractCommandEssence> tmp = new ArrayList<>();
        tmp.add(new VanillaCommonEss());
        tmp.add(new VanillaUncommonEss());
        tmp.add(new VanillaSpecialEss());
        tmp.add(new VanillaUltimateEss());
        tmp.removeIf(e -> !e.canSpawn(caller.getType()));
        return LMSK.GetRandom(tmp, rng).get().setCaller(caller);
    }
}