package rs.lunarshop.items.relics.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.items.abstracts.SpecialRelic;
import rs.lunarshop.utils.GIFPlayer;

public class PRVDCurse extends SpecialRelic {
    private final GIFPlayer animation;
    
    public PRVDCurse() {
        super(102);
        setStackable(false);
        animation = new GIFPlayer("LunarAssets/imgs/items/specials/providence_sheet.png", 
                4, 2, 1, true).setFrameDuration(0.06F);
        animation.create();
    }
    
    @Override
    public void update() {
        super.update();
        animation.update();
    }
    
    @Override
    protected void renderCustomValues(SpriteBatch sb, boolean inTopPanel) {
        TextureRegion frame = animation.getKeyFrameTexture();
        sb.setColor(Color.WHITE.cpy());
        sb.draw(frame, currentX - scale(40F), currentY - scale(40F), scale(40F), scale(40F),
                66F, 66F, scale, scale, 0F);
    }
}