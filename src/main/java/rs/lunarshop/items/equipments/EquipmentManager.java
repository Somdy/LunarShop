package rs.lunarshop.items.equipments;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.subjects.lunarprops.LunarItemID;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarEquipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class EquipmentManager {
    private static final List<AbstractLunarEquipment> equipments = new ArrayList<>();
    
    public static void LoadEquipments() {
        addLunarEquipments();
    
        List<AbstractLunarEquipment> tmp = new ArrayList<>(equipments);
        
        tmp.forEach(e -> {
            BaseMod.addRelic(e.makeCopy(), RelicType.SHARED);
            UnlockTracker.markRelicAsSeen(e.relicId);
        });
        
        tmp.clear();
    }
    
    private static void addLunarEquipments() {
        equipments.add(new ReduceStrength());
        equipments.add(new Helfire());
        equipments.add(new FakeWine());
        equipments.add(new CrowdFunder());
        equipments.add(new JadeElephant());
        equipments.add(new Opus());
        // TODO: Actual effects
//        equipments.add(new Meteorite());
    }
    
    public static Optional<AbstractLunarEquipment> Get(LunarItemID itemID) {
        return equipments.stream().filter(r -> r.props.lunarID == itemID.lunarID).findFirst();
    }
    
    public static Optional<AbstractLunarEquipment> Get(int lunarID) {
        return equipments.stream().filter(r -> r.props.lunarID == lunarID).findFirst();
    }
    
    public static Optional<AbstractLunarEquipment> GetExpt(Predicate<AbstractLunarEquipment> expt) {
        return equipments.stream().filter(expt).findFirst();
    }
    
    @NotNull
    public static List<AbstractLunarEquipment> GetAllAvailableEquips() {
        List<AbstractLunarEquipment> tmp = new ArrayList<>();
        equipments.forEach(r -> tmp.add((AbstractLunarEquipment) r.makeCopy()));
        return tmp;
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
    
    private static void Warn(Object what) {
        LunarMod.WarnInfo(what);
    }
}