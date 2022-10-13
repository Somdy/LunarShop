package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import rs.lunarshop.localizations.LunarLocalLoader;
import rs.lunarshop.localizations.LunarTipLocals;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarTip;

public class DifficultyOption extends LoadoutOption {
    public static final LunarTipLocals tipBuilder = LunarLocalLoader.GetTipBuilder("DifficultyOption");
    public static final int IMAGE_S = 48;
    public static final int FRAME_S = 68;
    
    public DifficultyOption(DifficultyTab tab, int level) {
        super(tab, LunarImageMst.RainLevelOf(level), IMAGE_S, IMAGE_S, 68F, 68F, Settings.scale);
        this.valueInt = level;
        makeLunarTip(tipBuilder, valueInt, getTipColor(), LunarTip.B_DEFAULT);
    }
    
    @Override
    protected void selfOnLeftClick() {
        if (!selected) {
            selected = true;
            for (LoadoutOption opt : tab.options) {
                if (opt != this)
                    opt.selected = false;
            }
            tab.value = valueInt;
            if (valueInt < LoadoutManager.DIFFICULTIES.MONSOON) {
                LoadoutTab eTab = tab.manager.getTab(EclipseTab.LOCALNAME);
                if (eTab instanceof EclipseTab) {
                    ((EclipseTab) eTab).removeEclipseMode();
                }
            }
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (!isHidden) {
            sb.setColor(Color.WHITE.cpy());
            if (!selected) {
                if (!hovered) {
                    sb.draw(LunarImageMst.RainLevelFrame, hb.cX - FRAME_S / 2F, hb.cY - FRAME_S / 2F, FRAME_S / 2F, 
                            FRAME_S / 2F, FRAME_S, getRenderHeight(FRAME_S), scale, scale, 0F, 0, 
                            FRAME_S - getSrcRenderHeight(FRAME_S), FRAME_S, getSrcRenderHeight(FRAME_S), 
                            false, false);
                } else {
                    sb.draw(LunarImageMst.RainLevelHoveredFrame, hb.cX - FRAME_S / 2F, hb.cY - FRAME_S / 2F, 
                            FRAME_S / 2F, FRAME_S / 2F, FRAME_S, getRenderHeight(FRAME_S), scale, scale, 0F, 
                            0, FRAME_S - getSrcRenderHeight(FRAME_S), FRAME_S, getSrcRenderHeight(FRAME_S), 
                            false, false);
                }
            }
            if (selected) {
                sb.setColor(Color.YELLOW.cpy());
                sb.draw(LunarImageMst.LoadoutSelectedOutline, hb.cX - LunarImageMst.LOADOUT_SELECTED_S / 2F, 
                        hb.cY - LunarImageMst.LOADOUT_SELECTED_S / 2F, LunarImageMst.LOADOUT_SELECTED_S / 2F, 
                        LunarImageMst.LOADOUT_SELECTED_S / 2F, LunarImageMst.LOADOUT_SELECTED_S, 
                        getRenderHeight(LunarImageMst.LOADOUT_SELECTED_S), scale, scale, 0F, 0, 
                        LunarImageMst.LOADOUT_SELECTED_S - getSrcRenderHeight(LunarImageMst.LOADOUT_SELECTED_S), 
                        LunarImageMst.LOADOUT_SELECTED_S, getSrcRenderHeight(LunarImageMst.LOADOUT_SELECTED_S), 
                        false, false);
                sb.draw(LunarImageMst.LoadoutSelectedMark, hb.cX - LunarImageMst.LOADOUT_MARK_S / 2F, 
                        hb.cY - LunarImageMst.LOADOUT_SELECTED_S / 2F - scale(5F), LunarImageMst.LOADOUT_MARK_S / 2F,
                        LunarImageMst.LOADOUT_SELECTED_S / 2F - LunarImageMst.LOADOUT_MARK_S, LunarImageMst.LOADOUT_MARK_S, 
                        LunarImageMst.LOADOUT_MARK_S, scale, scale, 0F, 0, 
                        LunarImageMst.LOADOUT_MARK_S - getSrcRenderHeight(LunarImageMst.LOADOUT_MARK_S), 
                        LunarImageMst.LOADOUT_MARK_S, getSrcRenderHeight(LunarImageMst.LOADOUT_MARK_S), false, false);
            }
        }
    }
    
    protected Color getTipColor() {
        switch (valueInt) {
            case 0:
                return Color.OLIVE.cpy();
            case 1:
                return Color.BROWN.cpy();
            case 2:
                return Color.FIREBRICK.cpy();
            default:
                return Color.LIGHT_GRAY.cpy();
        }
    }
}