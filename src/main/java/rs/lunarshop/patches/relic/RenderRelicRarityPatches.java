package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.utils.ColorHelper;
import rs.lunarshop.utils.LunarImageMst;

import java.lang.reflect.Field;

public final class RenderRelicRarityPatches {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("ItemRarity"));
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
                    String nLabel = ((AbstractLunarRelic) r).prop.getRarity().locals();
                    sb.setBlendFunction(770, 1);
                    FontHelper.renderWrappedText(sb, FontHelper.cardDescFont_N, rLabel, Settings.WIDTH / 2F - 35F * Settings.scale,
                            Settings.HEIGHT / 2F + 230F * Settings.scale, 9999F, RenderTransitColorPatches.RenderColor.cpy(), 1F);
                    FontHelper.renderWrappedText(sb, FontHelper.cardDescFont_N, nLabel, Settings.WIDTH / 2F + 35F * Settings.scale,
                            Settings.HEIGHT / 2F + 230F * Settings.scale, 9999F,
                            ColorHelper.GetRarityColor(((AbstractLunarRelic) r).prop.getRarity()), 1F);
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
    
    @SpirePatch(clz = SingleRelicViewPopup.class, method = "renderPopupBg")
    public static class LunarTierBgPatch {
        @SpirePostfixPatch
        public static void Postfix(SingleRelicViewPopup _inst, SpriteBatch sb) {
            try {
                Field relic = SingleRelicViewPopup.class.getDeclaredField("relic");
                relic.setAccessible(true);
                AbstractRelic r = (AbstractRelic) relic.get(_inst);
                if (r.isSeen && r instanceof AbstractLunarRelic) {
                    int popupIcon = ((AbstractLunarRelic) r).getPopupIcon();
                    sb.setColor(Color.WHITE.cpy());
                    sb.draw(LunarImageMst.ItemBgIconOf(popupIcon), Settings.WIDTH / 2F - 960F, Settings.HEIGHT / 2F - 540F, 
                            960F, 540F, 1920, 1080, Settings.scale, Settings.scale, 
                            0F, 0, 0, 1920, 1080, false, false);
                }
            } catch (Exception e) {
                LunarMod.LogInfo("Failed to render lunar tier bg");
                e.printStackTrace();
            }
        }
    }
}