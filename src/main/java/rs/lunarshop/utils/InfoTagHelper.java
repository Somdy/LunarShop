package rs.lunarshop.utils;

import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lazymankits.abstracts.DamageInfoTag;
import rs.lazymankits.utils.LMDamageInfoHelper;

import java.util.List;

public class InfoTagHelper {
    public static final DamageInfoTag FIRE = new DamageInfoTag("LUNAR_FIRE_DAMAGE");
    public static final DamageInfoTag EXPLOSIVE = new DamageInfoTag("LUNAR_EXPLOSIVE_DAMAGE");
    public static final DamageInfoTag ENERGETIC = new DamageInfoTag("LUNAR_ENERGETIC_DAMAGE");
    public static final DamageInfoTag TURBINE_IGNORED = new DamageInfoTag("LUNAR_TURBINE_IGNORED");
    
    public static List<DamageInfoTag> GetInfoTags(DamageInfo info) {
        return LMDamageInfoHelper.GetInfoTags(info);
    }
    
    public static void PutTags(DamageInfo info, DamageInfoTag... tags) {
        LMDamageInfoHelper.PutTags(info, tags);
    }
    
    public static void RemoveTags(DamageInfo info, DamageInfoTag... tags) {
        LMDamageInfoHelper.RemoveTags(info, tags);
    }
    
    public static void ClearTags(DamageInfo info) {
        LMDamageInfoHelper.ClearTags(info);
    }
    
    public static boolean HasTag(DamageInfo info, DamageInfoTag tag) {
        return LMDamageInfoHelper.HasTag(info, tag);
    }
    
    public static boolean HasAnyTag(DamageInfo info, DamageInfoTag... tags) {
        return LMDamageInfoHelper.HasAnyTag(info, tags);
    }
}