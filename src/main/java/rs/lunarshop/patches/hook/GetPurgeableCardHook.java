package rs.lunarshop.patches.hook;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.relics.GetPurgeableCardRelic;

public class GetPurgeableCardHook {
    @SpirePatch2(clz = CardGroup.class, method = "getPurgeableCards")
    public static class GetPurgeableCardRelicPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor(){
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("add".equals(m.getMethodName())) {
                        m.replace("{if(" + GetPurgeableCardRelicPatch.class.getName() + ".CanPurgeCard(c))" +
                                "{$_=$proceed($$);}}");
                    }
                }
            };
        }
        public static boolean CanPurgeCard(AbstractCard card) {
            boolean retVal = true;
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof GetPurgeableCardRelic)
                    retVal = ((GetPurgeableCardRelic) r).canPurgeCard(card);
            }
            return retVal;
        }
    }
}