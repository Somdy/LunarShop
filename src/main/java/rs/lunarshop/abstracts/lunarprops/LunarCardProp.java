package rs.lunarshop.abstracts.lunarprops;

import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.lunarshop.core.LunarMod;

import java.util.Objects;

public class LunarCardProp {
    public final int lunarID;
    private final String localID;
    public final String localname;
    public final AbstractCard.CardRarity rarity;
    public final AbstractCard.CardType type;
    private final int[] costValues;
    private final int[][] damageValues;
    private final int[][] blockValues;
    private final int[][] magicValues;
    
    public LunarCardProp(int lunarID, String localID, String localname, AbstractCard.CardRarity rarity, AbstractCard.CardType type, 
                         int[] costValues, int[][] damageValues, int[][] blockValues, int[][] magicValues) {
        this.lunarID = lunarID;
        this.localID = localID;
        this.localname = localname;
        this.rarity = rarity;
        this.type = type;
        this.costValues = costValues;
        this.damageValues = damageValues;
        this.blockValues = blockValues;
        this.magicValues = magicValues;
    }
    
    public String getLocalID() {
        return localID;
    }
    
    public String getGameID() {
        return LunarMod.Prefix(localID);
    }
    
    public int getCost(boolean upgraded) {
        return costValues[upgraded ? 1 : 0];
    }
    
    public int getDamage(boolean upgrade) {
        return damageValues[0][upgrade ? 1 : 0];
    }
    
    public int getExtraDamage(boolean upgrade) {
        return damageValues[1][upgrade ? 1 : 0];
    }
    
    public int getBlock(boolean upgrade) {
        return blockValues[0][upgrade ? 1 : 0];
    }
    
    public int getExtraBlock(boolean upgrade) {
        return blockValues[1][upgrade ? 1 : 0];
    }
    
    public int getMagic(boolean upgrade) {
        return magicValues[0][upgrade ? 1 : 0];
    }
    
    public int getExtraMagic(boolean upgrade) {
        return magicValues[1][upgrade ? 1 : 0];
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LunarCardProp that = (LunarCardProp) o;
        return lunarID == that.lunarID;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(lunarID);
    }
    
    public static LunarCardProp GetReplacer() {
        return new LunarCardProp(-1, "MISSING_CARD", "MISSING_CARD", AbstractCard.CardRarity.SPECIAL, 
                AbstractCard.CardType.SKILL, new int[]{-2}, new int[][]{{0,0,0},{0,0,0}}, new int[][]{{0,0,0},{0,0,0}}, new int[][]{{0,0,0},{0,0,0}});
    }
}