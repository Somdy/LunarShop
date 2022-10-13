package rs.lunarshop.config;

import com.megacrit.cardcrawl.helpers.Prefs;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.utils.LunarUtils;

import java.util.HashMap;
import java.util.Map;

public class LoadoutConfig implements LunarUtils {
    private final Map<String, Integer> intMap = new HashMap<>();
    private final Map<String, Boolean> boolMap = new HashMap<>();
    private final Map<String, String> stringMap = new HashMap<>();
    
    public void putInt(String key, int value) {
        intMap.put(key, value);
    }
    
    public void putString(String key, String value) {
        stringMap.put(key, value);
    }
    
    public void putBool(String key, boolean value) {
        boolMap.put(key, value);
    }
    
    public int getIntValue(String key) {
        if (stringMap.containsKey(key))
            return Integer.parseInt(stringMap.get(key));
        return 0;
    }
    
    public String getString(String key) {
        return stringMap.get(key);
    }
    
    public boolean isEmpty() {
        return stringMap.isEmpty() && intMap.isEmpty();
    }
    
    public void pushToPrefs(@NotNull Prefs pref) {
        intMap.forEach((k, v) -> {
            pref.putInteger(k, v);
            deLog("Saved loadout: [" + k + ", " + v + "]");
        });
        boolMap.forEach((k, v) -> {
            pref.putBoolean(k, v);
            deLog("Saved loadout: [" + k + ", " + v + "]");
        });
        stringMap.forEach((k, v) -> {
            pref.putString(k, v);
            deLog("Saved loadout: [" + k + ", " + v + "]");
        });
    }
}