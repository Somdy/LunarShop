package rs.lunarshop.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.subjects.lunarprops.LunarNpcProp;
import rs.lunarshop.utils.ArmorHelper;
import rs.lunarshop.utils.AttackHelper;
import rs.lunarshop.utils.NpcHelper;
import rs.lunarshop.utils.RegenHelper;

@SpirePatch2(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR, 
        paramtypez = {String.class, String.class, int.class, float.class, float.class, float.class, float.class, 
                String.class, float.class, float.class, boolean.class})
public class MonsterStatsPatch {
    @SpirePostfixPatch
    public static void Postfix(AbstractMonster __instance) {
        LunarNpcProp prop = NpcHelper.GetProp(__instance.id);
        if (prop != null) {
            ArmorHelper.SetArmor(__instance, prop.getArmor());
            AttackHelper.SetAttack(__instance, prop.getAttack());
            RegenHelper.SetRegen(__instance, prop.getRegen());
        }
    }
}