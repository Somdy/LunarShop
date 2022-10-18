package rs.lunarshop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.core.LunarMod;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class LunarTipHelper {
    private static final UIStrings uiStrings = LunarMod.UIStrings("LunarTipBuilder");
    public static final String[] TEXT = uiStrings.TEXT;
    
    private static boolean renderedTipThisFrame = false;
    private static boolean isCard = false;
    private static boolean relicPopup = false;
    private static float drawX;
    private static float drawY;
    private static List<String> KEYWORDS = new ArrayList<>();
    private static List<LunarTip> POWER_TIPS = new ArrayList<>();
    private static LunarTip TIP = new LunarTip(null, null, null, null);
    private static AbstractCard card;
    private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);
    private static final float CARD_TIP_PAD = 12F * Settings.scale;
    private static final float BOX_EDGE_H = 10F * Settings.scale;
    private static final float BOX_BODY_H = 20F * Settings.scale;
    private static final float BOX_W = 450F * Settings.scale;
    private static GlyphLayout gl = new GlyphLayout();
    private static float textHeight;
    private static final float TEXT_OFFSET_X = 22F * Settings.scale;
    private static final float HEADER_OFFSET_Y = -12F * Settings.scale;
    private static final float BODY_OFFSET_Y = -60F * Settings.scale;
    private static final float BODY_GAP_DIST = 5F * Settings.scale;
    private static final float HEADER_TAB_HEIGHT = -BODY_OFFSET_Y + HEADER_OFFSET_Y + BOX_EDGE_H;
    private static final float BODY_TEXT_WIDTH = 400F * Settings.scale;
    private static final float TITLE_DESC_LINE_SPACING = 24F * Settings.scale;
    private static final float TIP_DESC_LINE_SPACING = 32F * Settings.scale;
    private static final float POWER_ICON_OFFSET_X = 40F * Settings.scale;
    private static final float RELIC_INFO_TEXT_WIDTH = (LunarImageMst.TEXT_BLOCK_W - 50F) * Settings.scale;
    private static OrthographicCamera camera;
    private static float scrollPosition = 0F;
    private static float scrollTarget = 0F;
    private static float scrollLowerBound = 0F;
    private static float scrollUpperBound = 100F;
    private static float grabStartY = 0F;
    private static boolean grabbedScreen = false;
    private static float scrollSize = 0F;
    private static ScrollBar scrollBar = new ScrollBar(new RelicTextScrollListener(), Settings.WIDTH, 0F, 
            862F * Settings.scale){
        @Override
        public void reset() {}
    };
    
    public static void Render(SpriteBatch sb) {
        if (!Settings.hidePopupDetails && renderedTipThisFrame) {
            if (LMSK.Player() != null && (LMSK.Player().inSingleTargetMode
                    || (LMSK.Player().isDraggingCard && !Settings.isTouchScreen))) {
                TIP.clean();
                card = null;
                renderedTipThisFrame = false;
                return;
            }
            if (Settings.isTouchScreen && LMSK.Player() != null && LMSK.Player().isHoveringDropZone) {
                TIP.clean();
                card = null;
                renderedTipThisFrame = false;
                return;
            }
            if (isCard && card != null) {
                //TODO: No card tip needs to be rendered
            } else if (relicPopup && !POWER_TIPS.isEmpty()) {
                RenderRelicInfoText(sb, drawX, drawY, POWER_TIPS);
                relicPopup = false;
            } else if (TIP.hasTip()) {
                textHeight = -FontHelper.getSmartHeight(LunarFont.ROR_TIP_BODY_FONT, TIP.body, BODY_TEXT_WIDTH, 
                        TIP_DESC_LINE_SPACING) - BODY_OFFSET_Y - HEADER_OFFSET_Y + BODY_GAP_DIST * 2;
                renderTipBox(sb, drawX, drawY, TIP.header, TIP.body, TIP.hColor, TIP.bColor, TIP.hFontColor, TIP.bFontColor);
                TIP.clean();
            } else {
                renderLunarTips(sb, drawX, drawY, POWER_TIPS);
            }
            renderedTipThisFrame = false;
        }
    }
    
    public static void RenderGenericTip(float x, float y, LunarTip tip) {
        if (!Settings.hidePopupDetails) {
            if (!renderedTipThisFrame) {
                renderedTipThisFrame = true;
                TIP.copy(tip);
                drawX = x;
                drawY = y;
            }
        }
    }
    
    public static void QueueGenericLunarTips(float x, float y, List<? extends PowerTip> tipsToConvert) {
        if (!renderedTipThisFrame) {
            renderedTipThisFrame = true;
            drawX = x;
            drawY = y;
            POWER_TIPS = LunarTip.FromPowerTips(tipsToConvert, true, null);
        }
    }
    
    public static void QueueLunarTips(float x, float y, List<LunarTip> tips) {
        if (!renderedTipThisFrame) {
            renderedTipThisFrame = true;
            drawX = x;
            drawY = y;
            POWER_TIPS.clear();
            POWER_TIPS.addAll(tips);
        }
    }
    
    public static float MouseOffsetLeftX(float originX, float gap) {
        if ((originX + BOX_W) - gap >= InputHelper.mX) {
            originX = InputHelper.mX - BOX_W - gap;
        }
        return originX;
    }
    
    public static void RenderRelicInfoText(SpriteBatch sb, float x, float y, List<LunarTip> info) {
        if (camera == null) {
            try {
                Field c = CardCrawlGame.class.getDeclaredField("camera");
                c.setAccessible(true);
                camera = (OrthographicCamera) c.get(Gdx.app.getApplicationListener());
            } catch (Exception e) {
                LunarMod.WarnInfo("Failed to catch game camera when rendering relic text");
                e.printStackTrace();
                return;
            }
        }
        if (isScrolling()) {
            y = y + scrollPosition;
        }
        for (LunarTip i : info) {
            if (!"FLAVOR".equals(i.getMsg())) {
                textHeight = -FontHelper.getSmartHeight(LunarFont.ROR_RELIC_TEXT_FONT, i.body,
                        RELIC_INFO_TEXT_WIDTH, TIP_DESC_LINE_SPACING + 8F * Settings.scale)
                        - BODY_OFFSET_Y - BODY_GAP_DIST;
                renderRelicInfoTextBlock(sb, x, y, i, LunarFont.ROR_RELIC_TEXT_FONT);
            } else {
                textHeight = -FontHelper.getSmartHeight(LunarFont.ROR_TIP_HEADER_FONT, i.body,
                        RELIC_INFO_TEXT_WIDTH, TIP_DESC_LINE_SPACING + 4F * Settings.scale)
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
                    RELIC_INFO_TEXT_WIDTH, TIP_DESC_LINE_SPACING + 2F * Settings.scale, Color.LIGHT_GRAY);
        } else {
            FontHelper.renderSmartText(sb, bFont, tip.body, x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y - BODY_GAP_DIST,
                    RELIC_INFO_TEXT_WIDTH, TIP_DESC_LINE_SPACING + 4F * Settings.scale, Color.WHITE);
        }
    }
    
    private static void renderLunarTips(SpriteBatch sb, float x, float y, List<LunarTip> tips) {
        float originalY = y;
        boolean offsetLeft = x > Settings.WIDTH / 2F;
        float offset = 0F;
        for (LunarTip tip : tips) {
            textHeight = getPowerTipHeight(tip);
            float offsetChange = textHeight + BOX_EDGE_H * 3.15F;
            if (offset + offsetChange >= Settings.HEIGHT * 0.7F) {
                y = originalY;
                offset = 0F;
                if (offsetLeft) {
                    x -= 324F * Settings.scale;
                } else {
                    x += 324F * Settings.scale;
                }
            }
            renderTipBox(sb, x, y, tip.header, tip.body, tip.hColor, tip.bColor, tip.hFontColor, tip.bFontColor);
            gl.setText(LunarFont.ROR_TIP_HEADER_FONT, tip.header, tip.hFontColor, 0.0F, -1, false);
            if (tip.img != null) {
                sb.setColor(Color.WHITE);
                sb.draw(tip.img, x + TEXT_OFFSET_X + gl.width + 5F * Settings.scale, y - 10F * Settings.scale, 
                        32F * Settings.scale, 32F * Settings.scale);
            } else if (tip.imgRegion != null) {
                sb.setColor(Color.WHITE);
                sb.draw(tip.imgRegion, x + gl.width + POWER_ICON_OFFSET_X - tip.imgRegion.packedWidth / 2F, 
                        y + 5F * Settings.scale - tip.imgRegion.packedHeight / 2F, 
                        tip.imgRegion.packedWidth / 2F, tip.imgRegion.packedHeight / 2F, 
                        tip.imgRegion.packedWidth, tip.imgRegion.packedHeight, Settings.scale * 0.75F, 
                        Settings.scale * 0.75F, 0F);
            }
            y -= offsetChange;
            offset += offsetChange;
        }
    }
    
    private static float getPowerTipHeight(PowerTip tip) {
        return -FontHelper.getSmartHeight(LunarFont.ROR_TIP_BODY_FONT, tip.body, BODY_TEXT_WIDTH,
                TIP_DESC_LINE_SPACING) - BODY_OFFSET_Y - HEADER_OFFSET_Y + BODY_GAP_DIST * 2;
    }
    
    private static void renderTipBox(SpriteBatch sb, float x, float y, String title, String body) {
        renderTipBox(sb, x, y, title, body, LunarTip.H_DEFAULT, LunarTip.B_DEFAULT, LunarTip.F_DEFAULT, LunarTip.F_DEFAULT);
    }
    
    private static void renderTipBox(SpriteBatch sb, float x, float y, String title, String body, Color hColor, Color bColor, 
                                     Color hFont, Color bFont) {
        float h = textHeight;
        sb.setColor(bColor);
        sb.draw(LunarImageMst.LunarTipBodyBg, x, y - h - BOX_BODY_H, BOX_W, h + BOX_EDGE_H * 3);
        sb.setColor(hColor);
        sb.draw(LunarImageMst.LunarTipHeaderBg, x, y + BODY_OFFSET_Y - HEADER_OFFSET_Y, BOX_W, HEADER_TAB_HEIGHT);
        sb.setColor(Color.LIGHT_GRAY.cpy());
        sb.draw(LunarImageMst.LunarTipBoxTop, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(LunarImageMst.LunarTipBoxMid, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(LunarImageMst.LunarTipBoxBot, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);
        FontHelper.renderFontLeftTopAligned(sb, LunarFont.ROR_TIP_HEADER_FONT, title, x + TEXT_OFFSET_X, 
                y + HEADER_OFFSET_Y, hFont);
        FontHelper.renderSmartText(sb, LunarFont.ROR_TIP_BODY_FONT, body, x + TEXT_OFFSET_X, y + BODY_OFFSET_Y - BODY_GAP_DIST, 
                BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, bFont);
    }
    
    private static class RelicTextScrollListener implements ScrollBarListener {
        @Override
        public void scrolledUsingBar(float percent) {
            scrollTarget = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound, percent);
            updateScrollPercent();
        }
    }
    
    private static void updateScrollPercent() {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound, scrollPosition);
    }
    
    private static boolean isScrolling() {
        return scrollSize > 0F;
    }
}