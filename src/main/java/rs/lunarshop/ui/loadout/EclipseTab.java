package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.Prefs;
import rs.lunarshop.config.LoadoutConfig;
import rs.lunarshop.utils.LunarImageMst;

public class EclipseTab extends LoadoutTab {
    public static final String LOCALNAME = "日食";
    public static final Texture TAB_IMAGE = LunarImageMst.loadLocally("LunarAssets/imgs/ui/loadout/EclipseLevel_",
            ".png");
    
    public EclipseTab(LoadoutManager manager) {
        super(manager, LOCALNAME, TAB_IMAGE, GENERAL_TAB_W, GENERAL_TAB_H, GENERAL_TAB_W, GENERAL_TAB_H, Settings.scale);
        for (int i = 1; i <= 8; i++) {
            putOption(new EclipseOption(this, i));
        }
        value = 0;
    }
    
    @Override
    protected void init() {
        tabArea = new Hitbox(hb.x, hb.y, hb.width, 0);
        int col = 0;
        for (int i = 0; i < options.size(); i++) {
            LoadoutOption opt = options.get(i);
            opt.move(hb.cX - scale(126.5F) + scale(62F * col), hb.y + 5F);
            opt.isHidden = true;
            opt.unselect();
            col++;
            if (col % 4 == 0) {
                col = 0;
            }
        }
    }
    
    @Override
    protected void updateOnHovering() {
        int row = 0;
        int col = 0;
        for (int i = 0; i < options.size(); i++) {
            LoadoutOption opt = options.get(i);
            float renderY = hb.y - scale(78.03F) - scale(67F) * row;
            opt.translate(hb.cX - scale(126.5F) + scale(62F * col), renderY);
            opt.isHidden = false;
            col++;
            if (col % 4 == 0) {
                col = 0;
                row++;
            }
        }
        targetArea = new Hitbox(hb.x, hb.y - scale(156F), hb.width, scale(156F));
    }
    
    @Override
    protected void updateOnUnhovering() {
        int col = 0;
        for (int i = 0; i < options.size(); i++) {
            LoadoutOption opt = options.get(i);
            opt.translate(hb.cX - scale(126.5F) + scale(62F * col), hb.y + 5F);
            col++;
            if (col % 4 == 0) {
                col = 0;
            }
        }
        targetArea = new Hitbox(hb.x, hb.y + scale(GENERAL_TAB_H / 2F), 0, 0);
    }
    
    @Override
    protected void saveConfig(LoadoutConfig config) {
        config.putInt(LoadoutManager.ECLIPSE_KEY, getLoadoutValue());
    }
    
    @Override
    protected void loadConfig(Prefs pref) {
        int value = pref.getInteger(LoadoutManager.ECLIPSE_KEY);
        if (value < 0) return;
        for (LoadoutOption o : options) {
            if (o.valueInt == value) {
                o.selfOnLeftClick();
                break;
            }
        }
    }
    
    protected void removeEclipseMode() {
        for (LoadoutOption o : options) {
            if (o.selected && o instanceof EclipseOption) {
                if (!((EclipseOption) o).forcedSelected)
                    o.selfOnLeftClick();
            }
        }
    }
}