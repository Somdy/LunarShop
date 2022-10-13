package rs.lunarshop.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import rs.lunarshop.utils.AchvHelper;

public class GameOverScreenPatch {
    @SpirePatch(clz = GameOverScreen.class, method = "calcScore")
    public static class CalcScorePatch {
        @SpirePostfixPatch
        public static void Postfix(boolean victory) {
            if (victory) {
                AchvHelper.CheckAchvOnGameVictory();
            }
        }
    }
}