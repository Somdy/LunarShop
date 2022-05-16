package rs.lunarshop.utils;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.powers.RegenModifierPower;
import rs.lunarshop.patches.mechanics.RegenField;

public class RegenHelper {
    
    public static void PublishRegen(AbstractCreature target) {
        int regen = GetRegen(target);
        target.heal(regen, true);
        target.healthBarUpdatedEvent();
    }
    
    public static boolean HasRegen(@NotNull AbstractCreature target) {
        return !target.isDeadOrEscaped() && RegenField.regen.get(target) != null;
    }
    
    public static int GetRegen(@NotNull AbstractCreature target) {
        if (target.isPlayer) {
            return LunarMaster.Regen(true);
        }
        int regen = RegenField.regen.get(target);
        for (AbstractPower p : target.powers) {
            if (p instanceof RegenModifierPower)
                regen = ((RegenModifierPower) p).modifyRegen(regen);
        }
        return regen;
    }
    
    public static void SetRegen(@NotNull AbstractCreature target, int regen) {
        if (target.isPlayer) return;
        RegenField.regen.set(target, regen);
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
}