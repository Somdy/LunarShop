package rs.lunarshop.utils;

import org.jetbrains.annotations.NotNull;
import rs.lunarshop.abstracts.lunarprops.LunarCardProp;
import rs.lunarshop.core.LunarMod;

import java.util.HashMap;
import java.util.Map;

public class LunarCardHelper {
    private static final Map<Integer, LunarCardProp> CardPropMap = new HashMap<>();
    private static final Map<String, Integer> CardLocalnameMap = new HashMap<>();
    
    public static void AddCardProp(@NotNull LunarCardProp prop) {
        int lunarID = prop.lunarID;
        if (CardPropMap.containsKey(lunarID))
            LunarMod.LogInfo("Replacing card [" + lunarID + "] properties");
        CardPropMap.put(lunarID, prop);
        CardLocalnameMap.put(prop.localname, lunarID);
    }
    
    public static LunarCardProp GetProp(int lunarID) {
        if (!CardPropMap.containsKey(lunarID))
            return LunarCardProp.GetReplacer();
        return CardPropMap.get(lunarID);
    }
    
    public static LunarCardProp GetProp(String localname) {
        if (CardLocalnameMap.containsKey(localname)) {
            int lunarID = CardLocalnameMap.get(localname);
            return GetProp(lunarID);
        }
        return LunarCardProp.GetReplacer();
    }
}