package rs.lunarshop.shops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.enums.ShopRelicEffect;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.subjects.AbstractLunarShop;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.vfx.misc.LunarStoreRelicEffect;

public class LunarStoreRelic implements LunarUtils {
    private static final float RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
    private static final float RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
    private static final float RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
    private static final float RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
    private static final float GOLD_IMG_WIDTH = ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    
    private LunarStoreRelicEffect effect;
    private final AbstractLunarShop shop;
    private final Texture priceUI;
    public final AbstractRelic relic;
    public int price;
    public boolean isPurchased;
    
    public LunarStoreRelic(@NotNull AbstractRelic relic, AbstractLunarShop shop) {
        this.relic = relic;
        this.shop = shop;
        this.priceUI = shop.getPriceUI();
        effect = null;
        initPrice();
    }
    
    private void initPrice() {
        price = relic.getPrice();
        if (relic instanceof AbstractLunarRelic) {
            price = ((AbstractLunarRelic) relic).prop.shopPrice(shop.type);
        }
    }
    
    public void update(float x, float y) {
        if (relic != null) {
            if (!isPurchased) {
                if (effect != null) effect.update();
                if (x > 0 && y > 0) {
                    relic.currentX = x;
                    relic.currentY = y;
                    relic.hb.move(relic.currentX, relic.currentY);
                }
                relic.hb.update();
                relic.update();
                if (relic.hb.hovered) {
                    if (effect != null) effect.setPause(true);
                    relic.scale = scale(1.25F);
                    if (InputHelper.justClickedLeft)
                        relic.hb.clickStarted = true;
                    if (InputHelper.justClickedRight) {
                        CardCrawlGame.relicPopup.open(relic);
                        repositionMouseOnRelicPopup();
                    }
                } else {
                    if (effect != null) effect.setPause(false);
                    relic.scale = MathHelper.scaleLerpSnap(relic.scale, Settings.scale);
                }
            }
            if (relic.hb.clicked) {
                relic.hb.clicked = false;
                if (shop.canBuyRelic(this)) {
                    shop.purchaseRelic(this);
                    if (effect != null) effect = null;
                } else {
                    shop.createSpeech(shop.cantBuyMsg());
                }
            }
        }
    }
    
    public void render(SpriteBatch sb) {
        if (relic != null) {
            relic.renderWithoutAmount(sb, shop.relicSkinColor());
            sb.setColor(Color.WHITE.cpy());
            sb.draw(priceUI, relic.currentX + RELIC_GOLD_OFFSET_X, relic.currentY + RELIC_GOLD_OFFSET_Y, 
                    GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color cost = shop.canBuyRelic(this) ? Color.WHITE.cpy() : Color.SALMON.cpy();
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(price), 
                    relic.currentX + RELIC_PRICE_OFFSET_X, relic.currentY + RELIC_PRICE_OFFSET_Y, cost);
        }
    }
    
    public void hide() {
        if (relic != null) {
            relic.currentY = Settings.HEIGHT * 2F;
        }
    }
    
    public void addEffect(ShopRelicEffect effect) {
        this.effect = new LunarStoreRelicEffect(relic, effect);
    }
    
    public boolean hasEffect() {
        return effect != null;
    }
}