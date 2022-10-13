package rs.lunarshop.shops;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.AbstractLunarShop;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.PotencyHelper;

import static rs.lunarshop.shops.ShopType.INVALID;
import static rs.lunarshop.shops.ShopType.ReturnRndType;

public final class ShopEventManager implements LunarUtils {
    private static final float BaseRndShopChance = 0.01F;
    private int floorLastShop;
    private int rndShop;
    private int currShop;

    public ShopEventManager() {
        floorLastShop = -1;
        rndShop = INVALID;
        currShop = INVALID;
    }
    
    public boolean isRndShopReqsMet() {
        return isBonfireReqsMet() && currAct() < 4 && PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV6);
    }

    public boolean isBonfireReqsMet() {
        return currFloor() > floorLastShop + 2 && LunarMod.HasPass() && !LMSK.Player().hasRelic("");
    }

    private float weightedRndShopChance() {
        return BaseRndShopChance + ascenLv() / (1000F + ascenLv() * 5);
    }
    
    public void openShop() {
        if (currShop == INVALID) {
            log("No shop is asked currently");
            return;
        }
        log("Opening lunar shop: " + currShop);
        AbstractDungeon.shopScreen = new LunarShop();
        AbstractDungeon.shopScreen.init(null, null);
        AbstractDungeon.shopScreen.open();
        visitTheShop();
    }
    
    public void visitTheShop() {
        currShop = INVALID;
        setFloorLastShop(currFloor());
    }
    
    public boolean isVisitingShop() {
        return currShop != INVALID && floorLastShop == currFloor() && AbstractLunarShop.visiting;
    }
    
    public ShopEventManager setFloorLastShop(int floorLastShop) {
        this.floorLastShop = floorLastShop;
        return this;
    }
    
    public void setRndShop() {
        rndShop = ReturnRndType();
    }
    
    public ShopEventManager setCurrShop(int currShop) {
        this.currShop = currShop;
        return this;
    }
    
    public int getCurrShop() {
        return currShop;
    }
    
    public int getFloorLastShop() {
        return floorLastShop;
    }
}