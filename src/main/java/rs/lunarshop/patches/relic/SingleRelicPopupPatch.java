package rs.lunarshop.patches.relic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtBehavior;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.*;

import java.util.ArrayList;
import java.util.List;

public class SingleRelicPopupPatch {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("ItemRarity"));
    public static final String[] TEXT = uiStrings.TEXT;
    public static final float RELIC_IMAGE_OFFSET_Y = 264F * Settings.scale;
    public static final float RELIC_NAME_OFFSET_Y = 472F * Settings.scale;
    public static final float RELIC_RARITY_OFFSET_Y = 420F * Settings.scale;
    
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
    @SpirePatch2(clz = SingleRelicViewPopup.class, method = "initializeLargeImg")
    public static class InitializeLargeImgPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractRelic ___relic, @ByRef Texture[] ___largeImg) {
            if (___relic instanceof AbstractLunarRelic && UnlockTracker.isRelicSeen(___relic.relicId)) {
                ___largeImg[0] = ImageMaster.loadImage("LunarAssets/imgs/items/largeRelics/"
                        + ((AbstractLunarRelic) ___relic).prop.lunarID + ".png");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
    @SpirePatch2(clz = SingleRelicViewPopup.class, method = "render")
    public static class RenderRORStyleSRVPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn Insert(SpriteBatch sb, AbstractRelic ___relic, Texture ___largeImg) {
            if (___relic instanceof AbstractLunarRelic && UnlockTracker.isRelicSeen(___relic.relicId)) {
                AbstractLunarRelic r = ((AbstractLunarRelic) ___relic);
                renderPopupTextArea(sb);
                renderRelicInfoText(sb, r);
                renderPopupBg(sb);
                renderBGIcon(sb, r);
                renderOutline(sb);
                renderRelicImage(sb, r, ___largeImg);
                renderRelicNameAndRarity(sb, r);
                renderTips(r);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(SingleRelicViewPopup.class, 
                        "renderPopupBg");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
        
        private static void renderPopupTextArea(SpriteBatch sb) {
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LunarImageMst.RelicPopupTextArea, Settings.WIDTH / 2F - 960F, Settings.HEIGHT / 2F - 540F, 
                    960F, 540F, 1920F, 1080F, Settings.scale, Settings.scale, 0F, 
                    0, 0, 1920, 1080, false, false);
        }
    
        private static void renderPopupBg(SpriteBatch sb) {
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LunarImageMst.RelicPopupBg, Settings.WIDTH / 2F - 960F, Settings.HEIGHT / 2F - 540F,
                    960F, 540F, 1920F, 1080F, Settings.scale, Settings.scale, 0F,
                    0, 0, 1920, 1080, false, false);
        }
        
        private static void renderBGIcon(SpriteBatch sb, AbstractLunarRelic r) {
            int popupIcon = r.getPopupIcon();
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LunarImageMst.ItemBgIconOf(popupIcon), Settings.WIDTH / 2F - 960F, Settings.HEIGHT / 2F - 540F,
                    960F, 540F, 1920, 1080, Settings.scale, Settings.scale,
                    0F, 0, 0, 1920, 1080, false, false);
        }
        
        private static void renderOutline(SpriteBatch sb) {
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LunarImageMst.RelicIconOutline, Settings.WIDTH / 2F - 960F, Settings.HEIGHT / 2F - 540F,
                    960F, 540F, 1920F, 1080F, Settings.scale, Settings.scale, 0F,
                    0, 0, 1920, 1080, false, false);
        }
        
        private static void renderRelicImage(SpriteBatch sb, AbstractLunarRelic r, Texture largeImg) {
            sb.setColor(Color.WHITE.cpy());
            if (largeImg == null) {
                sb.draw(r.img, Settings.WIDTH / 2F - 64F, Settings.HEIGHT / 2F - 64F + RELIC_IMAGE_OFFSET_Y, 
                        64F, 64F, 128F, 128F, Settings.scale * 2F, Settings.scale * 2F, 
                        0F, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(largeImg, Settings.WIDTH / 2F - 128F, Settings.HEIGHT / 2F - 128F + RELIC_IMAGE_OFFSET_Y,
                        128F, 128F, 256F, 256F, Settings.scale, Settings.scale, 0F, 
                        0, 0, 256, 256, false, false);
            }
        }
        
        private static void renderRelicNameAndRarity(SpriteBatch sb, AbstractLunarRelic r) {
            FontHelper.renderWrappedText(sb, LunarFont.ROR_RELIC_TITLE_FONT, "[" + r.name + "]", Settings.WIDTH / 2F, 
                    Settings.HEIGHT / 2F + RELIC_NAME_OFFSET_Y, 9999F, Color.WHITE.cpy(), Settings.scale);
            String rLabel = TEXT[0];
            String nLabel = r.prop.getRarity().locals();
            sb.setBlendFunction(770, 1);
            FontHelper.renderWrappedText(sb, LunarFont.ROR_RELIC_SUBTL_FONT, rLabel, Settings.WIDTH / 2F - 45F * Settings.scale,
                    Settings.HEIGHT / 2F + RELIC_RARITY_OFFSET_Y, 9999F, r.getRenderColor().cpy(), Settings.scale);
            FontHelper.renderWrappedText(sb, LunarFont.ROR_RELIC_SUBTL_FONT, nLabel, Settings.WIDTH / 2F + 45F * Settings.scale,
                    Settings.HEIGHT / 2F + RELIC_RARITY_OFFSET_Y, 9999F,
                    ColorHelper.GetRarityColor(r.prop.getRarity()), Settings.scale);
            sb.setBlendFunction(770, 771);
        }
        
        private static void renderTips(AbstractLunarRelic r) {
            List<LunarTip> tips = new ArrayList<>();
            if (r.lunarTips.size() > 1) {
                for (int i = 1; i < r.lunarTips.size(); i++) {
                    tips.add(r.lunarTips.get(i));
                }
            }
            if (!tips.isEmpty()) {
                LunarTipHelper.QueueLunarTips(Settings.WIDTH / 2F + 360F * Settings.scale, 420F * Settings.scale, tips);
            }
        }
        
        private static void renderRelicInfoText(SpriteBatch sb, AbstractLunarRelic r) {
            RelicInfoTextRender.renderText(sb, Settings.WIDTH / 2F - 304F * Settings.scale, 
                    Settings.HEIGHT / 2F + 98F * Settings.scale, r.getTextInfo());
        }
    }
    
    private static class RelicInfoTextRender {
        private static final float BOX_EDGE_H = 10F * Settings.scale;
        private static final float BOX_BODY_H = 20F * Settings.scale;
        private static final float BOX_W = 450F * Settings.scale;
        private static GlyphLayout gl = new GlyphLayout();
        private static float textHeight;
        private static final float TEXT_OFFSET_X = 22F * Settings.scale;
        private static final float HEADER_OFFSET_Y = -12F * Settings.scale;
        private static final float BODY_OFFSET_Y = -36F * Settings.scale;
        private static final float BODY_GAP_DIST = 5F * Settings.scale;
        private static final float DESC_LINE_SPACING = 32F * Settings.scale;
        private static final float RELIC_INFO_TEXT_WIDTH = (LunarImageMst.TEXT_BLOCK_W - 50F) * Settings.scale;
        
        private static void renderText(SpriteBatch sb, float x, float y, List<LunarTip> info) {
            for (LunarTip i : info) {
                if (!"FLAVOR".equals(i.getMsg())) {
                    textHeight = -FontHelper.getSmartHeight(LunarFont.ROR_RELIC_TEXT_FONT, i.body,
                            RELIC_INFO_TEXT_WIDTH, DESC_LINE_SPACING + 8F * Settings.scale)
                            - BODY_OFFSET_Y - BODY_GAP_DIST;
                    renderRelicInfoTextBlock(sb, x, y, i, LunarFont.ROR_RELIC_TEXT_FONT);
                } else {
                    textHeight = -FontHelper.getSmartHeight(LunarFont.ROR_TIP_HEADER_FONT, i.body,
                            RELIC_INFO_TEXT_WIDTH, DESC_LINE_SPACING + 4F * Settings.scale)
                            - BODY_OFFSET_Y - BODY_GAP_DIST;
                    renderRelicInfoTextBlock(sb, x, y, i, LunarFont.ROR_TIP_HEADER_FONT);
                }
                float offsetChange = textHeight + BOX_EDGE_H * 3.5F;
                y -= offsetChange;
            }
        }
    
        private static void renderRelicInfoTextBlock(SpriteBatch sb, float x, float y, LunarTip tip, BitmapFont bFont) {
            float h = textHeight;
            float boxW = LunarImageMst.TEXT_BLOCK_W * Settings.scale;
            boolean renderingFlavor = "FLAVOR".equals(tip.getMsg());
            sb.setColor(Color.WHITE.cpy());
            sb.draw(LunarImageMst.TextBlockTop, x, y, boxW, BOX_EDGE_H);
            sb.draw(LunarImageMst.TextBlockMid, x, y - h - BOX_EDGE_H, boxW, h + BOX_EDGE_H);
            sb.draw(LunarImageMst.TextBlockBot, x, y - h - BOX_BODY_H, boxW, BOX_EDGE_H);
            if (renderingFlavor) {
                FontHelper.renderSmartText(sb, bFont, tip.body, x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y - BODY_GAP_DIST,
                        RELIC_INFO_TEXT_WIDTH, DESC_LINE_SPACING + 2F * Settings.scale, Color.LIGHT_GRAY);
            } else {
                FontHelper.renderSmartText(sb, bFont, tip.body, x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y - BODY_GAP_DIST,
                        RELIC_INFO_TEXT_WIDTH, DESC_LINE_SPACING + 4F * Settings.scale, Color.WHITE);
            }
        }
    }
}