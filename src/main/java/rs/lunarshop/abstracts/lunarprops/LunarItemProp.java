package rs.lunarshop.abstracts.lunarprops;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarClass;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.shops.ShopType;

import java.util.Objects;

public final class LunarItemProp {
    public final int lunarID;
    public final String localID;
    public final String localname;
    public final LunarRarity rarity;
    public final AbstractRelic.LandingSound sound;
    public final int popupIcon;
    private LunarClass clazz;
    
    public LunarItemProp(int lunarID, String localID, String localname, LunarRarity rarity, AbstractRelic.LandingSound sound, int popupIcon) {
        this.lunarID = lunarID;
        this.localID = localID;
        this.localname = localname;
        this.rarity = rarity;
        this.sound = sound;
        this.popupIcon = popupIcon;
    }
    
    public LunarItemProp setClazz(LunarClass clazz) {
        this.clazz = clazz;
        return this;
    }
    
    public LunarClass getClazz() {
        return clazz;
    }
    
    public String getGameID() {
        return LunarMod.Prefix(localID);
    }
    
    public LunarRarity getRarity() {
        return rarity;
    }
    
    public int getTier() {
        return rarity.tier();
    }
    
    public int shopPrice(int shopType) {
        switch (shopType) {
            case ShopType.LUNAR:
                return priceCoins();
            case ShopType.VOID:
                return priceBloods();
            default:
                return priceGolds();
        }
    }
    
    public int priceCoins() {
        return rarity.coins();
    }
    
    public int priceGolds() {
        return rarity.golds();
    }
    
    public int priceBloods() {
        if (clazz == LunarClass.VOID)
            return rarity.pureBloods();
        return rarity.bloods();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LunarItemProp that = (LunarItemProp) o;
        return lunarID == that.lunarID && localID.equals(that.localID) && clazz == that.clazz;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lunarID, localID, clazz);
    }
    
    @NotNull
    public static LunarItemProp GetReplacer() {
        return new LunarItemProp(-1, "MISSING_ITEM", "MISSING_ITEM", LunarRarity.UNREAL, AbstractRelic.LandingSound.FLAT, 6);
    }
}