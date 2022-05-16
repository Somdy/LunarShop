package rs.lunarshop.achievements;

import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.subjects.AbstractLunarAchievement;
import rs.lunarshop.subjects.lunarprops.LunarAchvData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AchvManager {
    private static final List<AbstractLunarAchievement> achvList = new ArrayList<>();
    
    public static void Initialize() {
        try {
            Field[] Ids = AchvID.class.getDeclaredFields();
            for (Field id : Ids) {
                try {
                    id.setAccessible(true);
                    LunarAchvData data = (LunarAchvData) id.get(AchvID.class);
                    create(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @NotNull
    public static AbstractLunarAchievement create(LunarAchvData data) {
        AbstractLunarAchievement achv = new AbstractLunarAchievement(data);
        if (achvList.stream().noneMatch(a -> a.data.key == data.key)) {
            achvList.add(achv);
//            LunarMod.LogInfo("Achievement " + achv.title + " is loaded");
        }
        return achv;
    }
    
    @NotNull
    public static List<AbstractLunarAchievement> GetAllAchv() {
        List<AbstractLunarAchievement> tmp = new ArrayList<>();
        achvList.forEach(a -> tmp.add(a.copy()));
        return tmp;
    }
    
    public static AbstractLunarAchievement Get(int key) {
        AbstractLunarAchievement achv = null;
        Optional<AbstractLunarAchievement> opt = achvList.stream().filter(a -> a.data.key == key).findFirst();
        if (opt.isPresent())
            achv = opt.get().copy();
        return achv;
    }
}