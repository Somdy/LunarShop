package rs.lunarshop.ui.loadout;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import rs.lunarshop.config.LoadoutConfig;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

import java.util.*;

@SuppressWarnings("unused")
public class LoadoutManager implements LunarUtils, CustomSavable<Map<String, String>> {
    private static LoadoutManager Inst = null;
    public static final Texture LOADOUT_BUTTON = LunarImageMst.loadLocally("LunarAssets/imgs/ui/loadout/LoadoutButton_", ".png");
    public static final Texture LOADOUT_BUTTON_PRESSED = LunarImageMst.loadLocally("LunarAssets/imgs/ui/loadout/LoadoutButton_pressed_", ".png");
    public static final int LOADOUT_W = 309;
    public static final int LOADOUT_H = 71;
    public static final float TAB_ANIM_SPEED = 12F;
    public static final String RAIN_KEY = "rainLevel";
    public static final String ECLIPSE_KEY = "eclipseLevel";
    public static final String ARTIFACT_KEY = "artifact_";
    protected final List<LoadoutTab> tabs = new ArrayList<>();
    protected final List<LoadoutTab> viewingTabs = new ArrayList<>();
    protected final LoadoutConfig config = new LoadoutConfig();
    protected CharacterSelectScreen css;
    protected Hitbox loadoutHb;
    private boolean pressed;
    protected float layoutTopRenderY;
    protected float layoutMidRenderY;
    protected float layoutMidRenderHeight;
    protected float layoutBotRenderY;
    private int difficultyLevel;
    private int rainLevel;
    private int eclipseLevel;
    private final List<String> selectedArtifacts = new ArrayList<>();
    
    public static LoadoutManager Inst() {
        if (Inst == null)
            Inst = new LoadoutManager();
        return Inst;
    }
    
    private LoadoutManager() {
        tabs.add(new DifficultyTab(this));
        tabs.add(new EclipseTab(this));
        tabs.add(new ArtifactTab(this));
        loadoutHb = new Hitbox(scale(LOADOUT_W), scale(LOADOUT_H));
        difficultyLevel = rainLevel + eclipseLevel;
        init();
        BaseMod.addSaveField("LoadoutManager", this);
    }
    
    public void init() {
        move(Settings.WIDTH * 0.8F, Settings.HEIGHT * 0.9F);
        for (int i = 0; i < tabs.size(); i++) {
            LoadoutTab tab = tabs.get(i);
            tab.setLocation(loadoutHb.cX, loadoutHb.y - tab.hb.height / 2F - LoadoutTab.GAP_DIST
                    - i * (tab.hb.height + LoadoutTab.GAP_DIST));
            tab.init();
        }
    }
    
    public void move(float cX, float cY) {
        loadoutHb.move(cX, cY);
    }
    
    public void captureCharSelectScreen(CharacterSelectScreen css) {
        this.css = css;
    }
    
    public void update() {
        loadoutHb.update();
        if (loadoutHb.hovered) {
            if (InputHelper.justClickedLeft) {
                loadoutHb.clickStarted = true;
                pressed = true;
                playSound("UI_HOVER");
            }
            if (loadoutHb.clicked) {
                loadoutHb.clicked = false;
                pressed = false;
                onLeftClick();
            }
        }
        if (pressed && InputHelper.justReleasedClickLeft)
            pressed = false;
        updateTabs();
        updateRenderArea();
    }
    
    private void updateTabs() {
        for (int i = 0; i < tabs.size(); i++) {
            float baseRenderY = loadoutHb.y - LoadoutTab.GAP_DIST;
            LoadoutTab tab = tabs.get(i);
            baseRenderY -= (tab.hb.height + LoadoutTab.GAP_DIST) * (i + 1);
            for (int j = 0; j < i; j++) {
                LoadoutTab otherTab = tabs.get(j);
                if (otherTab.inspectingTab && otherTab.tabArea != null) 
                    baseRenderY -= otherTab.tabArea.height;
            }
            tab.translate(loadoutHb.x + scale(9.5F), baseRenderY);
            tab.update();
            if (tab.inspectingTab || tab.hovered) {
                viewingTabs.add(tab);
            }
            if (!tab.inspectingTab) {
                viewingTabs.remove(tab);
            }
        }
    }
    
    private void updateRenderArea() {
        int last = tabs.size() - 1;
        LoadoutTab tab = tabs.get(last);
        layoutBotRenderY = tab.hb.y - LoadoutTab.GAP_DIST - scale(LunarImageMst.LAYOUT_BG_H);
        if (tab.tabArea != null) layoutBotRenderY -= tab.tabArea.height;
        layoutMidRenderY = layoutBotRenderY + LunarImageMst.LAYOUT_BG_H;
        float topLine = loadoutHb.y + loadoutHb.height + LunarImageMst.LAYOUT_BG_H;
        layoutMidRenderHeight = topLine - (layoutMidRenderY + LunarImageMst.LAYOUT_BG_H);
        layoutTopRenderY = layoutMidRenderY + layoutMidRenderHeight;
    }
    
    protected void updateTotalLevel() {
        rainLevel = tabs.get(0).getLoadoutValue();
        eclipseLevel = tabs.get(1).getLoadoutValue();
        difficultyLevel = rainLevel + eclipseLevel;
    }
    
    protected void updateSelectedGroup() {
        selectedArtifacts.clear();
        selectedArtifacts.addAll(tabs.get(2).getLoadoutValues());
    }
    
    public void render(SpriteBatch sb) {
        renderLayoutBg(sb);
        tabs.forEach(tab -> tab.render(sb));
        sb.setColor(Color.WHITE.cpy());
        sb.draw(getLoadoutButton(), loadoutHb.cX - LOADOUT_W / 2F, loadoutHb.cY - LOADOUT_H / 2F, LOADOUT_W / 2F, 
                LOADOUT_H / 2F, LOADOUT_W, LOADOUT_H, Settings.scale, Settings.scale, 
                0F, 0, 0, LOADOUT_W, LOADOUT_H, false, false);
        loadoutHb.render(sb);
    }
    
    protected void renderLayoutBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(LunarImageMst.LayoutBgTop, loadoutHb.cX - scale(LOADOUT_W) / 2F - scale(10.5F), layoutTopRenderY,
                scale(LunarImageMst.LAYOUT_BG_W), LunarImageMst.LAYOUT_BG_H);
        sb.draw(LunarImageMst.LayoutBgBot, loadoutHb.cX - scale(LOADOUT_W) / 2F - scale(10.5F), layoutBotRenderY,
                scale(LunarImageMst.LAYOUT_BG_W), LunarImageMst.LAYOUT_BG_H);
        sb.draw(LunarImageMst.LayoutBgMid, loadoutHb.cX - scale(LOADOUT_W) / 2F - scale(10.5F), layoutMidRenderY,
                scale(LunarImageMst.LAYOUT_BG_W), layoutMidRenderHeight);
    }
    
    private Texture getLoadoutButton() {
        return pressed ? LOADOUT_BUTTON_PRESSED : LOADOUT_BUTTON;
    }
    
    private void onLeftClick() {
        
    }
    
    protected LoadoutTab getTab(String localname) {
        return tabs.stream().filter(t -> localname.equals(t.localname))
                .findFirst()
                .orElse(null);
    }
    
    protected void saveConfig() {
        tabs.forEach(t -> t.saveConfig(config));
        Prefs pref = null;
        for (CharacterOption option : css.options) {
            if (option.selected) {
                deLog("Saving [" + option.name + "] loadout values");
                pref = option.c.getPrefs();
                break;
            }
        }
        if (pref != null) {
            config.pushToPrefs(pref);
            pref.flush();
        }
    }
    
    public void loadPrefsOnCharacterOptionJustSelected(CharacterOption option) {
        Prefs pref = option.c.getPrefs();
        if (pref != null) {
            deLog("Loading [" + option.c.chosenClass + "] loadout values");
            tabs.forEach(t -> t.loadConfig(pref));
            updateTotalLevel();
            updateSelectedGroup();
        }
    }
    
    public int getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public int getRainLevel() {
        return rainLevel;
    }
    
    public int getEclipseLevel() {
        return eclipseLevel;
    }
    
    public boolean isArtifactEnabled(String artifactID) {
        return selectedArtifacts.contains(artifactID);
    }
    
    @Override
    public Map<String, String> onSave() {
        log("Saving in-game loadout values");
        Map<String, String> map = new HashMap<>();
        map.put(RAIN_KEY, String.valueOf(rainLevel));
        map.put(ECLIPSE_KEY, String.valueOf(eclipseLevel));
        if (!selectedArtifacts.isEmpty()) {
            for (String ID : selectedArtifacts) {
                map.put(ARTIFACT_KEY + ID, String.valueOf(true));
            }
        }
        return map;
    }
    
    @Override
    public void onLoad(Map<String, String> map) {
        if (map != null) {
            log("Loading in-game loadout values");
            selectedArtifacts.clear();
            map.forEach((k ,v) -> {
                if (RAIN_KEY.equals(k))
                    rainLevel = Integer.parseInt(v);
                if (ECLIPSE_KEY.equals(k))
                    eclipseLevel = Integer.parseInt(v);
                if (k.startsWith(ARTIFACT_KEY)) {
                    String artifactID = k.split("_")[1];
                    if (!ARTIFACTS.MANAGED.contains(artifactID)) {
                        throw new RuntimeException("[" + artifactID + "] is not an artifact");
                    }
                    selectedArtifacts.add(artifactID);
                }
                log("loadout: [" + k + ", " + v + "]");
            });
            clearMessOnLoad();
        }
    }
    
    private void clearMessOnLoad() {
        difficultyLevel = rainLevel + eclipseLevel;
    }
    
    public static class DIFFICULTIES {
        public static final int DRIZZLE = 0;
        public static final int RAINSTORM = 1;
        public static final int MONSOON = 2;
    }
    
    public static class ECLIPSES {
        public static final int LV1 = 1;
        public static final int LV2 = 2;
        public static final int LV3 = 3;
        public static final int LV4 = 4;
        public static final int LV5 = 5;
        public static final int LV6 = 6;
        public static final int LV7 = 7;
        public static final int LV8 = 8;
    }
    
    public static class ARTIFACTS {
        public static final String SACRIFICE = "Sacrifice";
        public static final String SWARMS = "Swarms";
        public static final String EVOLUTION = "Evolution";
        public static final String KIN = "Kin";
        public static final String COMMAND = "Command";
        public static final String ENIGMA = "Enigma";
        public static final String SPITE = "Spite";
        public static final String METAMORPHOSIS = "Metamorphosis";
        
        public static final List<String> MANAGED = new ArrayList<>(Arrays.asList(
                SACRIFICE, SWARMS, EVOLUTION, KIN, COMMAND, ENIGMA, SPITE, METAMORPHOSIS
        ));
    }
}