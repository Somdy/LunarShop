package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.LunarTip;
import rs.lunarshop.utils.LunarTipHelper;

import java.util.ArrayList;
import java.util.List;

public class SingleRelicPopupPatches {
    @SpirePatch2(clz = SingleRelicViewPopup.class, method = "renderTips")
    public static class RenderRORStyleTipPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractRelic ___relic) {
            if (___relic instanceof AbstractLunarRelic && ___relic.isSeen) {
                List<LunarTip> tips = new ArrayList<>();
                if (((AbstractLunarRelic) ___relic).lunarTips.size() > 1) {
                    for (int i = 1; i < ((AbstractLunarRelic) ___relic).lunarTips.size(); i++) {
                        tips.add(((AbstractLunarRelic) ___relic).lunarTips.get(i));
                    }
                }
                if (!tips.isEmpty()) {
                    LunarTipHelper.QueueLunarTips(Settings.WIDTH / 2F + 340F * Settings.scale, 420F * Settings.scale, tips);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
