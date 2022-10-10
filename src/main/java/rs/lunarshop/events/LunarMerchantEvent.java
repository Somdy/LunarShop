package rs.lunarshop.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.npcs.friendly.NewtMerchant;
import rs.lunarshop.shops.LunarShop;
import rs.lunarshop.utils.NpcHelper;

public class LunarMerchantEvent extends AbstractEvent {
    public static final byte KEY = 1;
    public static final String ID = LunarMod.Prefix("LunarMerchant");
    private static final EventStrings eventStrings = LunarMod.EventStrings(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    
    private static final int BG_W = 1920;
    private static final int BG_H = 1152;
    private static Texture bg;
    
    private static final byte shopping = 0;
    private static final byte robbing = 1;
    private static final byte exploring = 2;
    private static final byte leaving = 3;
    
    private final LunarShop shop;
    
    public LunarMerchantEvent() {
        bg = ImageMaster.loadImage("LunarAssets/imgs/ui/events/bazaar_0.png");
        shop = new LunarShop(this);
        shop.init(null, null);
        body = DESCRIPTIONS[0];
        roomEventText.clear();
        roomEventText.addDialogOption(OPTIONS[0]);
        roomEventText.addDialogOption(OPTIONS[1], false);
        roomEventText.addDialogOption(OPTIONS[4], true);
        roomEventText.addDialogOption(OPTIONS[3]);
        NpcHelper.MakeMonsters(new NewtMerchant(0, 0));
        hasDialog = true;
        hasFocus = true;
    }
    
    @Override
    protected void buttonEffect(int bp) {
        switch (bp) {
            case shopping:
                AbstractDungeon.shopScreen = shop;
                AbstractDungeon.shopScreen.open();
                break;
            case robbing:
                enterCombat();
                AbstractDungeon.lastCombatMetricKey = "Newt The Merchant";
                break;
            case exploring:
                break;
            case leaving:
                openMap();
                break;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(bg, 0, 0, 0F, 0F, BG_W, BG_H, Settings.scale, Settings.scale, 
                0F, 0, 0, BG_W, BG_H, false, false);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if (bg != null) {
            bg.dispose();
        }
    }
}