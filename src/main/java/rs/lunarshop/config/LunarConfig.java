package rs.lunarshop.config;

import java.util.HashMap;
import java.util.Map;

public class LunarConfig {
      private final int saver;
      private int value;
      private boolean bool;
      private String string;
      private Map<String, String> map;
      
      protected LunarConfig(int saver, int value, boolean bool, String string) {
            this.saver = saver;
            this.value = value;
            this.bool = bool;
            this.string = string;
            this.map = new HashMap<>();
      }
      
      protected LunarConfig(int saver) {
            this(saver, -1, false, null);
      }
      
      public int getSaver() {
            return saver;
      }
      
      public int getValue() {
            return value;
      }
      
      public void setValue(int value) {
            this.value = value;
      }
      
      public boolean isBool() {
            return bool;
      }
      
      public void setBool(boolean bool) {
            this.bool = bool;
      }
      
      public String getString() {
            return string;
      }
      
      public void setString(String string) {
            this.string = string;
      }
      
      public Map<String, String> getMap() {
            return map;
      }
      
      public String getMapValue(Object key) {
            return map.get(key);
      }
      
      public boolean hasMapKey(String key) {
            return map.containsKey(key);
      }
      
      public void setMap(Map<String, String> map) {
            this.map = map;
      }
      
      public void setMap(String key, String value) {
            map.put(key, value);
      }
}