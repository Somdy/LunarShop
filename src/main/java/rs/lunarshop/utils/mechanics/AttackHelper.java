package rs.lunarshop.utils.mechanics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.AttackModifierInterface;
import rs.lunarshop.patches.mechanics.AttackField;

public class AttackHelper {
    
    public static float ApplyPowersToMonster(AbstractCreature m, float damage) {
        int attack = GetAttack(m);
        if (attack >= 0) {
            if (damage < 0) 
                damage = attack;
            else 
                damage += attack;
        } else {
            damage -= Math.abs(attack);
        }
        return damage;
    }
    
    public static float ApplyPowersToCard(AbstractPlayer p, float damage) {
        int attack = GetAttack(p);
        if (attack >= 0) {
            if (damage < 0)
                damage = attack;
            else
                damage += attack;
        } else {
            damage -= Math.abs(attack);
        }
        return damage;
    }
    
    public static boolean HasAttack(@NotNull AbstractCreature target) {
        return !target.isDeadOrEscaped() && AttackField.attack.get(target) != null;
    }
    
    public static int GetAttack(@NotNull AbstractCreature target) {
        int attack = AttackField.attack.get(target);
        if (target.isPlayer) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AttackModifierInterface)
                    attack = ((AttackModifierInterface) r).modifyAttack(attack);
            }
        }
        for (AbstractPower p : target.powers) {
            if (p instanceof AttackModifierInterface)
                attack = ((AttackModifierInterface) p).modifyAttack(attack);
        }
        return attack;
    }
    
    public static void SetAttack(@NotNull AbstractCreature target, int attack) {
        AttackField.attack.set(target, attack);
    }
}