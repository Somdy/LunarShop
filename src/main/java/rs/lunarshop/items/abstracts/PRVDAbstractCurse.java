package rs.lunarshop.items.abstracts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.utils.GIFPlayer;
import rs.lunarshop.utils.PatternHelper;

public class PRVDAbstractCurse extends SpecialRelic {
    private final GIFPlayer animation;
    private int curseIndex;
    
    public PRVDAbstractCurse(int lunarID, int curseIndex) {
        super(lunarID);
        setStackable(false);
        isProvidenceCurse = true;
        presetInfo(s -> s[0] = null);
        this.curseIndex = curseIndex;
        animation = new GIFPlayer("LunarAssets/imgs/items/specials/providence_sheet.png",
                4, 2, 1, true).setFrameDuration(0.06F);
        animation.create();
        initializeCurse();
    }
    
    public void initializeCurse() {
        if (curseIndex <= 0 || curseIndex >= DESCRIPTIONS.length) {
            log("Curse out of index: " + curseIndex);
            return;
        }
        String curse = DESCRIPTIONS[curseIndex];
        curse = PatternHelper.PRVD_CURSE_PREFIX.matcher(curse).replaceAll("");
        description = curse;
        tips.clear();
        addTip(name, description);
        initializeTips();
    }
    
    public int getCurseIndex() {
        return curseIndex;
    }
    
    @Override
    protected void saveThings(RelicConfigBuilder builder) {
        builder.map("curseIndex", curseIndex);
    }
    
    @Override
    protected void loadThings(LunarConfig config) {
        if (config.hasMapKey("curseIndex")) {
            curseIndex = Integer.parseInt(config.getMapValue("curseIndex"));
            log("Loading saved curse [" + curseIndex + "]");
            initializeCurse();
        }
    }
    
    @Override
    public void update() {
        super.update();
        animation.update();
    }
    
    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        TextureRegion frame = animation.getKeyFrameTexture();
        sb.setColor(Color.WHITE.cpy());
        sb.draw(frame, currentX - scale(40F), currentY - scale(40F), scale(40F), scale(40F),
                66F, 66F, scale, scale, 0F);
        super.renderCounter(sb, inTopPanel);
    }
}