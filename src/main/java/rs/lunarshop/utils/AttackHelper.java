package rs.lunarshop.utils;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.interfaces.powers.AttackModifierPower;
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
        if (target.isPlayer) {
            return LunarMaster.Attack(true);
        }
        int attack = AttackField.attack.get(target);
        for (AbstractPower p : target.powers) {
            if (p instanceof AttackModifierPower)
                attack = ((AttackModifierPower) p).modifyAttack(attack);
        }
        return attack;
    }
    
    public static void SetAttack(@NotNull AbstractCreature target, int attack) {
        if (target.isPlayer) return;
        AttackField.attack.set(target, attack);
    }
}