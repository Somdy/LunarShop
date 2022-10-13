package rs.lunarshop.vfx.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.achievements.AchvTracker;
import rs.lunarshop.abstracts.AbstractLunarAchievement;
import rs.lunarshop.utils.AudioMst;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.List;

public class AchvUnlockedEffect extends LMCustomGameEffect implements LunarUtils {
    private final List<BroadcastAchvEffect> broadcasters;
    private final AchvTracker tracker;
    
    public AchvUnlockedEffect(AchvTracker tracker) {
        this.tracker = tracker;
        broadcasters = new ArrayList<>();
    }
    
    public boolean broadcastAchv(AbstractLunarAchievement achv) {
        if (broadcastingCount() >= 4) return false;
        BroadcastAchvEffect broad = new BroadcastAchvEffect(achv);
        float sX = Settings.WIDTH + BroadcastAchvEffect.BOARD_W;
        float sY = Settings.HEIGHT * 0.45F;
        float dX = Settings.WIDTH - scale(BroadcastAchvEffect.BOARD_W / 2F);
        float dY = Settings.HEIGHT * 0.55F - scale(BroadcastAchvEffect.BOARD_H) * (broadcasters.size() - 1);
        broad.init(sX, sY);
        broad.dest = new Vector2(dX, dY);
        broadcasters.add(broad);
        playSound(AudioMst.TONE_MSG);
        return true;
    }
    
    int broadcastingCount() {
        int count = 0;
        if (!broadcasters.isEmpty()) {
            for (BroadcastAchvEffect broad : broadcasters) {
                if (!broad.isDone) count++;
            }
        }
        return count;
    }
    
    @Override
    public void update() {
        if (!broadcasters.isEmpty()) {
            List<BroadcastAchvEffect> removal = new ArrayList<>();
            BroadcastAchvEffect b = broadcasters.get(0);
            if (b.pos.x == b.dest.x && b.pos.y == b.dest.y && !b.fadingStarted)
                b.fadingStarted = true;
            for (BroadcastAchvEffect broadcast : broadcasters) {
                broadcast.update();
                if (broadcast.isDone) {
                    removal.add(broadcast);
                    repositionBroadcasters();
                }
            }
            if (!removal.isEmpty()) {
                for (BroadcastAchvEffect broadcast : removal) {
                    broadcasters.remove(broadcast);
                    tracker.onBroadcastingFinished();
                }
            }
        }
    }
    
    void repositionBroadcasters() {
        if (broadcasters.size() > 1) {
            for (int i = 1; i < broadcasters.size(); i++) {
                BroadcastAchvEffect broad = broadcasters.get(i);
                if (!broad.isDone) {
                    broad.dest.y += scale(BroadcastAchvEffect.BOARD_H + 5F);
                }
            }
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        if (!broadcasters.isEmpty()) {
            for (BroadcastAchvEffect broadcast : broadcasters) {
                broadcast.render(sb);
            }
        }
    }
    
    @Override
    public void dispose() {
        
    }
}