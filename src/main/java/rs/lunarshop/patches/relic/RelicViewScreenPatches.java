package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.compendium.RelicViewScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtBehavior;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.RelicMst;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RelicViewScreenPatches {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("LunarRelicViewList"));
    public static final String[] TEXT = uiStrings.TEXT;
    
    private static final ArrayList<AbstractRelic> lunarPool = new ArrayList<>();
    private static final ArrayList<AbstractRelic> planetPool = new ArrayList<>();
    private static final ArrayList<AbstractRelic> legacyPool = new ArrayList<>();
    
    @SpirePatch(clz = RelicViewScreen.class, method = "open")
    public static class OpenPatch {
        @SpirePostfixPatch
        public static void Postfix(RelicViewScreen _inst) {
            lunarPool.clear();
            lunarPool.addAll(RelicMst.GetLunarItems());
            lunarPool.forEach(r -> {
                if (UnlockTracker.isRelicSeen(r.relicId))
                    r.isSeen = true;
            });
            planetPool.clear();
            planetPool.addAll(RelicMst.GetPlanetItems());
            planetPool.forEach(r -> {
                if (UnlockTracker.isRelicSeen(r.relicId))
                    r.isSeen = true;
            });
            legacyPool.clear();
            legacyPool.addAll(RelicMst.GetLegacyItems());
            legacyPool.forEach(r -> {
                if (UnlockTracker.isRelicSeen(r.relicId))
                    r.isSeen = true;
            });
        }
    }
    
    @SpirePatch2(clz = RelicViewScreen.class, method = "render")
    public static class RenderPatch {
        private static final List<AbstractRelic> removeList = new ArrayList<>();
        @SpirePrefixPatch
        public static void PrefixRemoveItems() {
            removeList.clear();
            RelicLibrary.specialList.removeIf(r -> {
                if (RelicMst.IsLunarItem(r) || RelicMst.IsProvidenceCurse(r))
                    return removeList.add(r);
                return false;
            });
        }
        @SpirePostfixPatch
        public static void PostfixReturnItems() {
            RelicLibrary.specialList.addAll(removeList);
            removeList.clear();
        }
        
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(RelicViewScreen __instance, SpriteBatch sb) throws Exception {
            Method renderList = RelicViewScreen.class.getDeclaredMethod("renderList", 
                    SpriteBatch.class, String.class, String.class, ArrayList.class);
            renderList.setAccessible(true);
            invoker(__instance, sb, renderList, TEXT[0], TEXT[1], lunarPool);
            invoker(__instance, sb, renderList, TEXT[2], TEXT[3], planetPool);
            invoker(__instance, sb, renderList, TEXT[4], TEXT[5], legacyPool);
        }
        private static void invoker(RelicViewScreen _inst, SpriteBatch sb, Method renderList, String tier, String text, 
                                    ArrayList<AbstractRelic> relics) throws Exception {
            if (!relics.isEmpty()) {
                renderList.invoke(_inst, sb, tier, text, relics);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(RelicViewScreen.class, "button");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
    
    @SpirePatch(clz = RelicViewScreen.class, method = "update")
    public static class UpdatePatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(RelicViewScreen _inst) throws Exception {
            Method updateList = RelicViewScreen.class.getDeclaredMethod("updateList", ArrayList.class);
            updateList.setAccessible(true);
            invoker(_inst, updateList, lunarPool);
            invoker(_inst, updateList, planetPool);
            invoker(_inst, updateList, legacyPool);
        }
        private static void invoker(RelicViewScreen _inst, Method updateList, ArrayList<AbstractRelic> relics) throws Exception {
            if (!relics.isEmpty()) {
                updateList.invoke(_inst, relics);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(Settings.class, "isControllerMode");
                int[] line = LineFinder.findAllInOrder(ctBehavior, matcher);
                return new int[] {line[line.length - 1]};
            }
        }
    }
}