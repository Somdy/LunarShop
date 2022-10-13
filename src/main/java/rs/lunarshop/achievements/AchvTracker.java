package rs.lunarshop.achievements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.abstracts.AbstractLunarAchievement;
import rs.lunarshop.utils.MsgHelper;
import rs.lunarshop.utils.PotencyHelper;
import rs.lunarshop.vfx.misc.AchvUnlockedEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AchvTracker {
    public final boolean[] PREF = new boolean[50];
    public final AchvUnlockedEffect effect;
    public final List<AbstractLunarAchievement> hanging;
    
    public AchvTracker() {
        effect = new AchvUnlockedEffect(this);
        hanging = new ArrayList<>();
    }
    
    public void initProperties(Properties props) {
        for (int i = 0; i < PREF.length; i++) {
            props.setProperty("LunarAchv#" + i, Boolean.toString(PREF[i]));
        }
    }
    
    public void loadConfig(SpireConfig config) {
        MsgHelper.PreLoad("Lunar Achv: ");
        for (int i = 0; i < PREF.length; i++) {
            PREF[i] = config.getBool("LunarAchv#" + i);
            if (PREF[i]) MsgHelper.Append(i);
        }
        MsgHelper.End();
    }
    
    public void checkUnlocks() {
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1))
            unlockAchv(AchvID.NewtFirstTime.key);
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV5))
            unlockAchv(AchvID.LunarBusiness.key);
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV6))
            unlockAchv(AchvID.OutsideLand.key);
    }
    
    public void unlockAchv(int key) {
        if (!PREF[key]) {
            LunarMod.LogInfo("Unlocking achievement: " + key);
            PREF[key] = true;
            LunarMod.SaveBool("LunarAchv#" + key, true);
            AbstractLunarAchievement achv = AchvManager.Get(key);
            if (achv != null) {
                if (!effect.broadcastAchv(achv)) hanging.add(achv);
            }
        }
    }
    
    public void onBroadcastingFinished() {
        if (!hanging.isEmpty()) {
            List<AbstractLunarAchievement> removal = new ArrayList<>();
            for (AbstractLunarAchievement achv : hanging) {
                if (effect.broadcastAchv(achv))
                    removal.add(achv);
            }
            hanging.removeAll(removal);
        }
    }
    
    public void update() {
        effect.update();
    }
    
    public void render(SpriteBatch sb) {
        effect.render(sb);
    }
    
    public boolean isAchvUnlocked(int key) {
        return PREF[key];
    }
}