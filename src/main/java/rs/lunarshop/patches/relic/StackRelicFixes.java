package rs.lunarshop.patches.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarRelic;

public class StackRelicFixes {
    @SpirePatch(clz = AbstractRoom.class, method = "spawnRelicAndObtain")
    public static class StopCallingOnEquipFromSomewhereStrangeFix {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractRoom _inst, float x, float y, AbstractRelic r) {
            if (r instanceof AbstractLunarRelic && LMSK.Player().hasRelic(r.relicId)) {
                LunarMod.PatchLog("Preventing repeated equipping: " + r.name);
                r.obtain();
                r.isAnimating = false;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}