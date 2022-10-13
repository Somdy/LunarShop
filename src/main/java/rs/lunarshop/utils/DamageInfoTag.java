package rs.lunarshop.utils;

import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.patches.mechanics.DamageInfoField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DamageInfoTag {
    public static final DamageInfoTag FIRE = new DamageInfoTag("LUNAR_FIRE_DAMAGE");
    public static final DamageInfoTag EXPLOSIVE = new DamageInfoTag("LUNAR_EXPLOSIVE_DAMAGE");
    public static final DamageInfoTag ENERGETIC = new DamageInfoTag("LUNAR_ENERGETIC_DAMAGE");
    
    private final String ID;
    
    public DamageInfoTag(String ID) {
        this.ID = ID;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DamageInfoTag that = (DamageInfoTag) o;
        return ID.equals(that.ID);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
    
    public static List<DamageInfoTag> GetInfoTags(DamageInfo info) {
        List<DamageInfoTag> tags = DamageInfoField.infoTags.get(info);
        if (tags == null)
            DamageInfoField.infoTags.set(info, new ArrayList<>());
        return DamageInfoField.infoTags.get(info);
    }
    
    public static void PutTags(DamageInfo info, DamageInfoTag... tags) {
        List<DamageInfoTag> infoTags = GetInfoTags(info);
        if (tags != null && tags.length > 0) {
            for (DamageInfoTag t : tags) {
                if (!infoTags.contains(t))
                    infoTags.add(t);
            }
        }
    }
    
    public static void RemoveTags(DamageInfo info, DamageInfoTag... tags) {
        List<DamageInfoTag> infoTags = GetInfoTags(info);
        if (!infoTags.isEmpty() && tags != null && tags.length > 0) {
            for (DamageInfoTag t : tags) {
                infoTags.remove(t);
            }
        }
    }
    
    public static void ClearTags(DamageInfo info) {
        List<DamageInfoTag> infoTags = GetInfoTags(info);
        infoTags.clear();
    }
    
    public static boolean HasTag(DamageInfo info, DamageInfoTag tag) {
        List<DamageInfoTag> infoTags = GetInfoTags(info);
        return infoTags.contains(tag);
    }
    
    public static boolean HasAnyTag(DamageInfo info, DamageInfoTag... tags) {
        List<DamageInfoTag> infoTags = GetInfoTags(info);
        if (!infoTags.isEmpty() && tags != null && tags.length > 0) {
            for (DamageInfoTag t : tags) {
                if (infoTags.contains(t))
                    return true;
            }
        }
        return false;
    }
}