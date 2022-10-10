package rs.lunarshop.localizations;

import org.dom4j.Element;
import rs.lunarshop.utils.LunarUtils;

import java.util.HashMap;
import java.util.Map;

public class AchvLocals implements LunarUtils {
    final Map<Integer, SingleData> dataMap;
    
    public AchvLocals() {
        dataMap = new HashMap<>();
    }
    
    public SingleData getData(int key) {
        if (dataMap.containsKey(key))
            return dataMap.get(key);
        log("Unable to find " + key + " achievement locales");
        return new SingleData(key);
    }
    
    public final AchvLocals copyData(Element data) {
        if (!data.getName().equals("LunarAchv")) {
            log(data.getName() + " is not a valid lunar achievement locales");
            throw new IllegalArgumentException();
        }
        String v = data.attributeValue("v");
        log("Loading achievement locales: " + v);
        voidrun(() -> {
            for (Element e : data.elements()) {
                if (e.getName().equals("item") && e.attribute("key") != null) {
                    SingleData sData = new SingleData(Integer.parseInt(e.attributeValue("key")));
                    if (e.element("title") != null)
                        sData.title = e.elementText("title");
                    if (e.element("description") != null)
                        sData.description = e.elementText("description");
                    dataMap.put(sData.key, sData);
                }
            }
            return null;
        });
        return this;
    }
    
    public class SingleData {
        public final int key;
        private String title;
        private String description;
        
        private SingleData(int key) {
            this.key = key;
            title = "MISSING_TITLE";
            description = "MISSING_INFO";
        }
    
        public String getTitle() {
            return title;
        }
    
        public SingleData setTitle(String title) {
            this.title = title;
            return this;
        }
    
        public String getDescription() {
            return description;
        }
    
        public SingleData setDescription(String description) {
            this.description = description;
            return this;
        }
    }
}