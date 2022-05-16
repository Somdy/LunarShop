package rs.lunarshop.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.Fealty;
import rs.lunarshop.npcs.monsters.LunarSnecko;

public class SpawnLunarBossesPatches {
    
    @SpirePatch(clz = AbstractDungeon.class, method = "getBoss")
    public static class GetFinalBossPatch {
        @SpireInsertPatch(rloc = 3)
        public static SpireReturn<MonsterGroup> GetLunarBoss(AbstractDungeon _inst) {
            if (Fealty.SpawnNewtForHeart() && (AbstractDungeon.floorNum >= 53
                    || AbstractDungeon.lastCombatMetricKey.equals(MonsterHelper.THE_HEART_ENC))) {
                return SpireReturn.Return(new MonsterGroup(new LunarSnecko(0F, 0F)));
            }
            return SpireReturn.Continue();
        }
    }
    
    @SpirePatch(clz = MonsterHelper.class, method = "getEncounter")
    public static class SpawnFinalBossPatch {
        @SpirePrefixPatch
        public static SpireReturn<MonsterGroup> GetLunarBoss(String key) {
            if (Fealty.SpawnNewtForHeart() && AbstractDungeon.floorNum >= 53
                    && AbstractDungeon.dungeonMapScreen.map.atBoss
                    && key.equals(MonsterHelper.THE_HEART_ENC)) {
                return SpireReturn.Return(new MonsterGroup(new LunarSnecko(0F, 0F)));
            }
            return SpireReturn.Continue();
        }
    }
}