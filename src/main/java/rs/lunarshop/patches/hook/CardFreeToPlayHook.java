package rs.lunarshop.patches.hook;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.relics.FreePlayRelic;

public class CardFreeToPlayHook {
    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class ModifyFreeToPlayPatch {
        @SpireInsertPatch(rloc = 8)
        public static SpireReturn<Boolean> Insert(AbstractCard _inst) {
            boolean free = false;
            if (InCombat()) {
                for (AbstractRelic r : LMSK.Player().relics) {
                    if (r instanceof FreePlayRelic)
                        free = ((FreePlayRelic) r).canFreeToPlay(_inst);
                    if (free) break;
                }
            }
            return SpireReturn.Return(free);
        }
    }
    
    static boolean InCombat() {
        return LMSK.Player() != null && AbstractDungeon.getCurrMapNode() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }
}