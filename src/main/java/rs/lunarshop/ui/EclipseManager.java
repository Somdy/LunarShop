package rs.lunarshop.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

public class EclipseManager implements LunarUtils {
    public static final int ICON_W = 128;
    public static final int ICON_H = 128;
    
    private Hitbox hb;
    private Hitbox prev;
    private Hitbox next;
    private Texture icon;
    private int level;
    
    public EclipseManager() {
        hb = new Hitbox(scale(ICON_W), scale(ICON_H));
        prev = new Hitbox(scale(70F), scale(70F));
        next = new Hitbox(scale(70F), scale(70));
    }
    
    public void init() {
        level = 1;
        icon = getIcon();
    }
    
    Texture getIcon() {
        return LunarImageMst.EclipseOf(level);
    }
    
    void changeLevel(int dir) {
        level += dir > 0 ? 1 : -1;
        icon = getIcon();
    }
    
    public void update() {
        
    }
    
    public void render(SpriteBatch sb) {
        
    }
}