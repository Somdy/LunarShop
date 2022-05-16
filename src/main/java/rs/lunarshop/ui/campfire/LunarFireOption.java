package rs.lunarshop.ui.campfire;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.cards.temp.BlueCampfire;
import rs.lunarshop.items.cards.temp.GoldenCampfire;
import rs.lunarshop.items.cards.temp.PurpleCampfire;
import rs.lunarshop.items.relics.LunarPass;
import rs.lunarshop.shops.ShopType;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;

public class LunarFireOption extends AbstractCampfireOption implements LunarUtils {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("LunarFireOption"));
    public static final String[] TEXT = uiStrings.TEXT;
    private boolean exhausted;
    
    public LunarFireOption(boolean active) {
        label = TEXT[0];
        description = TEXT[active ? 1 : 0];
        usable = active;
        exhausted = !active;
        img = LunarImageMst.LunarFireOpt;
    }
    
    @Override
    public void useOption() {
        if (usable && !exhausted && LunarMod.HasPass()) {
            usable = false;
            exhausted = true;
            ArrayList<AbstractCard> list = new ArrayList<>();
            if (LunarMod.HasPass(LunarPass.LUNAR)) list.add(new BlueCampfire());
            if (LunarMod.HasPass(LunarPass.VOID)) list.add(new PurpleCampfire());
            if (LunarMod.HasPass(LunarPass.TABOO)) list.add(new GoldenCampfire());
            if (!list.isEmpty()) {
                LunarMod.addToBot(new ChooseOneAction(list));
            } else {
                log("No available options");
            }
            LunarMod.addToBot(new QuickAction(() -> {
                CardCrawlGame.metricData.addCampfireChoiceData("LunarFire", 
                        ShopType.GetShopName(LunarMaster.ShopManager.getCurrShop()));
                AbstractRoom.waitTimer = 0F;
                currRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                CampfireUI.hidden = false;
                ((RestRoom) currRoom()).campfireUI.somethingSelected = false;
            }));
        }
    }
}