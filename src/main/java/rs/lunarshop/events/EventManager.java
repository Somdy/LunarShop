package rs.lunarshop.events;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.events.AbstractEvent;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.utils.MsgHelper;

import java.util.Properties;

public class EventManager {
    private static final int[] EventRecords = new int[10];
    
    public static void InitProperties(Properties props) {
        for (int i = 0; i < EventRecords.length; i++) {
            props.setProperty("EventRecords#" + i, Integer.toString(0));
        }
    }
    
    public static void LoadConfig(SpireConfig config) {
        MsgHelper.PreLoad("Lunar Event Records: ");
        for (int i = 0; i < EventRecords.length; i++) {
            EventRecords[i] = config.getInt("EventRecords#" + i);
            MsgHelper.Append(Integer.toString(EventRecords[i]));
        }
        MsgHelper.End();
    }
    
    public static void SaveRecords(SpireConfig config) {
        for (int i = 0; i < EventRecords.length; i++) {
            config.setInt("EventRecords#" + i, EventRecords[i]);
        }
    }
    
    protected static void AddRecord(int eventKey) {
        EventRecords[eventKey]++;
    }
    
    protected static int Record(int eventKey) {
        return EventRecords[eventKey];
    }
    
    protected static void DisableAllOptions(@NotNull AbstractEvent event) {
        event.imageEventText.optionList.forEach(o -> o.isDisabled = true);
    }
    
    protected static void EnableAllOptions(@NotNull AbstractEvent event) {
        event.imageEventText.optionList.forEach(o -> o.isDisabled = false);
    }
    
    public static boolean CanEventSpawn(int eventKey) {
        if (eventKey == CleansingPoolEvent.KEY) {
            int lunarCount = CleansingPoolEvent.countLunarItems();
            return lunarCount >= 3;
        }
        return false;
    }
}