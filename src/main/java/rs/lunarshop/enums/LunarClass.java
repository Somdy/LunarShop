package rs.lunarshop.enums;

import rs.lunarshop.shops.ShopType;

public enum LunarClass {
    LUNAR(ShopType.LUNAR), VOID(ShopType.VOID), TABOO(ShopType.TABOO), PLANET(ShopType.PLANET), SPECIAL(ShopType.INVALID);
    
    public final int type;
    
    LunarClass(int type) {
        this.type = type;
    }
}