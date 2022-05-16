package rs.lunarshop.patches.hook;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.subjects.AbstractLunarRelic;

public class CreatureHealHook {
    @SpirePatch(clz = AbstractCreature.class, method = "heal", paramtypez = {int.class, boolean.class})
    public static class AfterHealPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractCreature _inst, int healAmt, boolean effect) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AbstractLunarRelic)
                    healAmt = ((AbstractLunarRelic) r).preModifyHeal(_inst, healAmt);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "currentHealth");
                int line = LineFinder.findAllInOrder(ctBehavior, matcher)[0];
                return new int[]{line};
            }
        }
    }
}