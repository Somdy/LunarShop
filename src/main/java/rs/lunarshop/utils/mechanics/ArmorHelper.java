package rs.lunarshop.utils.mechanics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.ArmorModifierInterface;
import rs.lunarshop.patches.mechanics.ArmorField;
import rs.lunarshop.patches.mechanics.DamageInfoField;

public class ArmorHelper {
    private static final float BaseArmorFactor = 50F;
    
    public static float RoughDamageMultiplier(AbstractCreature target) {
        float multiplier;
        int armor = GetArmor(target);
        if (armor >= 0) {
            multiplier = (1F - (armor / (armor + BaseArmorFactor)));
        } else {
            multiplier = (2.1F - (BaseArmorFactor / (BaseArmorFactor - armor)));
        }
        return multiplier;
    }
    
    public static float DamageMultiplier(AbstractCreature target, float damageToTake, DamageInfo info) {
        float multiplier;
        int armor = GetArmor(target);
        float efficiency = 1F;
        if (info != null) {
            efficiency = GetArmorEfficiency(info);
        }
        if (armor >= 0) {
            float armorFix = damageToTake <= armor ? BaseArmorFactor :
                    BaseArmorFactor - BaseArmorFactor * Math.min(0.15F, (damageToTake - armor) / 50F);
            float stackFix = armorFix <= armor ? 0 : Math.min(0.95F, 0.2F * ((armor - armorFix) / armorFix));
            float adjustedArmor = armor - armor * stackFix > armorFix ? armor - armor * stackFix : armor;
            adjustedArmor *= efficiency;
            multiplier = (1F - (adjustedArmor / (adjustedArmor + armorFix)));
        } else {
            armor *= (2F - efficiency);
            multiplier = (2.1F - (BaseArmorFactor / (BaseArmorFactor - armor)));
        }
        return multiplier;
    }
    
    public static boolean HasArmor(@NotNull AbstractCreature target) {
        return !target.isDeadOrEscaped() && ArmorField.armor.get(target) != null;
    }
    
    public static float GetArmorEfficiency(DamageInfo info) {
        float efficiency = DamageInfoField.armorEfficiency.get(info);
        if (efficiency < 0)
            efficiency = 0;
        return efficiency;
    }
    
    public static int GetArmor(@NotNull AbstractCreature target) {
        int armor = ArmorField.armor.get(target);
        if (target.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof rs.lunarshop.interfaces.ArmorModifierInterface)
                    armor = ((rs.lunarshop.interfaces.ArmorModifierInterface) r).modifyArmor(armor);
            }
        }
        for (AbstractPower p : target.powers) {
            if (p instanceof ArmorModifierInterface)
                armor = ((ArmorModifierInterface) p).modifyArmor(armor);
        }
        return armor;
    }
    
    public static void SetArmor(@NotNull AbstractCreature target, int armor) {
        ArmorField.armor.set(target, armor);
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
}