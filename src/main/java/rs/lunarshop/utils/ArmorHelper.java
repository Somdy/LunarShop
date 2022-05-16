package rs.lunarshop.utils;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.powers.ArmorModifierPower;
import rs.lunarshop.patches.mechanics.ArmorField;

public class ArmorHelper {
    private static final float ArmorFactor = 50F;
    
    public static float DamageMultiplier(AbstractCreature target) {
        float multiplier;
        int armor = GetArmor(target);
        if (armor >= 0) {
            multiplier = (1 - (armor / (armor + ArmorFactor)));
        } else {
            multiplier = (2 - (ArmorFactor / (ArmorFactor - armor)));
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