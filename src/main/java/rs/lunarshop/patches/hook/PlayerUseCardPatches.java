package rs.lunarshop.patches.hook;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import rs.lunarshop.interfaces.InstantOnUseCardInterface;

public class PlayerUseCardPatches {
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class MultiCardUseEffectPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer _inst, AbstractCard card, AbstractMonster m, int e) {
            for (AbstractRelic r : _inst.relics) {
                if (r instanceof InstantOnUseCardInterface)
                    ((InstantOnUseCardInterface) r).instantOnUseCard(card, _inst, m);
            }
            for (AbstractPower r : _inst.powers) {
                if (r instanceof InstantOnUseCardInterface)
                    ((InstantOnUseCardInterface) r).instantOnUseCard(card, _inst, m);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
