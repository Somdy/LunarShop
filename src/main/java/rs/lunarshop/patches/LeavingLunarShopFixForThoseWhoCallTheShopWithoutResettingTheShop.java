package rs.lunarshop.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarShop;

public class LeavingLunarShopFixForThoseWhoCallTheShopWithoutResettingTheShop {
    
    @SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
    public static class CloseScreenPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP 
                    && AbstractDungeon.shopScreen instanceof AbstractLunarShop
                    && AbstractLunarShop.visiting) {
                AbstractLunarShop.visiting = false;
                LunarMod.LogInfo("Leaving a lunar shop, returning vanilla shop");
                AbstractDungeon.shopScreen = new ShopScreen();
            }
        }
    }
}