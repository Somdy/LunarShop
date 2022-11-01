package rs.lunarshop.localizations;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.LunarTip;
import rs.lunarshop.utils.LunarUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class LunarLocalLoader implements LunarUtils {
    private static String lang;
    private static SAXReader reader;
    private static final AchvLocals achievements = new AchvLocals();
    private static final Map<String, ArtifactLocals> ArtifactLocalMap = new HashMap<>();
    private static final Map<String, LunarTipLocals> TipLocalMap = new HashMap<>();
    private static final Map<String, LunarCardLocals> CardLocalMap = new HashMap<>();
    
    public static void Initialize() {
        long time = System.currentTimeMillis();
        lang = LunarUtils.GetLang();
    
        try {
            loadXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        Gson gson = new Gson();
        String jsonPath = getJsonPath("artifacts.json");
        String jsonData = loadJson(jsonPath);
        ArtifactLocals[] artifactLocals = gson.fromJson(jsonData, ArtifactLocals[].class);
        assert artifactLocals != null;
        for (ArtifactLocals local : artifactLocals) {
            ArtifactLocalMap.put(local.ID, local);
        }
        jsonPath = getJsonPath("tips.json");
        jsonData = loadJson(jsonPath);
        LunarTipLocals[] tipLocals = gson.fromJson(jsonData, LunarTipLocals[].class);
        assert tipLocals != null;
        for (LunarTipLocals local : tipLocals) {
            TipLocalMap.put(local.ID, local);
        }
        jsonPath = getJsonPath("lunar_cards.json");
        jsonData = loadJson(jsonPath);
        LunarCardLocals[] cardLocals = gson.fromJson(jsonData, LunarCardLocals[].class);
        assert cardLocals != null;
        for (LunarCardLocals local : cardLocals) {
            CardLocalMap.put(local.ID, local);
        }
    
        LunarMod.LogInfo("lunar local strings are loaded: " + (System.currentTimeMillis() - time) + " ms");
    }
    
    static String getJsonPath(String fileName) {
        return "LunarAssets/locals/" + lang + "/" + fileName;
    }
    
    static String loadJson(String path) {
        if (!Gdx.files.internal(path).exists())
            LunarMod.LogInfo("Localization does not exist: " + path);
        return Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
    }
    
    static void loadXML() throws Exception {
        reader = new SAXReader();
        Element element = loadElementFromPath("LunarAssets/locals/" + lang + "/achievements");
        achievements.copyData(element);
    }
    
    static Element loadElementFromPath(String path) throws Exception {
        if (Gdx.files.internal(path).exists()) {
            LunarMod.LogInfo("Target data file located: " + path);
        }
        URL url = loadFileFromString(path);
        return rootElement(url);
    }
    
    static Element rootElement(URL path) throws DocumentException {
        return reader.read(path).getRootElement();
    }
    
    static URL loadFileFromString(String path) {
        return LunarLocalLoader.class.getClassLoader().getResource(path);
    }
    
    public static AchvLocals.SingleData GetAchvLocales(int key) {
        return achievements.getData(key);
    }
    
    public static ArtifactLocals GetArtifactLocal(String ID) {
        if (ArtifactLocalMap.containsKey(ID))
            return ArtifactLocalMap.get(ID);
        return ArtifactLocals.MockingLocals();
    }
    
    public static LunarTipLocals GetTipBuilder(String ID) {
        if (TipLocalMap.containsKey(ID))
            return TipLocalMap.get(ID);
        return LunarTipLocals.MockingTip();
    }
    
    public static LunarCardLocals GetCardLocal(String ID) {
        if (CardLocalMap.containsKey(ID))
            return CardLocalMap.get(ID);
        return LunarCardLocals.MockingLocals();
    }
}