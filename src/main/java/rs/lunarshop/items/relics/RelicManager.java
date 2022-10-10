package rs.lunarshop.items.relics;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.abstracts.PlanetRelic;
import rs.lunarshop.items.abstracts.SpecialRelic;
import rs.lunarshop.items.equipments.EquipmentManager;
import rs.lunarshop.items.relics.lunar.Fealty;
import rs.lunarshop.items.relics.planet.Pearl;
import rs.lunarshop.items.relics.special.DioConsumed;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.subjects.lunarprops.LunarItemProp;
import rs.lunarshop.utils.MsgHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class RelicManager {
    private static final List<AbstractRelic> Functions = new ArrayList<>();
    
    private static final List<AbstractLunarRelic> LunarPool = new ArrayList<>();
    private static final List<AbstractLunarRelic> PlanetPool = new ArrayList<>();
    private static final List<AbstractLunarRelic> SpecialPool = new ArrayList<>();
    
    private static final List<AbstractLunarRelic> RelicPool = new ArrayList<>();
    
    public static void LoadRelics() {
        Functions.add(0, new LunarPass());
        Functions.add(1, CETemplate.Get());
        Functions.forEach(r -> {
            BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
            UnlockTracker.markRelicAsSeen(r.relicId);
        });
    
        MsgHelper.PreLoad("RELIC LOADED");
        addLunarRelics();
        addPlanetRelics();
        addSpecialRelics();
        MsgHelper.End();
        
        LunarPool.sort(Comparator.comparingInt(o -> o.prop.getRarity().tier()));
        PlanetPool.sort(Comparator.comparingInt(o -> o.prop.getRarity().tier()));
        
        RelicPool.clear();
        RelicPool.addAll(LunarPool);
        RelicPool.addAll(PlanetPool);
        RelicPool.addAll(SpecialPool);
    }
    
    private static void addLunarRelics() {
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(Fealty.class)
                .any(LunarRelic.class, (i, r) -> {
                    LunarPool.add(r);
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    UnlockTracker.markRelicAsSeen(r.relicId);
                    MsgHelper.Append(r.prop.lunarID);
                });
    }
    
    private static void addPlanetRelics() {
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(Pearl.class)
                .any(PlanetRelic.class, (i, r) -> {
                    PlanetPool.add(r);
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    UnlockTracker.markRelicAsSeen(r.relicId);
                    MsgHelper.Append(r.prop.lunarID);
                });
    }
    
    private static void addSpecialRelics() {
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(DioConsumed.class)
                .any(SpecialRelic.class, (i, r) -> {
                    SpecialPool.add(r);
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    UnlockTracker.markRelicAsSeen(r.relicId);
                    MsgHelper.Append(r.prop.lunarID);
                });
    }
    
    @NotNull
    public static ArrayList<AbstractRelic> GetLunarItems() {
        ArrayList<AbstractRelic> tmp = new ArrayList<>(GetAllAvailableRelics());
        tmp.addAll(EquipmentManager.GetAllAvailableEquips());
        tmp.removeIf(r -> !(r instanceof LunarRelic) && !(r instanceof LunarEquipment));
        return tmp;
    }
    
    @NotNull
    public static AbstractRelic Get(LunarItemProp itemID) {
        return Get(itemID, 0);
    }
    
    @NotNull
    public static AbstractRelic Get(LunarItemProp itemID, int extraStack) {
        Optional<AbstractLunarRelic> opt = RelicPool.stream().filter(r -> r.prop.lunarID == itemID.lunarID).findFirst();
        if (opt.isPresent()) {
            AbstractLunarRelic r = (AbstractLunarRelic) opt.get().makeCopy();
            r.stackAmt(extraStack, false);
            return r;
        }
        Warn("Unable to find item: " + itemID.lunarID);
        return new Circlet();
    }
    
    public static LunarPass GetPass() {
        return (LunarPass) Functions.get(0).makeCopy();
    }
    
    @NotNull
    public static List<AbstractLunarRelic> GetAllAvailableRelics() {
        List<AbstractLunarRelic> tmp = new ArrayList<>();
        RelicPool.forEach(r -> tmp.add((AbstractLunarRelic) r.makeCopy()));
        tmp.removeIf(r -> r.prop.getClazz() == LunarClass.SPECIAL);
        return tmp;
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
    
    private static void Warn(Object what) {
        LunarMod.WarnInfo(what);
    }
}