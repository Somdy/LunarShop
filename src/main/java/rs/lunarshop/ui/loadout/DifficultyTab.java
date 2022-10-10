package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.Prefs;
import rs.lunarshop.config.LoadoutConfig;
import rs.lunarshop.utils.LunarImageMst;

public class DifficultyTab extends LoadoutTab {
    public static final String LOCALNAME = "困难度";
    public static final Texture TAB_IMAGE = LunarImageMst.loadLocally("LunarAssets/imgs/ui/loadout/RainLevel_", 
            ".png");
    
    public DifficultyTab(LoadoutManager manager) {
        super(manager, LOCALNAME, TAB_IMAGE, GENERAL_TAB_W, GENERAL_TAB_H, GENERAL_TAB_W, GENERAL_TAB_H, Settings.scale);
        for (int i = 0; i < 3; i++) {
            putOption(new DifficultyOption(this, i));
        }
        value = options.get(0).valueInt;
    }
    
    @Override
    protected void init() {
//        Vector2 location = new Vector2(manager.loadoutHb.cX, manager.loadoutHb.y);
//        setLocation(location.x, location.y - hb.height / 2F - GAP_DIST);
        tabArea = new Hitbox(hb.x, hb.y, hb.width, 0);
        for (int i = 0; i < options.size(); i++) {
            LoadoutOption opt = options.get(i);
            opt.move(hb.cX - scale(109.5F) + scale(75F * i), hb.y + 5F);
            opt.isHidden = true;
            opt.unselect();
        }
        options.get(0).selected = true;
    }
    
    @Override
    protected void updateOnHovering() {
        for (int i = 0; i < options.size(); i++) {
            LoadoutOption opt = options.get(i);
            opt.translate(hb.cX - scale(109.5F) + scale(75F * i), hb.y - scale(84.03F));
            opt.isHidden = false;
        }
        targetArea = new Hitbox(hb.x, hb.y - scale(89F), hb.width, scale(95F));
    }
    
    @Override
    protected void updateOnUnhovering() {
        for (int i = 0; i < options.size(); i++) {
            LoadoutOption opt = options.get(i);
            opt.translate(hb.cX - scale(109.5F) + scale(75F * i), hb.y + 5F);
        }
        targetArea = new Hitbox(hb.x, hb.y + scale(GENERAL_TAB_H / 2F), 0, 0F);
    }
    
    @Override
    protected void saveConfig(LoadoutConfig config) {
        config.putInt(LoadoutManager.RAIN_KEY, getLoadoutValue());
    }
    
    @Override
    protected void loadConfig(Prefs pref) {
        int value = pref.getInteger(LoadoutManager.RAIN_KEY, 0);
        for (LoadoutOption o : options) {
            if (o.valueInt == value) {
                o.selfOnLeftClick();
                break;
            }
        }
    }
    
    protected void forceSelect(int level) {
        for (LoadoutOption o : options) {
            if (o.valueInt == level) {
                o.selfOnLeftClick();
                break;
            }
        }
    }
}