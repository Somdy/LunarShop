package rs.lunarshop.data;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.subjects.lunarprops.LunarItemProp;
import rs.lunarshop.subjects.lunarprops.LunarNpcProp;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.MsgHelper;
import rs.lunarshop.utils.NpcHelper;

import java.nio.charset.StandardCharsets;

public class LunarDataLoader {
    
    public static void Initialize() {
        Gson gson = new Gson();
        String dataPath = "LunarAssets/data/relic_data_main.json";
        String dataJson = readJson(dataPath);
        MsgHelper.PreLoad("RELIC DATA LOADED");
        {
            RelicData[] data = gson.fromJson(dataJson, RelicData[].class);
            assert data != null;
            for (RelicData relicData : data) {
                LunarItemProp prop = createItemProp(relicData);
                ItemHelper.AddItemProp(prop);
                MsgHelper.Append(relicData.lunarID);
            }
        }
        MsgHelper.End();
        dataPath = "LunarAssets/data/vanilla_npc_stats_main.json";
        dataJson = readJson(dataPath);
        {
            NpcStats[] stats = gson.fromJson(dataJson, NpcStats[].class);
            assert stats != null;
            for (NpcStats stat : stats) {
                float[] propStats = stat.stats;
                LunarNpcProp prop = new LunarNpcProp(stat.ID, (int) propStats[0], (int) propStats[1], (int) propStats[2],
                        propStats[3], (int) propStats[4], (int) propStats[5]);
                NpcHelper.AddNpcProp(prop);
            }
        }
        dataPath = "LunarAssets/data/npc_stats_main.json";
        dataJson = readJson(dataPath);
        {
            NpcStats[] stats = gson.fromJson(dataJson, NpcStats[].class);
            assert stats != null;
            for (NpcStats stat : stats) {
                float[] propStats = stat.stats;
                LunarNpcProp prop = new LunarNpcProp(stat.ID, (int) propStats[0], (int) propStats[1], (int) propStats[2], 
                        propStats[3], (int) propStats[4], (int) propStats[5]);
                NpcHelper.AddNpcProp(prop);
            }
        }
        dataPath = "LunarAssets/data/difficulty_data_main.json";
        dataJson = readJson(dataPath);
        MsgHelper.PreLoad("DIFFICULTY MOD LOADED");
        {
            DifficultyMod[] mods = gson.fromJson(dataJson, DifficultyMod[].class);
            assert mods != null;
            for (DifficultyMod m : mods) {
                LunarMaster.AddDifficultyMod(m);
                MsgHelper.Append(m.level);
            }
        }
        MsgHelper.End();
    }
    
    @Nullable
    private static String readJson(String path) {
        if (Gdx.files.internal(path).exists()) {
            LunarMod.LogInfo("data file located in " + path);
            return Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
        }
        LunarMod.LogInfo("unable to find data file in " + path);
        return null;
    }
    
    private static LunarItemProp createItemProp(@NotNull RelicData data) {
        int lunarID = data.lunarID;
        String localID = data.localID;
        String localname = data.localname;
        LunarRarity rarity;
        switch (data.tier) {
            case 1:
                rarity = LunarRarity.COMMON;
                break;
            case 2:
                rarity = LunarRarity.UNCOMMON;
                break;
            case 3:
                rarity = LunarRarity.RARE;
                break;
            case 4:
                rarity = LunarRarity.LEGEND;
                break;
            case 5:
                rarity = LunarRarity.MYTHIC;
                break;
            default:
                rarity = LunarRarity.UNREAL;
        }
        AbstractRelic.LandingSound sound = AbstractRelic.LandingSound.valueOf(data.sound.toUpperCase());
        return new LunarItemProp(lunarID, localID, localname, rarity, sound);
    }
    
    private static class NpcStats {
        private String ID;
        private final float[] stats = new float[6];
    }
    
    private static class RelicData {
        private int lunarID;
        private String localID;
        private String localname;
        private int tier;
        private String sound;
    }
}