package rs.lunarshop.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.shops.LunarStoreEssence;
import rs.lunarshop.shops.LunarStoreRelic;
import rs.lunarshop.shops.ShopType;
import rs.lunarshop.utils.LunarMathUtils;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLunarShop extends ShopScreen implements LunarUtils {
    private static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
    private static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
    private static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;
    
    private static final Map<String, Texture> rugMap = new HashMap<>();
    
    public static boolean visiting;
    
    private ShopSpeechBubble speechBubble;
    private SpeechTextEffect dialogEffect;
    protected float rugY = Settings.HEIGHT / 2F + 540F * Settings.yScale;
    public final int type;
    private Texture rugImg;
    protected List<LunarStoreRelic> relics;
    
    public AbstractLunarShop(String ID, int type, String rugPath) {
        loadRugImg(ID, rugPath);
        this.type = type;
        relics = new ArrayList<>();
    }
    
    private void loadRugImg(String ID, String rugPath) {
        if (rugMap.containsKey(ID)) {
            rugImg = rugMap.get(ID);
        } else {
            rugImg = ImageMaster.loadImage(rugPath);
            rugMap.put(ID, rugImg);
        }
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    @Override
    public void init(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
        init();
    }
    
    protected abstract void init();
    
    @Override
    public void applyDiscount(float multiplier, boolean affectPurge) {
        discount();
    }
    
    protected abstract void discount();
    
    public abstract boolean canBuyRelic(LunarStoreRelic relic);
    public abstract void purchaseRelic(LunarStoreRelic relic);
    public abstract boolean canBuyEssence(LunarStoreEssence essence);
    public abstract void purchaseEssence(LunarStoreEssence essence);
    
    public abstract void open();
    
    public abstract void update();
    
    protected void updateSpeech() {
        if (speechBubble != null) {
            speechBubble.update();
            if (speechBubble.hb.hovered && speechBubble.duration > 0.3F) {
                speechBubble.duration = 0.3F;
                dialogEffect.duration = 0.3F;
            }
            if (speechBubble.isDone) speechBubble = null;
        }
        if (dialogEffect != null) {
            dialogEffect.update();
            if (dialogEffect.isDone) dialogEffect = null;
        }
    }
    
    protected void updateRug() {
        if (rugY != 0F) {
            rugY = LunarMathUtils.lerp(rugY, Settings.HEIGHT / 2F - scaleY(540F), Gdx.graphics.getDeltaTime() * 5F);
            if (Math.abs(rugY - 0F) < 0.5F) rugY = 0F;
        }
    }
    
    public abstract void render(SpriteBatch sb);
    
    protected void renderRug(@NotNull SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(rugImg, 0F, rugY, Settings.WIDTH, Settings.HEIGHT);
    }
    
    protected void renderRelics(SpriteBatch sb) {
        for (LunarStoreRelic r : relics) {
            if (!r.isPurchased)
                r.render(sb);
        }
    }
    
    protected void renderSpeech(SpriteBatch sb) {
        if (speechBubble != null)
            speechBubble.render(sb);
        if (dialogEffect != null)
            dialogEffect.render(sb);
    }
    
    public void createSpeech(String msg) {
        if (msg == null) msg = "MISSING_TEXT";
        boolean isRight = MathUtils.randomBoolean();
        float x = scale(MathUtils.random(660F, 1260F));
        float y = Settings.HEIGHT - scale(380F);
        speechBubble = new ShopSpeechBubble(x, y, 4F, msg, isRight);
        float offsetX = isRight ? SPEECH_TEXT_R_X : SPEECH_TEXT_L_X;
        dialogEffect = new SpeechTextEffect(x + offsetX, y + SPEECH_TEXT_Y, 4F, msg, DialogWord.AppearEffect.BUMP_IN);
    }
    
    public String buyMsg() {
        return null;
    }
    
    public String cantBuyMsg() {
        return null;
    }
    
    public void playBuySfx() {
        super.playBuySfx();
    }
    public void playCantBuySfx() {
        super.playCantBuySfx();
    }
    
    public Color relicSkinColor() {
        return Color.WHITE.cpy();
    }
    
    public Texture getPriceUI() {
        return ShopType.GetPriceUI(type);
    }
}