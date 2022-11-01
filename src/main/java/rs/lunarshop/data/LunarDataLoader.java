package rs.lunarshop.data;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.lunarshop.abstracts.lunarprops.LunarCardProp;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.abstracts.lunarprops.LunarItemProp;
import rs.lunarshop.abstracts.lunarprops.LunarNpcProp;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.LunarCardHelper;
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
        dataPath = "LunarAssets/data/card_data_main.json";
        dataJson = readJson(dataPath);
        MsgHelper.PreLoad("CARD DATA LOADED");
        {
            CardData[] data = gson.fromJson(dataJson, CardData[].class);
            assert data != null;
            for (CardData cardData : data) {
                LunarCardProp prop = createCardProp(cardData);
                LunarCardHelper.AddCardProp(prop);
                MsgHelper.Append(prop.lunarID);
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
        int popupIcon = data.popupIcon;
        return new LunarItemProp(lunarID, localID, localname, rarity, sound, popupIcon);
    }
    
    @NotNull
    private static LunarCardProp createCardProp(@NotNull CardData data) {
        int lunarID = data.lunarID;
        String localID = data.localID;
        String localname = data.localname;
        AbstractCard.CardRarity rarity = AbstractCard.CardRarity.valueOf(data.rarity.toUpperCase());
        AbstractCard.CardType type = AbstractCard.CardType.valueOf(data.type.toUpperCase());
        int[] costValues = new int[]{data.values[0][0], data.values[0].length > 1 ? data.values[0][1] : data.values[0][0]};
        int[][] damageValues = buildCardValues(data.values[1], data.values[4]);
        int[][] blockValues = buildCardValues(data.values[2], data.values[5]);
        int[][] magicValues = buildCardValues(data.values[3], data.values[6]);
        return new LunarCardProp(lunarID, localID, localname, rarity, type, costValues, damageValues, blockValues, magicValues);
    }
    
    private static int[][] buildCardValues(@NotNull int[] src, @NotNull int[] eSrc) {
        return new int[][]{{src[0], src.length > 1 ? src[1] : 0}, {eSrc[0], eSrc.length > 1 ? eSrc[1] : 0}};
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
        private int popupIcon;
    }
    
    private static class CardData {
        private int lunarID;
        private String localID;
        private String localname;
        private String rarity;
        private String type;
        private int[][] values;
    }
}