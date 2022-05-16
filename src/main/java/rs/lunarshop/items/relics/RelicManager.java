package rs.lunarshop.items.relics;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.relics.planet.Pearl;
import rs.lunarshop.items.relics.planet.PerfectPearl;
import rs.lunarshop.items.relics.special.DioConsumed;
import rs.lunarshop.items.relics.special.WineAffliction;
import rs.lunarshop.subjects.lunarprops.LunarItemID;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.equipments.EquipmentManager;
import rs.lunarshop.items.relics.lunar.*;
import rs.lunarshop.subjects.AbstractLunarRelic;

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
        
        addLunarRelics();
        addPlanetRelics();
        addSpecialRelics();
        
        LunarPool.sort(Comparator.comparingInt(o -> o.props.getRarity().coins()));
        PlanetPool.sort(Comparator.comparingInt(o -> o.props.getRarity().coins()));
        
        RelicPool.clear();
        RelicPool.addAll(LunarPool);
        RelicPool.addAll(PlanetPool);
        RelicPool.addAll(SpecialPool);
        
        Functions.forEach(r -> {
            BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
            UnlockTracker.markRelicAsSeen(r.relicId);
        });
        
        RelicPool.forEach(r -> {
            BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
            UnlockTracker.markRelicAsSeen(r.relicId);
        });
    }
    
    private static void addLunarRelics() {
        LunarPool.add(new Fealty());
        LunarPool.add(new Crown());
        LunarPool.add(new Corpsebloom());
        LunarPool.add(new LessBossHp());
        LunarPool.add(new DrownedGesture());
        LunarPool.add(new PowerRachis());
        LunarPool.add(new Purity());
        LunarPool.add(new SharpedGlass());
        LunarPool.add(new Beetle());
        LunarPool.add(new Gouge());
        LunarPool.add(new Clover());
        LunarPool.add(new JumpHigher());
        LunarPool.add(new Horn());
        LunarPool.add(new LeechingSeed());
        LunarPool.add(new Planula());
        LunarPool.add(new DeathMark());
        LunarPool.add(new InstantKiller());
        LunarPool.add(new Vase());
        LunarPool.add(new RoseBuckler());
        LunarPool.add(new Shattering());
        LunarPool.add(new ChargedDrill());
        LunarPool.add(new BossBullet());
        LunarPool.add(new Microbots());
        LunarPool.add(new Daisy());
        LunarPool.add(new Scythe());
        LunarPool.add(new GhorTome());
        LunarPool.add(new Infusion());
        LunarPool.add(new BleederDagger());
        LunarPool.add(new Chronobauble());
        LunarPool.add(new TougherTimes());
        LunarPool.add(new Crowbar());
        LunarPool.add(new CeremonialDagger());
        LunarPool.add(new ArmorPlate());
        LunarPool.add(new TheGlasses());
        LunarPool.add(new Medkit());
        LunarPool.add(new Aegis());
        LunarPool.add(new HealingRack());
        LunarPool.add(new Spleen());
        LunarPool.add(new Knurl());
        LunarPool.add(new AlienHead());
        LunarPool.add(new Crystal());
        LunarPool.add(new Syringe());
        LunarPool.add(new HopooFeather());
        LunarPool.add(new DioFriend());
        LunarPool.add(new Slug());
        LunarPool.add(new RustyKey());
        LunarPool.add(new PredatoryMask());
    }
    
    private static void addPlanetRelics() {
        PlanetPool.add(new Pearl());
        PlanetPool.add(new PerfectPearl());
    }
    
    private static void addSpecialRelics() {
        SpecialPool.add(new DioConsumed());
        SpecialPool.add(new WineAffliction());
    }
    
    @NotNull
    public static ArrayList<AbstractRelic> GetLunarItems() {
        ArrayList<AbstractRelic> tmp = new ArrayList<>(GetAllAvailableRelics());
        tmp.addAll(EquipmentManager.GetAllAvailableEquips());
        tmp.removeIf(r -> !(r instanceof LunarRelic) && !(r instanceof LunarEquipment));
        return tmp;
    }
    
    @NotNull
    public static AbstractRelic Get(LunarItemID itemID) {
        return Get(itemID, 0);
    }
    
    @NotNull
    public static AbstractRelic Get(LunarItemID itemID, int extraStack) {
        Optional<AbstractLunarRelic> opt = RelicPool.stream().filter(r -> r.props.lunarID == itemID.lunarID).findFirst();
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
        tmp.removeIf(r -> r.props.getClazz() == LunarClass.SPECIAL);
        return tmp;
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
    
    private static void Warn(Object what) {
        LunarMod.WarnInfo(what);
    }
}