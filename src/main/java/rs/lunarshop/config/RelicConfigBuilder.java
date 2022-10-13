package rs.lunarshop.config;

import rs.lunarshop.abstracts.AbstractLunarRelic;

import java.util.Map;

public final class RelicConfigBuilder {
    private final LunarConfig config;
    
    public RelicConfigBuilder(AbstractLunarRelic relic) {
        this.config = new LunarConfig(relic.prop.lunarID);
    }
    
    public RelicConfigBuilder alterValue(boolean alter, int value1, int value2) {
        return value(alter ? value1 : value2);
    }
    
    public RelicConfigBuilder value(int value) {
        config.setValue(value);
        return this;
    }
    
    public RelicConfigBuilder bool(boolean bool) {
        config.setBool(bool);
        return this;
    }
    
    public RelicConfigBuilder string(String string) {
        config.setString(string);
        return this;
    }
    
    public RelicConfigBuilder map(Map<String, String> map) {
        config.setMap(map);
        return this;
    }
    
    public RelicConfigBuilder map(String key, String value) {
        config.setMap(key, value);
        return this;
    }
    
    public LunarConfig build() {
        return config;
    }
}