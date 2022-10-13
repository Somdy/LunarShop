package rs.lunarshop.utils.mechanics;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.RegenModifierInterface;
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
        int regen = RegenField.regen.get(target);
        if (target.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof RegenModifierInterface)
                    regen = ((RegenModifierInterface) r).modifyRegen(regen);
            }
        }
        for (AbstractPower p : target.powers) {
            if (p instanceof RegenModifierInterface)
                regen = ((RegenModifierInterface) p).modifyRegen(regen);
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