package rs.lunarshop.utils.mechanics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.CritModifierInterface;
import rs.lunarshop.patches.mechanics.CritField;
import rs.lunarshop.patches.mechanics.DamageInfoField;

@SuppressWarnings("unused")
public class CritHelper {
    public static final float BASE_CRITICAL_MULT = 1.5F;
    
    public static boolean CanCrit(@NotNull DamageInfo info) {
        return info.type == DamageInfo.DamageType.NORMAL && info.owner != null && !info.owner.isDeadOrEscaped()
                && CritField.critChance.get(info.owner) != null;
    }
    
    public static void SetInfoCritical(DamageInfo info, int critHitDamage) {
        DamageInfoField.critical.set(info, true);
        info.output = critHitDamage;
        info.isModified = true;
    }
    
    public static float GetChance(@NotNull AbstractCreature source) {
        float chance = CritField.critChance.get(source);
        if (source.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof CritModifierInterface)
                    chance = ((CritModifierInterface) r).modifyCritChance(chance);
            }
        }
        for (AbstractPower p : source.powers) {
            if (p instanceof CritModifierInterface)
                chance = ((CritModifierInterface) p).modifyCritChance(chance);
        }
        return chance;
    }
    
    public static float GetCalculatedChance(@NotNull AbstractCreature source, AbstractCreature hitTarget) {
        float chance = CritField.critChance.get(source);
        if (source.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof CritModifierInterface)
                    chance = ((CritModifierInterface) r).modifyCritChance(chance, hitTarget);
            }
        }
        for (AbstractPower p : source.powers) {
            if (p instanceof CritModifierInterface)
                chance = ((CritModifierInterface) p).modifyCritChance(chance, hitTarget);
        }
        return chance;
    }
    
    public static float GetMult(@NotNull AbstractCreature source) {
        float mult = CritField.critMult.get(source);
        if (source.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof CritModifierInterface)
                    mult = ((CritModifierInterface) r).modifyCritMult(mult);
            }
        }
        for (AbstractPower p : source.powers) {
            if (p instanceof CritModifierInterface)
                mult = ((CritModifierInterface) p).modifyCritMult(mult);
        }
        if (mult < 1F) mult = 1F;
        return mult;
    }
    
    public static float GetCalculatedMult(@NotNull AbstractCreature source, AbstractCreature hitTarget) {
        float mult = CritField.critMult.get(source);
        if (source.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof CritModifierInterface)
                    mult = ((CritModifierInterface) r).modifyCritMult(mult, hitTarget);
            }
        }
        for (AbstractPower p : source.powers) {
            if (p instanceof CritModifierInterface)
                mult = ((CritModifierInterface) p).modifyCritMult(mult, hitTarget);
        }
        if (mult < 1F) mult = 1F;
        return mult;
    }
    
    public static boolean IsCritical(@NotNull DamageInfo info) {
        return DamageInfoField.critical.get(info) != null && DamageInfoField.critical.get(info);
    }
    
    public static void SetCritChance(AbstractCreature target, float chance) {
        CritField.critChance.set(target, chance);
    }
    
    public static void SetCritMult(AbstractCreature target, float mult) {
        CritField.critMult.set(target, mult);
    }
}