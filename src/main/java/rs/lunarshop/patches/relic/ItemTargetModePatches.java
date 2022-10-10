package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import rs.lunarshop.utils.ItemHelper;

public class ItemTargetModePatches {
    @SpirePatch(clz = TopPanel.class, method = "update")
    public static class TopPanelUpdatePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(TopPanel _inst) {
            ItemHelper.TARGET_INVOKER.update();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(PotionPopUp.class, "update");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
    @SpirePatch(clz = TopPanel.class, method = "render")
    public static class TopPanelRenderPatch {
        @SpirePrefixPatch
        public static void Prefix(TopPanel _inst, SpriteBatch sb) {
            ItemHelper.TARGET_INVOKER.render(sb);
        }
    }
}