package rs.lunarshop.shops;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.LunarImageMst;

public class ShopType {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(LunarMod.Prefix("ShopType"));
    public static final String[] TEXT = uiStrings.TEXT;
    public static final int INVALID = -1;
    public static final int LUNAR = 0;
    public static final int VOID = 1;
    public static final int LEGACY = 2;
    public static final int DEV = 3;
    public static final int PLANET = 4;

    protected static boolean ValidType(int type) {
        return type > INVALID && type <= LEGACY;
    }

    protected static int ReturnRndType() {
        int type1 = AbstractDungeon.eventRng.random(LUNAR, LEGACY);
        int type2 = AbstractDungeon.eventRng.random(LUNAR, LEGACY);
        return type1 == type2 ? type1 : ReturnRndType();
    }
    
    public static String GetShopName(int type) {
        switch (type) {
            case LUNAR:
                return TEXT[0];
            case VOID:
                return TEXT[1];
            case LEGACY:
                return TEXT[2];
            case DEV:
                return TEXT[3];
            default:
                return TEXT[4];
        }
    }
    
    public static Texture GetPriceUI(int type) {
        switch (type) {
            case LUNAR:
                return LunarImageMst.LunarCoin;
            case VOID:
                return ImageMaster.TP_HP;
            default:
                return ImageMaster.UI_GOLD;
        }
    }
}