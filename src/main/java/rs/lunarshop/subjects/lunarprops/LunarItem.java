package rs.lunarshop.subjects.lunarprops;

import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.shops.ShopType;
import rs.lunarshop.utils.LunarUtils;

public class LunarItem implements LunarUtils {
    public final int lunarID;
    public final String internalID;
    private LunarRarity rarity;
    private LunarClass clazz;

    public LunarItem(int lunarID, String internalID) {
        this.lunarID = lunarID;
        this.internalID = internalID;
    }
    
    public LunarRarity getRarity() {
        return rarity;
    }
    
    public LunarItem setRarity(LunarRarity rarity) {
        this.rarity = rarity;
        return this;
    }
    
    public LunarClass getClazz() {
        return clazz;
    }
    
    public LunarItem setClazz(LunarClass clazz) {
        this.clazz = clazz;
        return this;
    }
    
    public final int shopPrice(int shopType) {
        switch (shopType) {
            case ShopType.LUNAR:
                return priceCoins(); 
            case ShopType.VOID:
                return priceBloods();
            default:
                return priceGolds();
        }
    }
    
    public final int priceCoins() {
        return rarity.coins();
    }
    
    public final int priceGolds() {
        if (clazz == LunarClass.TABOO)
            return rarity.pureGolds();
        return rarity.golds();
    }
    
    public final int priceBloods() {
        if (clazz == LunarClass.VOID)
            return rarity.pureBloods();
        return rarity.bloods();
    }
}