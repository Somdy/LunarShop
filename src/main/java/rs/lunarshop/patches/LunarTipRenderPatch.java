package rs.lunarshop.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CtBehavior;
import rs.lunarshop.utils.LunarTipHelper;

public class LunarTipRenderPatch {
    @SpirePatch2(clz = CardCrawlGame.class, method = "render")
    public static class RenderLunarTipPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(SpriteBatch ___sb) {
            LunarTipHelper.Render(___sb);
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(TipHelper.class, "render");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}