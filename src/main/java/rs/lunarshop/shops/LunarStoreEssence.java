package rs.lunarshop.shops;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.items.relics.CETemplate;
import rs.lunarshop.subjects.AbstractCommandEssence;
import rs.lunarshop.subjects.AbstractLunarShop;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.vfx.misc.LunarStoreRelicEffect;

public class LunarStoreEssence implements LunarUtils {
    private static final float RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
    private static final float RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
    private static final float RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
    private static final float RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
    private static final float GOLD_IMG_WIDTH = ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    
    private final AbstractLunarShop shop;
    private final Texture priceUI;
    public final AbstractCommandEssence essence;
    public int price;
    public boolean isPurchased;
    
    public LunarStoreEssence(@NotNull AbstractCommandEssence essence, AbstractLunarShop shop) {
        this.essence = essence;
        this.shop = shop;
        priceUI = shop.getPriceUI();
        price = essence.getPrice();
    }
    
    void applyDiscount(float percent) {
        price = price - MathUtils.floor(price * percent);
    }
    
    public void update(float x, float y) {
        if (essence != null) {
            if (!isPurchased) {
                essence.move(x, y);
                essence.update();
                if (essence.hovered) {
                    essence.scale = 1.25F;
                    if (InputHelper.justClickedRight && !essence.clicked) {
                        CardCrawlGame.relicPopup.open(CETemplate.Get());
                        repositionMouseOnRelicPopup();
                    }
                } else {
                    essence.scale = MathHelper.scaleLerpSnap(essence.scale, Settings.scale);
                }
            }
            if (essence.clicked) {
                essence.clicked = false;
                if (shop.canBuyEssence(this)) {
                    shop.purchaseEssence(this);
                }
                else {
                    shop.createSpeech(shop.cantBuyMsg());
                }
            }
        }
    }
    
    public void render(SpriteBatch sb) {
        if (essence != null) {
            essence.render(sb);
            sb.setColor(Color.WHITE.cpy());
            sb.draw(priceUI, essence.hb.cX + RELIC_GOLD_OFFSET_X, essence.hb.cY + RELIC_GOLD_OFFSET_Y,
                    GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color cost = shop.canBuyEssence(this) ? Color.WHITE.cpy() : Color.SALMON.cpy();
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(price),
                    essence.hb.cX + RELIC_PRICE_OFFSET_X, essence.hb.cY + RELIC_PRICE_OFFSET_Y, cost);
        }
    }
    
    public void hide() {
        if (essence != null) {
            essence.move(essence.hb.cX, Settings.HEIGHT * 2F);
        }
    }
}