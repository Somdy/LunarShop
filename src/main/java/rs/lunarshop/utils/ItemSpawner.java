package rs.lunarshop.utils;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.items.equipments.EquipmentManager;
import rs.lunarshop.items.relics.RelicManager;
import rs.lunarshop.items.relics.lunar.RustyKey;
import rs.lunarshop.subjects.AbstractLunarRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ItemSpawner {
    
    @NotNull
    public static AbstractLunarRelic ReturnRndExptItem(Random rng, Predicate<AbstractLunarRelic> expt) {
        List<AbstractLunarRelic> tmp = RelicManager.GetAllAvailableRelics();
        tmp.addAll(EquipmentManager.GetAllAvailableEquips());
        tmp.removeIf(r -> !r.canSpawnInReward());
        tmp.removeIf(r -> !expt.test(r));
        if (tmp.isEmpty()) {
            LunarMod.LogInfo("No relic can spawn at the given condition");
            tmp.clear();
            tmp.add(new RustyKey());
        }
        return LMSK.GetRandom(tmp, rng).get();
    }
    
    @NotNull
    public static List<AbstractLunarRelic> PopulateLimitedRelicLists(Predicate<AbstractLunarRelic> expt) {
        List<AbstractLunarRelic> tmp = RelicManager.GetAllAvailableRelics();
        tmp.removeIf(r -> !r.canSpawnInReward());
        tmp.removeIf(r -> !expt.test(r));
        if (tmp.isEmpty()) {
            LunarMod.LogInfo("No relics can spawn at the limitations");
        }
        return tmp;
    }
    
    public static Optional<AbstractLunarRelic> PopulateRndRelicForShop(int shopType) {
        List<AbstractLunarRelic> tmp = RelicManager.GetAllAvailableRelics();
        tmp.addAll(EquipmentManager.GetAllAvailableEquips());
        tmp.removeIf(r -> !r.canSpawnInShop(shopType));
        if (tmp.isEmpty()) {
            LunarMod.LogInfo("No relics can spawn for shop: " + shopType);
            return Optional.empty();
        }
        Collections.shuffle(tmp, LMSK.RelicRng().random);
        return LMSK.GetRandom(tmp, AbstractDungeon.merchantRng);
    }
    
    @NotNull
    public static List<AbstractLunarRelic> PopulateRndRelicListForShop(int shopType, int amount) {
        List<AbstractLunarRelic> tmp = RelicManager.GetAllAvailableRelics();
        tmp.addAll(EquipmentManager.GetAllAvailableEquips());
        tmp.removeIf(r -> !r.canSpawnInShop(shopType));
        if (tmp.isEmpty()) {
            LunarMod.LogInfo("No relics can spawn for shop: " + shopType);
        }
        Collections.shuffle(tmp, LMSK.RelicRng().random);
        List<AbstractLunarRelic> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Optional<AbstractLunarRelic> opt = LMSK.GetRandom(tmp, AbstractDungeon.merchantRng);
            opt.ifPresent(list::add);
        }
        return list;
    }
    
    public static Optional<AbstractLunarRelic> PopulateLimitedRndRelicForReward(LunarRarity maxRarity) {
        List<AbstractLunarRelic> tmp = RelicManager.GetAllAvailableRelics();
        tmp.removeIf(r -> !r.canSpawnInReward());
        tmp.removeIf(r -> r.prop.getRarity().above(maxRarity));
        if (tmp.isEmpty()) {
            LunarMod.LogInfo("No relics can spawn below the rarity of " + maxRarity.toString());
            return Optional.empty();
        }
        Collections.shuffle(tmp, LMSK.RelicRng().random);
        return LMSK.GetRandom(tmp, LMSK.RelicRng());
    }
}