package rs.lunarshop.localizations;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.Settings;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import rs.lunarshop.utils.LunarUtils;

import java.net.URL;

public final class LocaleManager implements LunarUtils {
    private static String lang;
    private static SAXReader reader;
    private final AchvLocales achievements;
    
    public LocaleManager() {
        achievements = new AchvLocales();
        init();
    }
    
    private void init() {
        long time = System.currentTimeMillis();
        lang = getSupportedLanguage(Settings.language);
        
        try {
            loadXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        log("ALL locale data is loaded: " + (System.currentTimeMillis() - time) + " ms");
    }
    
    void loadXML() throws Exception {
        reader = new SAXReader();
        Element element = LoadElementFromPath("LunarAssets/locals/" + lang + "/achievements");
        achievements.copyData(element);
    }
    
    Element LoadElementFromPath(String path) throws Exception {
        if (Gdx.files.internal(path).exists()) {
            log("Target data file located: " + path);
        }
        URL url = LoadFileFromString(path);
        return RootElement(url);
    }
    
    Element RootElement(URL path) throws DocumentException {
        return reader.read(path).getRootElement();
    }
    
    URL LoadFileFromString(String path) {
        return LocaleManager.class.getClassLoader().getResource(path);
    }
    
    public AchvLocales.SingleData getAchvLocales(int key) {
        return achievements.getData(key);
    }
}