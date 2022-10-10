package rs.lunarshop.utils;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.powers.ArmorModifierPower;
import rs.lunarshop.patches.mechanics.ArmorField;

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
    
    public static float DamageMultiplier(AbstractCreature target, float damageToTake) {
        float multiplier;
        int armor = GetArmor(target);
        if (armor >= 0) {
            float armorFix = damageToTake <= armor ? BaseArmorFactor :
                    BaseArmorFactor - BaseArmorFactor * Math.min(0.15F, (damageToTake - armor) / 50F);
            float stackFix = armorFix <= armor ? 0 : Math.min(0.95F, 0.2F * ((armor - armorFix) / armorFix));
            float adjustedArmor = armor - armor * stackFix > armorFix ? armor - armor * stackFix : armor;
            multiplier = (1F - (adjustedArmor / (adjustedArmor + armorFix)));
        } else {
            multiplier = (2.1F - (BaseArmorFactor / (BaseArmorFactor - armor)));
        }
        return multiplier;
    }
    
    public static boolean HasArmor(@NotNull AbstractCreature target) {
        return !target.isDeadOrEscaped() && ArmorField.armor.get(target) != null;
    }
    
    public static int GetArmor(@NotNull AbstractCreature target) {
        if (target.isPlayer) {
            return LunarMaster.Armor(true);
        }
        int armor = ArmorField.armor.get(target);
        for (AbstractPower p : target.powers) {
            if (p instanceof ArmorModifierPower)
                armor = ((ArmorModifierPower) p).modifyArmor(armor);
        }
        return armor;
    }
    
    public static void SetArmor(@NotNull AbstractCreature target, int armor) {
        if (target.isPlayer) return;
        ArmorField.armor.set(target, armor);
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
}