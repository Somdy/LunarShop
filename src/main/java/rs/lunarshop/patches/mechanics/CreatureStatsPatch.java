package rs.lunarshop.patches.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.data.DifficultyMod;
import rs.lunarshop.abstracts.lunarprops.LunarNpcProp;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.utils.mechanics.ArmorHelper;
import rs.lunarshop.utils.mechanics.AttackHelper;
import rs.lunarshop.utils.NpcHelper;
import rs.lunarshop.utils.mechanics.CritHelper;
import rs.lunarshop.utils.mechanics.RegenHelper;


public class CreatureStatsPatch {
    @SpirePatch2(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, int.class, float.class, float.class, float.class, float.class,
                    String.class, float.class, float.class, boolean.class})
    public static class MonsterStatsPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster __instance) {
            LunarNpcProp prop = NpcHelper.GetProp(__instance.id);
            if (prop != null) {
                prop = prop.modify();
                ArmorHelper.SetArmor(__instance, prop.getArmor());
                AttackHelper.SetAttack(__instance, prop.getAttack());
                RegenHelper.SetRegen(__instance, prop.getRegen());
            }
            CritHelper.SetCritChance(__instance, 0F);
            CritHelper.SetCritMult(__instance, CritHelper.BASE_CRITICAL_MULT);
        }
    }
    
    @SpirePatch2(clz = AbstractPlayer.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, AbstractPlayer.PlayerClass.class})
    public static class PlayerStatsPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance) {
            DifficultyMod mod = LunarMaster.GetDifficultyMod(LoadoutManager.Inst.getDifficultyLevel());
            if (mod != null) {
                ArmorHelper.SetArmor(__instance, mod.playerStats[0]);
                AttackHelper.SetAttack(__instance, mod.playerStats[1]);
                RegenHelper.SetRegen(__instance, mod.playerStats[2]);
            }
            CritHelper.SetCritChance(__instance, 0F);
            CritHelper.SetCritMult(__instance, CritHelper.BASE_CRITICAL_MULT);
        }
    }
}