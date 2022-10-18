package rs.lunarshop.utils;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.RelicMst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStatHelper {
    private static final String RELIC_STACK_PREFIX = "LunarRelicStack_";
    private static final String RELIC_COUNT_PREFIX = "LunarRelicCount_";
    
    private static final Map<Integer, RelicStats> RELIC_STATS_MAP = new HashMap<>();
    
    public static Prefs GetCharPref() {
        return AbstractDungeon.player.getPrefs();
    }
    
    public static void PutRelicHighestStack(AbstractLunarRelic r, int stack) {
        Prefs pref = GetCharPref();
        String key = getRelicStackKey(r);
        int oldV = pref.getInteger(key, 0);
        if (oldV > stack) return;
        pref.putInteger(getRelicStackKey(r), stack);
        pref.flush();
    }
    
    public static void PutRelicCollectCount(AbstractLunarRelic r) {
        Prefs pref = GetCharPref();
        String key = getRelicCountKey(r);
        int oldV = pref.getInteger(key, -1);
        if (oldV < 0) {
            oldV = 0;
            LunarMod.LogInfo("[" + LMSK.Player().chosenClass + "] just got first [" + r.prop.lunarID + "]");
        }
        pref.putInteger(key, oldV + 1);
    }
    
    public static RelicStats GetRelicStats(AbstractLunarRelic relic) {
        if (!RELIC_STATS_MAP.containsKey(relic.prop.lunarID)) {
            LunarMod.LogInfo("Loading lunar item [" + relic.prop.lunarID +  "] stats");
            List<Prefs> prefs = CardCrawlGame.characterManager.getAllPrefs();
            RelicStats stats = new RelicStats();
            for (Prefs p : prefs) {
                int count = p.getInteger(getRelicCountKey(relic), 0);
                int stack = p.getInteger(getRelicStackKey(relic), 0);
                stats.setValues(count, stack);
            }
            RELIC_STATS_MAP.put(relic.prop.lunarID, stats);
            return stats;
        }
        return RELIC_STATS_MAP.get(relic.prop.lunarID);
    }
    
    public static void RefreshAllRelicStats() {
        List<AbstractLunarRelic> relics = RelicMst.GetAllRelics();
        for (AbstractLunarRelic r : relics) {
            String countKey = getRelicCountKey(r);
            String stackKey = getRelicStackKey(r);
            List<Prefs> prefs = CardCrawlGame.characterManager.getAllPrefs();
            RelicStats stats = new RelicStats();
            for (Prefs p : prefs) {
                p.flush();
                int count = p.getInteger(countKey, 0);
                int stack = p.getInteger(stackKey, 0);
                stats.setValues(count, stack);
            }
            RELIC_STATS_MAP.put(r.prop.lunarID, stats);
        }
    }
    
    private static String getRelicCountKey(AbstractLunarRelic r) {
        return RELIC_COUNT_PREFIX + r.prop.lunarID;
    }
    
    private static String getRelicStackKey(AbstractLunarRelic r) {
        return RELIC_STACK_PREFIX + r.prop.lunarID;
    }
    
    public static class RelicStats {
        public int collects;
        public int maxStack;
        
        private RelicStats() {}
        
        private void setValues(int collects, int maxStack) {
            this.collects = collects;
            this.maxStack = maxStack;
        }
    }
}