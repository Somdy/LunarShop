package rs.lunarshop.utils;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import org.jetbrains.annotations.NotNull;
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
    private static final Map<Integer, RelicStats> CACHED_RELIC_STATS_MAP = new HashMap<>();
    
    public static void PutRelicHighestStack(AbstractLunarRelic r, int stack) {
        RelicStats stats = getCachedRelicStats(r);
        int oldV = stats.maxStack;
        if (oldV > stack) return;
        stats.maxStack = stack;
        if (stats.maxStack > 0 && stats.collects <= 0) {
            LunarMod.LogInfo("[" + r.name + "] has stacks but no collects");
            stats.collects = 1;
        }
        CACHED_RELIC_STATS_MAP.put(r.prop.lunarID, stats);
    }
    
    public static void PutRelicCollectCount(AbstractLunarRelic r) {
        RelicStats stats = getCachedRelicStats(r);
        int oldV = stats.collects;
        if (oldV <= 0) {
            oldV = 0;
            LunarMod.LogInfo("[" + LMSK.Player().chosenClass + "] just got first [" + r.prop.lunarID + "]");
        }
        stats.collects = oldV + 1;
    }
    
    public static void FlushCachedStats() {
        if (!CACHED_RELIC_STATS_MAP.isEmpty()) {
            CACHED_RELIC_STATS_MAP.forEach((k,v) -> {
                LunarMod.LogInfo("Saving lunar item [" + k + "] stats {" + v.collects + ", " + v.maxStack + "}");
                LunarMod.SaveInt(getRelicCountKey(k), v.collects);
                LunarMod.SaveInt(getRelicStackKey(k), v.maxStack);
            });
            CACHED_RELIC_STATS_MAP.clear();
        }
    }
    
    public static RelicStats GetRelicStats(@NotNull AbstractLunarRelic r) {
        LunarMod.LogInfo("Loading lunar item [" + r.prop.lunarID +  "] stats");
        if (!RELIC_STATS_MAP.containsKey(r.prop.lunarID)) {
            RelicStats stats = new RelicStats(){{
                collects = LunarMod.GetInt(getRelicCountKey(r), 0);
                maxStack = LunarMod.GetInt(getRelicStackKey(r), 0);
            }};
            RELIC_STATS_MAP.put(r.prop.lunarID, stats);
            return stats;
        }
        return RELIC_STATS_MAP.get(r.prop.lunarID);
    }
    
    public static void RefreshAllRelicStats() {
        RELIC_STATS_MAP.clear();
        List<AbstractLunarRelic> relics = RelicMst.GetAllRelics();
        for (AbstractLunarRelic r : relics) {
            RelicStats stats = new RelicStats(){{
                collects = LunarMod.GetInt(getRelicCountKey(r), 0);
                maxStack = LunarMod.GetInt(getRelicStackKey(r), 0);
            }};
            RELIC_STATS_MAP.put(r.prop.lunarID, stats);
        }
    }
    
    private static RelicStats getCachedRelicStats(@NotNull AbstractLunarRelic r) {
        int lunarID = r.prop.lunarID;
        if (CACHED_RELIC_STATS_MAP.containsKey(lunarID))
            return CACHED_RELIC_STATS_MAP.get(lunarID);
        RelicStats mainStat = new RelicStats(){{
            collects = LunarMod.GetInt(getRelicCountKey(r), 0);
            maxStack = LunarMod.GetInt(getRelicStackKey(r), r.getStack());
        }};
        CACHED_RELIC_STATS_MAP.put(lunarID, mainStat);
        return mainStat;
    }
    
    private static String getRelicCountKey(@NotNull AbstractLunarRelic r) {
        return RELIC_COUNT_PREFIX + r.prop.lunarID;
    }
    
    private static String getRelicCountKey(int lunarID) {
        return RELIC_COUNT_PREFIX + lunarID;
    }
    
    private static String getRelicStackKey(@NotNull AbstractLunarRelic r) {
        return RELIC_STACK_PREFIX + r.prop.lunarID;
    }
    
    private static String getRelicStackKey(int lunarID) {
        return RELIC_STACK_PREFIX + lunarID;
    }
    
    public static class RelicStats {
        public int collects;
        public int maxStack;
        
        private RelicStats() {}
    
        private void sumValues(int collects, int maxStack) {
            this.collects += collects;
            if (maxStack > this.maxStack)
                this.maxStack = maxStack;
        }
    }
}