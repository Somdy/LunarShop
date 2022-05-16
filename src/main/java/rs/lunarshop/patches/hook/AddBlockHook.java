package rs.lunarshop.patches.hook;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import rs.lunarshop.interfaces.relics.GainedBlockRelic;

public class AddBlockHook {
    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    public static class OnCreatureGainedBlockPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp", "effect"})
        public static void Insert(AbstractCreature _inst, int blockAmount, float tmp, boolean effect) {
            if (_inst instanceof AbstractPlayer && !((AbstractPlayer) _inst).relics.isEmpty()) {
                for (AbstractRelic r : ((AbstractPlayer) _inst).relics) {
                    if (r instanceof GainedBlockRelic)
                        ((GainedBlockRelic) r).onBlockGained(_inst, blockAmount, tmp, effect);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, 
                        "currentBlock");
                int line = LineFinder.findAllInOrder(ctBehavior, matcher)[1];
                return new int[]{line};
            }
        }
    }
}