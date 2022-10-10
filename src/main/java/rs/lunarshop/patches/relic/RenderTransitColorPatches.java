package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarRelic;

import java.lang.reflect.Field;

public final class RenderTransitColorPatches {
    public static Color RenderColor;
    
    @SpirePatch(clz = SingleRelicViewPopup.class, method = "renderName")
    public static class RenderRelicNamePatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SingleRelicViewPopup _inst, SpriteBatch sb) {
            try {
                Field relic = SingleRelicViewPopup.class.getDeclaredField("relic");
                relic.setAccessible(true);
                AbstractRelic r = (AbstractRelic) relic.get(_inst);
                if (r instanceof AbstractLunarRelic && r.isSeen) {
                    RenderColor = ((AbstractLunarRelic) r).getRenderColor();
                    FontHelper.renderWrappedText(sb, FontHelper.SCP_cardDescFont, r.name, Settings.WIDTH / 2F,
                            Settings.HEIGHT / 2F + 280F * Settings.scale, 9999F, RenderColor.cpy(), 0.9F);
                    return SpireReturn.Return();
                }
            } catch (Exception e) {
                LunarMod.LogInfo("Failed to render item color");
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }
}