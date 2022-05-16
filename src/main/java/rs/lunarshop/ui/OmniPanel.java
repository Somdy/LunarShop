package rs.lunarshop.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.ArmorHelper;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.RegenHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class OmniPanel implements LunarUtils {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(LunarMod.Prefix("TopPanel"));
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];
    public static final String AVAILABLE = TEXT[12];
    public static final String UNAVAILABLE = TEXT[13];
    private static float FX;
    private static float IY;
    private static float IW;
    private final Hitbox infoHb;
    private Vector2 pos;
    private float bufferTime;
    private boolean showingTips;
    private boolean enabled;
    private final ArrayList<PowerTip> tips;
    private float rotation;
    private boolean init;
    
    public OmniPanel() {
        infoHb = new Hitbox(64F, 64F);
        pos = new Vector2();
        bufferTime = 0F;
        showingTips = false;
        enabled = false;
        tips = new ArrayList<>();
        rotation = 0F;
        init = false;
    }
    
    public void init() {
        log("Initializing Omni Panel");
        pos = new Vector2();
        if (AbstractDungeon.isAscensionMode && ascenLv() > 0) {
            pos.x = AbstractDungeon.topPanel.ascensionHb.cX + scale(130F);
            pos.y = AbstractDungeon.topPanel.ascensionHb.cY;
        } else {
            log("No ascension detected");
            try {
                Field floorX = TopPanel.class.getDeclaredField("floorX");
                floorX.setAccessible(true);
                FX = floorX.getFloat(AbstractDungeon.topPanel);
                Field ICON_Y = TopPanel.class.getDeclaredField("ICON_Y");
                Field ICON_W = TopPanel.class.getDeclaredField("ICON_W");
                ICON_Y.setAccessible(true);
                ICON_W.setAccessible(true);
                IY = ICON_Y.getFloat(AbstractDungeon.topPanel);
                IW = ICON_W.getFloat(AbstractDungeon.topPanel);
                pos.x = FX + scale(200F);
                pos.y = IY +  IW / 2F;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        infoHb.move(pos.x, pos.y);
        init = true;
    }
    
    public void update() {
        if (!init) {
            init();
            return;
        }
        updatePosition();
        updateInfoHb();
        updateBufferTime();
        updateRotation();
    }
    
    private void updatePosition() {
        if (AbstractDungeon.isAscensionMode && ascenLv() > 0) {
            pos.x = AbstractDungeon.topPanel.ascensionHb.cX - scale(128F);
            pos.y = AbstractDungeon.topPanel.ascensionHb.cY;
        } else {
            pos.x = FX + scale(200F);
            pos.y = IY +  IW/ 2F;
        }
        infoHb.move(pos.x, pos.y);
    }
    
    private void updateRotation() {
        if (showingTips || enabled) {
            rotation += Gdx.graphics.getDeltaTime() * 25F;
        }
        if (!enabled && !showingTips && rotation % 360F != 0) {
            rotation -= Gdx.graphics.getDeltaTime() * (50F + rotation % 360);
            if (rotation % 360F <= 1F) rotation = 0;
        }
    }
    
    private void updateBufferTime() {
        if (bufferTime > 0F) {
            if (bufferTime > 0.1F) {
                bufferTime -= Gdx.graphics.getDeltaTime() * 0.1F;
            } else {
                bufferTime = 0F;
            }
        }
    }
    
    private void updateInfoHb() {
        infoHb.update();
        if (infoHb.justHovered)
            CardCrawlGame.sound.play("UI_HOVER");
        if (infoHb.hovered) {
            showingTips = true;
            updateTips();
            if (InputHelper.justClickedLeft && bufferTime <= 0) {
                infoHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
        }
        if (infoHb.clicked && bufferTime <= 0) {
            infoHb.clicked = false;
            enabled = !enabled;
            bufferTime = 2F;
            checkLunarItems();
        }
        if (!infoHb.hovered && showingTips) {
            showingTips = false;
            bufferTime = 0.1F;
        }
    }
    
    private void checkLunarItems() {
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof AbstractLunarRelic)
                ((AbstractLunarRelic) r).callByOmniPanelCheck();
        }
    }
    
    private void updateTips() {
        tips.clear();
        String header = TEXT[0];
        String body = constructTips();
        tips.add(new PowerTip(header, body));
    }
    
    @NotNull
    private String constructTips() {
        int attack = LunarMaster.Attack(true);
        int regen = RegenHelper.GetRegen(LMSK.Player());
        int armor = ArmorHelper.GetArmor(LMSK.Player());
        float armorRate = ArmorHelper.DamageMultiplier(LMSK.Player());
        int coins = LunarMaster.LunarCoin();
        float luck = LunarMaster.Luck();
        StringBuilder sbr = new StringBuilder();
        sbr.append(enabled ? TEXT[2] : TEXT[1]).append(TEXT[3]);
        sbr.append(" NL ");
        sbr.append(TEXT[14]);
        sbr.append(" NL ");
        sbr.append(TEXT[16]).append(attack);
        sbr.append(" NL ");
        sbr.append(TEXT[4]).append(armor).append(" | ").append(SciPercent(1 - armorRate)).append("%");
        sbr.append(" NL ");
        sbr.append(TEXT[17]).append(regen);
        sbr.append(" NL ");
        sbr.append(TEXT[6]).append(luck);
        sbr.append(" NL ");
        sbr.append(TEXT[15]);
        sbr.append(" NL ");
        sbr.append(TEXT[5]).append(coins);
        sbr.append(" NL ");
        if (LunarMod.HasPass()) {
            if (LunarMaster.ShopManager.isBonfireReqsMet()) {
                sbr.append(TEXT[7]);
            } else {
                sbr.append(String.format(TEXT[8], (Math.max(LunarMaster.ShopManager.getFloorLastShop(), 0) + 3) - currFloor()));
            }
        } else {
            sbr.append(TEXT[11]);
        }
        sbr.append(" NL ");
        sbr.append(TEXT[9]).append(LunarMaster.ShopManager.isRndShopReqsMet() ? AVAILABLE : UNAVAILABLE);
        return sbr.toString();
    }
    
    public void render(SpriteBatch sb) {
        infoHb.render(sb);
        sb.setColor(Color.WHITE.cpy());
        float scale = scale(0.85F);
        sb.draw(LunarImageMst.InfoBg, infoHb.x, infoHb.y, 32F, 32F, 64F, 64F, scale, scale, 
                0, 0, 0, 64, 64, false, false);
        sb.draw(LunarImageMst.InfoGear, infoHb.x, infoHb.y, 32F, 32F, 64F, 64F, scale, scale,
                rotation, 0, 0, 64, 64, false, false);
        if (showingTips) {
            TipHelper.queuePowerTips(InputHelper.mX + scale(32F), InputHelper.mY - scale(32F), tips);
        }
//        CardCrawlGame.cursor.render(sb);
    }
    
    public boolean showDetails() {
        return enabled;
    }
}