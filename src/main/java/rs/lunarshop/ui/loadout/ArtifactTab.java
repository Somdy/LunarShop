package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.Prefs;
import rs.lunarshop.config.LoadoutConfig;
import rs.lunarshop.utils.LunarImageMst;

import java.util.ArrayList;
import java.util.List;

import static rs.lunarshop.ui.loadout.LoadoutManager.*;

public class ArtifactTab extends LoadoutTab {
    public static final String LOCALNAME = "神器";
    public static final Texture TAB_IMAGE = LunarImageMst.loadLocally("LunarAssets/imgs/ui/loadout/Artifacts_",
            ".png");
    
    protected final List<LoadoutOption> locked = new ArrayList<>();
    
    public ArtifactTab(LoadoutManager manager) {
        super(manager, LOCALNAME, TAB_IMAGE, GENERAL_TAB_W, GENERAL_TAB_H, GENERAL_TAB_W, GENERAL_TAB_H, Settings.scale);
        putArtifact(ARTIFACTS.SACRIFICE, true);
        putArtifact(ARTIFACTS.SWARMS, true);
        putArtifact(ARTIFACTS.EVOLUTION, true);
        
        putArtifact(ARTIFACTS.KIN, true);
        putArtifact(ARTIFACTS.COMMAND, true);
        putArtifact(ARTIFACTS.ENIGMA, true);
        putArtifact(ARTIFACTS.SPITE, true);
        putArtifact(ARTIFACTS.METAMORPHOSIS, true);
    }
    
    private void putArtifact(String ID, boolean unlocked) {
        if (unlocked) {
            putOption(new ArtifactOption(this, ID, true));
        } else {
            locked.add(new ArtifactOption(this, ID, false));
        }
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
            if (col % 4 == 0 && i < options.size() - 1) {
                col = 0;
                row++;
            }
        }
        float height = GAP_DIST + scale(70F * (row + 1));
        targetArea = new Hitbox(hb.x, hb.y - height, hb.width, height);
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
        for (LoadoutOption o : options) {
            config.putBool(ARTIFACT_KEY + o.valueStr, selectedValues.contains(o.valueStr));
        }
    }
    
    @Override
    protected void loadConfig(Prefs pref) {
        for (LoadoutOption o : options) {
            if (pref.getBoolean(ARTIFACT_KEY + o.valueStr, false)) {
                if (!selectedValues.contains(o.valueStr)) {
                    o.selfOnLeftClick();
                }
            }
        }
    }
}
