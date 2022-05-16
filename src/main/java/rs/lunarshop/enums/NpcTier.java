package rs.lunarshop.enums;

public enum NpcTier {
    MERCHANT(3), NORMAL(0), ELITE(1), BOSS(2);
    
    public final int tier;
    
    NpcTier(int tier) {
        this.tier = tier;
    }
}