package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.ColorHelper;

import java.lang.reflect.Field;

public final class RenderRelicRarityPatches {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(LunarMod.Prefix("ItemRarity"));
    public static final String[] TEXT = uiStrings.TEXT;
    
    @SpirePatch(clz = SingleRelicViewPopup.class, method = "renderRarity")
    public static class LunarRarityPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SingleRelicViewPopup _inst, SpriteBatch sb) {
            try {
                Field relic = SingleRelicViewPopup.class.getDeclaredField("relic");
                relic.setAccessible(true);
                AbstractRelic r = (AbstractRelic) relic.get(_inst);
                if (r.isSeen && r instanceof AbstractLunarRelic) {
                    String rLabel = TEXT[0];
                    String nLabel = ((AbstractLunarRelic) r).props.getRarity().local;
                    sb.setBlendFunction(770, 1);
                    FontHelper.renderWrappedText(sb, FontHelper.cardDescFont_N, rLabel, Settings.WIDTH / 2F - 35F * Settings.scale,
                            Settings.HEIGHT / 2F + 230F * Settings.scale, 9999F, RenderTransitColorPatches.RenderColor.cpy(), 1F);
                    FontHelper.renderWrappedText(sb, FontHelper.cardDescFont_N, nLabel, Settings.WIDTH / 2F + 35F * Settings.scale,
                            Settings.HEIGHT / 2F + 230F * Settings.scale, 9999F,
                            ColorHelper.GetRarityColor(((AbstractLunarRelic) r).props.getRarity()), 1F);
                    sb.setBlendFunction(770, 771);
                    return SpireReturn.Return();
                }
            } catch (Exception e) {
                LunarMod.LogInfo("Failed to render lunar rarity");
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }
}