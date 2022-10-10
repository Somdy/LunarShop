package rs.lunarshop.items.equipments;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.subjects.AbstractLunarEquipment;
import rs.lunarshop.subjects.lunarprops.LunarItemProp;
import rs.lunarshop.utils.MsgHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class EquipmentManager {
    private static final List<AbstractLunarEquipment> equipments = new ArrayList<>();
    
    public static void LoadEquipments() {
        MsgHelper.PreLoad("EQUIPMENT LOADED");
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(ReduceStrength.class)
                .any(LunarEquipment.class, (i, r) -> {
                    equipments.add(r);
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    UnlockTracker.markRelicAsSeen(r.relicId);
                    MsgHelper.Append(r.prop.lunarID);
                });
        MsgHelper.End();
        equipments.sort(Comparator.comparingInt(o -> o.prop.getRarity().tier()));
    }
    
    public static Optional<AbstractLunarEquipment> Get(LunarItemProp itemID) {
        return equipments.stream().filter(r -> r.prop.lunarID == itemID.lunarID).findFirst();
    }
    
    public static Optional<AbstractLunarEquipment> Get(int lunarID) {
        return equipments.stream().filter(r -> r.prop.lunarID == lunarID).findFirst();
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