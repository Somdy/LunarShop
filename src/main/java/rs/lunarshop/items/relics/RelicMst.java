package rs.lunarshop.items.relics;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.abstracts.AbstractLunarEquipment;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.items.abstracts.*;
import rs.lunarshop.items.equipments.EquipmentManager;
import rs.lunarshop.items.relics.legacy.FiremanBoots;
import rs.lunarshop.items.relics.lunar.Fealty;
import rs.lunarshop.items.relics.planet.Pearl;
import rs.lunarshop.items.relics.special.DioConsumed;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.abstracts.lunarprops.LunarItemProp;
import rs.lunarshop.utils.MsgHelper;

import java.util.*;
import java.util.stream.Collectors;

public final class RelicMst {
    public static final Comparator<AbstractLunarRelic> ITEM_COMPARATOR = new ItemComparator();
    private static final List<AbstractRelic> Functions = new ArrayList<>();
    
    private static final List<Integer> LunarPool = new ArrayList<>();
    private static final List<Integer> LegacyPool = new ArrayList<>();
    private static final List<Integer> PlanetPool = new ArrayList<>();
    private static final List<Integer> SpecialPool = new ArrayList<>();
    
    private static final Map<LunarItemProp, AbstractLunarRelic> ITEM_MAP = new HashMap<>();
    private static final Map<Integer, LunarItemProp> ITEM_PROP_MAP = new HashMap<>();
    
    public static void LoadRelics() {
        Functions.add(0, new LunarPass());
        Functions.add(1, CETemplate.Get());
        Functions.forEach(r -> {
            BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
            UnlockTracker.markRelicAsSeen(r.relicId);
        });
    
        MsgHelper.PreLoad("RELIC LOADED");
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(Fealty.class)
                .any(LunarRelic.class, (i, r) -> {
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    addRelic(r, LunarPool);
                });
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(Pearl.class)
                .any(PlanetRelic.class, (i, r) -> {
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    addRelic(r, PlanetPool);
                });
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(FiremanBoots.class)
                .any(LegacyRelic.class, (i, r) -> {
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    addRelic(r, LegacyPool);
                });
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(DioConsumed.class)
                .any(SpecialRelic.class, (i, r) -> {
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    addRelic(r, SpecialPool);
                });
        MsgHelper.End();
    }
    
    private static void addRelic(AbstractLunarRelic r, List<Integer> pool) {
        ITEM_MAP.put(r.prop, r);
        ITEM_PROP_MAP.put(r.prop.lunarID, r.prop);
        pool.add(r.prop.lunarID);
        MsgHelper.Append(r.prop.lunarID);
        if (LunarMod.DevMode) UnlockTracker.markRelicAsSeen(r.relicId);
    }
    
    @NotNull
    public static ArrayList<? extends AbstractRelic> GetLunarItems() {
        ArrayList<AbstractLunarRelic> tmp = new ArrayList<>(GetAllAvailableRelics());
        tmp.addAll(EquipmentManager.GetAllAvailableEquips());
        tmp.removeIf(r -> !(r instanceof LunarRelic) && !(r instanceof LunarEquipment));
        tmp.sort(ITEM_COMPARATOR);
        return tmp;
    }
    
    public static ArrayList<? extends AbstractRelic> GetPlanetItems() {
        ArrayList<AbstractLunarRelic> tmp = new ArrayList<>(GetAllAvailableRelics());
        tmp.removeIf(r -> !(r instanceof PlanetRelic));
        tmp.sort(ITEM_COMPARATOR);
        return tmp;
    }
    
    public static ArrayList<? extends AbstractRelic> GetLegacyItems() {
        ArrayList<AbstractLunarRelic> tmp = new ArrayList<>(GetAllAvailableRelics());
        tmp.removeIf(r -> !(r instanceof LegacyRelic));
        tmp.sort(ITEM_COMPARATOR);
        return tmp;
    }
    
    @NotNull
    public static AbstractRelic Get(LunarItemProp itemID) {
        return Get(itemID, 0);
    }
    
    @NotNull
    public static AbstractRelic Get(LunarItemProp itemID, int extraStack) {
        AbstractLunarRelic r = ITEM_MAP.get(itemID);
        if (r != null) {
            AbstractLunarRelic retVal = (AbstractLunarRelic) r.makeCopy();
            retVal.stackAmt(extraStack, false);
            return retVal;
        }
        Warn("Unable to find item: " + itemID.lunarID);
        return new Circlet();
    }
    
    public static LunarPass GetPass() {
        return (LunarPass) Functions.get(0).makeCopy();
    }
    
    @NotNull
    public static List<AbstractLunarRelic> GetAllAvailableRelics() {
        List<AbstractLunarRelic> tmp = GetAllRelics();
        tmp.removeIf(r -> r.prop.getClazz() == LunarClass.SPECIAL);
        return tmp;
    }
    
    @NotNull
    public static List<Integer> GetAllRelicLunarID() {
        return new ArrayList<>(ITEM_PROP_MAP.keySet());
    }
    
    @NotNull
    public static List<AbstractLunarRelic> GetAllRelics() {
        return ITEM_MAP.values().stream().map(r -> ((AbstractLunarRelic) r.makeCopy())).collect(Collectors.toList());
    }
    
    public static boolean IsLunarItem(AbstractRelic r) {
        return r instanceof LunarRelic || r instanceof PlanetRelic || r instanceof LegacyRelic || r instanceof AbstractLunarEquipment;
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
    
    private static void Warn(Object what) {
        LunarMod.WarnInfo(what);
    }
    
    private static class ItemComparator implements Comparator<AbstractLunarRelic> {
        @Override
        public int compare(AbstractLunarRelic o1, AbstractLunarRelic o2) {
            int res = o1.prop.getTier() - o2.prop.getTier();
            return res != 0 ? res : o1.name.compareTo(o2.name);
        }
    }
}