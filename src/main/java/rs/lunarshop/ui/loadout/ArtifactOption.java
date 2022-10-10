package rs.lunarshop.ui.loadout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import rs.lunarshop.localizations.ArtifactLocals;
import rs.lunarshop.localizations.LunarLocalLoader;
import rs.lunarshop.localizations.LunarTipLocals;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarTip;

public class ArtifactOption extends LoadoutOption {
    public static final LunarTipLocals tipBuilder = LunarLocalLoader.GetTipBuilder("ArtifactOption");
    private final LunarTip overall;
    protected final String ID;
    protected final ArtifactLocals localStrings;
    protected final String name;
    protected final String description;
    
    protected Texture shadowImage;
    protected boolean unlocked;
    
    public ArtifactOption(ArtifactTab tab, String ID, boolean unlocked) {
        super(tab, LunarImageMst.ArtifactOf(ID.toLowerCase()), 48, 48, 68, 68, Settings.scale);
        this.ID = ID;
        localStrings = LunarLocalLoader.GetArtifactLocal(ID);
        name = localStrings.NAME;
        description = localStrings.DESCRIPTION;
        this.valueStr = this.ID;
        this.shadowImage = LunarImageMst.ArtifactShadowOf(ID.toLowerCase());
        this.unlocked = unlocked;
        overall = tipBuilder.getTip(0);
        makeLunarTip(name, description, Color.PURPLE.cpy(), LunarTip.B_DEFAULT);
    }
    
    protected void unlock() {
        unlocked = true;
    }
    
    protected boolean isUnlocked() {
        return unlocked;
    }
    
    @Override
    public void update() {
        super.update();
        if (selected) {
            makeLunarTip(name, description, Color.PURPLE.cpy(), LunarTip.B_DEFAULT);
        } else {
            makeLunarTip(name + overall.header, description, Color.PURPLE.cpy(), LunarTip.B_DEFAULT);
        }
    }
    
    @Override
    protected void selfOnLeftClick() {
        selected = !selected;
        if (tab.selectedValues.contains(valueStr)) {
            log("[" + name + "] unselected");
            tab.selectedValues.remove(valueStr);
        } else {
            log("[" + name + "] selected");
            tab.selectedValues.add(valueStr);
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        if (isHidden) return;
        sb.setColor(Color.WHITE.cpy());
        Texture renderImage = selected ? image : shadowImage;
        sb.draw(renderImage, hb.cX - imageW / 2F, hb.cY - imageH / 2F, imageW / 2F, imageH / 2F, imageW,
                getRenderHeight(imageH), scale, scale, 0F, 0, imageH - getSrcRenderHeight(imageH), imageW,
                getSrcRenderHeight(imageH), false, false);
        if (hovered && tip.hasTip()) {
            renderTip();
        }
        hb.render(sb);
    }
}