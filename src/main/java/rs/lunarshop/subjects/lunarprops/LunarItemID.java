package rs.lunarshop.subjects.lunarprops;

public final class LunarItemID {
    public static final int UNKNOWN = -1;
    
    public final int lunarID;
    public final String internalID;
    
    private LunarItemID(int lunarID, String internalID) {
        this.lunarID = lunarID;
        this.internalID = internalID;
    }
    
    public static LunarItemID Create(int lunarID, String internalID) {
        return new LunarItemID(lunarID, internalID);
    }
}