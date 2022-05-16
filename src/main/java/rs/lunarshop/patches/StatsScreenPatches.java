package rs.lunarshop.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.CharacterManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import javassist.CtBehavior;
import rs.lunarshop.core.LunarMod;

import java.lang.reflect.Field;

public class StatsScreenPatches {
    @SpirePatch(clz = StatsScreen.class, method = "open")
    public static class OpenStatsPatch {
        @SpirePostfixPatch
        public static void Postfix(StatsScreen _inst) {
            LunarMod.OpenAchvGrid();
        }
    }
    
    @SpirePatch(clz = StatsScreen.class, method = "update")
    public static class UpdateStatsPatch {
        @SpirePostfixPatch
        public static void Postfix(StatsScreen _inst) {
            LunarMod.achvGrid.update();
        }
    }
    
    @SpirePatch(clz = StatsScreen.class, method = "renderStatScreen")
    public static class RenderStatsPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"renderY"})
        public static void Insert(StatsScreen _inst, SpriteBatch sb, @ByRef float[] renderY) throws IllegalAccessException {
            Field screenX = ReflectionHacks.getCachedField(StatsScreen.class, "screenX");
            LunarMod.achvGrid.renderHeaderInStats(sb, screenX.getFloat(_inst), renderY[0]);
            LunarMod.achvGrid.renderInStats(sb, renderY[0]);
            renderY[0] -= 950F * Settings.scale;
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(CharacterManager.class, 
                        "getAllCharacters");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
    
    @SpirePatch(clz = StatsScreen.class, method = "calculateScrollBounds")
    public static class CalcScrollBoundsPatch {
        @SpirePostfixPatch
        public static void Postfix(StatsScreen _inst) throws Exception {
            Field scrollUpperBound = StatsScreen.class.getDeclaredField("scrollUpperBound");
            scrollUpperBound.setAccessible(true);
            float oBound = scrollUpperBound.getFloat(_inst);
            oBound += 1000F * Settings.scale;
            scrollUpperBound.setFloat(_inst, oBound);
        }
    }
}