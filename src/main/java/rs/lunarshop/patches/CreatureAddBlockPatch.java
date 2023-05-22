package rs.lunarshop.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CreatureAddBlockPatch {
    @SpirePatch2(clz = AbstractCreature.class, method = "addBlock")
    public static class ModifyPowerOnTimePatch {
        @SpirePostfixPatch
        public static void Postfix() {
            AbstractDungeon.onModifyPower();
        }
    }
}