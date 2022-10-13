package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import rs.lunarshop.localizations.LunarLocalLoader;
import rs.lunarshop.localizations.LunarTipLocals;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarTip;

public class EclipseOption extends LoadoutOption {
    public static final LunarTipLocals tipBuilder = LunarLocalLoader.GetTipBuilder("EclipseOption");
    public static final int IMAGE_S = 48;
    public static final int FRAME_S = 64;
    private static final int LOADOUT_SELECTED_S = LunarImageMst.LOADOUT_SELECTED_S;
    protected boolean forcedSelected;
    
    public EclipseOption(EclipseTab tab, int level) {
        super(tab, LunarImageMst.EclipseOf(level), IMAGE_S, IMAGE_S, 68F, 68F, Settings.scale);
        this.valueInt = level;
        LunarTip overall = tipBuilder.getTip(0, Color.SKY.cpy(), LunarTip.B_DEFAULT);
        makeLunarTip(overall.concat(tipBuilder.getTip(valueInt)));
    }
    
    @Override
    protected void selfOnLeftClick() {
        if (!selected) {
            selected = true;
            if (forcedSelected)
                forcedSelected = false;
            for (LoadoutOption opt : tab.options) {
                if (opt != this) {
                    opt.selected = false;
                    if (opt instanceof EclipseOption) {
                        ((EclipseOption) opt).forcedSelected = opt.valueInt < this.valueInt;
                    }
                }
            }
            tab.value = valueInt;
            LoadoutTab dTab = tab.manager.getTab(DifficultyTab.LOCALNAME);
            if (dTab instanceof DifficultyTab) {
                ((DifficultyTab) dTab).forceSelect(LoadoutManager.DIFFICULTIES.MONSOON);
            }
        } else if (!forcedSelected) {
            selected = false;
            for (LoadoutOption opt : tab.options) {
                if (opt != this) {
                    if (opt instanceof EclipseOption && opt.valueInt < this.valueInt) {
                        ((EclipseOption) opt).forcedSelected = false;
                    }
                }
            }
            tab.value = 0;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (!isHidden) {
            sb.setColor(Color.WHITE.cpy());
            if (!selected && !forcedSelected && hovered) {
                sb.draw(LunarImageMst.EclipseHoveredFrame, hb.cX - FRAME_S / 2F, hb.cY - FRAME_S / 2F, FRAME_S / 2F, 
                        FRAME_S / 2F, FRAME_S, getRenderHeight(FRAME_S), scale, scale, 0F, 0, 
                        FRAME_S - getSrcRenderHeight(FRAME_S), FRAME_S, getSrcRenderHeight(FRAME_S), false, false);
            }
            if (selected || forcedSelected) {
                sb.setColor(forcedSelected ? Color.DARK_GRAY.cpy() : Color.YELLOW.cpy());
                sb.draw(LunarImageMst.LoadoutSelectedOutline, hb.cX - LOADOUT_SELECTED_S / 2F,
                        hb.cY - LOADOUT_SELECTED_S / 2F, LOADOUT_SELECTED_S / 2F,
                        LOADOUT_SELECTED_S / 2F, LOADOUT_SELECTED_S,
                        getRenderHeight(LOADOUT_SELECTED_S), scale * 0.95F, scale * 0.95F, 0F, 0,
                        LOADOUT_SELECTED_S - getSrcRenderHeight(LOADOUT_SELECTED_S),
                        LOADOUT_SELECTED_S, getSrcRenderHeight(LOADOUT_SELECTED_S),
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
}