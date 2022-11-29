package rs.lunarshop.patches.hook;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lunarshop.core.LunarMod;

public class DropGoldRewardHook {
    @SpirePatch(clz = AbstractRoom.class, method = "addGoldToRewards")
    public static class ModifyGoldNum {
        @SpirePrefixPatch
        public static void Prefix(AbstractRoom _inst, @ByRef int[] gold) {
            gold[0] = LunarMod.DropGoldReward(gold[0], _inst);
        }
    }
}