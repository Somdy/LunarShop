package rs.lunarshop.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CtBehavior;
import rs.lunarshop.ui.loadout.LoadoutManager;

public class CharacterLoadoutPatch {
    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class UpdateLoadoutPatch {
        @SpirePrefixPatch
        public static void Prefix(CharacterSelectScreen _inst) {
            if ((Boolean) ReflectionHacks.getPrivate(_inst, CharacterSelectScreen.class, "anySelected")) {
                LoadoutManager.Inst.update();
            }
        }
    }
    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class RenderLoadoutPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen _inst, SpriteBatch sb) {
            if ((Boolean) ReflectionHacks.getPrivate(_inst, CharacterSelectScreen.class, "anySelected")) {
                LoadoutManager.Inst.render(sb);
            }
        }
    }
    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class LoadoutCaptureCSSPatch {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen _inst) {
            LoadoutManager.Inst.captureCharSelectScreen(_inst);
        }
    }
    @SpirePatch(clz = CharacterOption.class, method = "updateHitbox")
    public static class CharacterOptionPrefPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CharacterOption _inst) {
            LoadoutManager.Inst.loadPrefsOnCharacterOptionJustSelected(_inst);
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "getPrefs");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}