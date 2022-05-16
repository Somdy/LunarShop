package rs.lunarshop.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

public class LunarButton implements LunarUtils {
    public static final int BTN_W = 128;
    public static final int BTN_H = 64;
    
    private Hitbox hb;
    private String label;
    private final Texture btn;
    private final Texture highlight;
    public Color btnColor;
    public Color textColor;
    private boolean disabled;
    private boolean hidden;
    public boolean hovered;
    public boolean leftClicked;
    
    public LunarButton(String label) {
        this.label = label;
        disabled = false;
        hidden = true;
        hb = new Hitbox(scale(BTN_W), scale(BTN_H));
        btn = LunarImageMst.BtnNaked;
        highlight = LunarImageMst.BtnHighlighted;
        btnColor = Color.WHITE.cpy();
        textColor = Color.WHITE.cpy();
    }
    
    public LunarButton() {
        this(null);
    }
    
    public LunarButton setLabel(String label) {
        this.label = label;
        return this;
    }
    
    public void translate(float x, float y) {
        hb.translate(x, y);
    }
    
    public void move(float cX, float cY) {
        hb.move(cX, cY);
    }
    
    public void disable() {
        if (!disabled) {
            disabled = true;
            hb.hovered = false;
            hovered = false;
            leftClicked = false;
            btnColor = Color.DARK_GRAY.cpy();
            textColor = Color.LIGHT_GRAY.cpy();
        }
    }
    
    public void enable() {
        if (disabled) {
            disabled = false;
            btnColor = Color.WHITE.cpy();
            textColor = Color.WHITE.cpy();
        }
    }
    
    public void show() {
        hidden = false;
    }
    
    public void hide() {
        hidden = true;
    }
    
    public void update() {
        if (hidden) return;
        if (!disabled) {
            hb.update();
            if (hb.justHovered) {
                playSound("UI_HOVER");
            }
            hovered = hb.hovered;
            if (hb.hovered) {
                if (InputHelper.justClickedLeft) {
                    hb.clickStarted = true;
                    playSound("UI_CLICK_1");
                }
                if (hb.clicked) {
                    hb.clicked = false;
                    leftClicked = true;
                }
            }
        }
    }
    
    public void render(SpriteBatch sb) {
        if (hidden) return;
        hb.render(sb);
        sb.setColor(btnColor);
        sb.draw(btn, hb.cX - BTN_W / 2F, hb.cY - BTN_H / 2F, BTN_W / 2F, BTN_H / 2F, BTN_W, BTN_H, 
                Settings.scale, Settings.scale, 0F, 0, 0, BTN_W, BTN_H, false, false);
        sb.setColor(Color.WHITE.cpy());
        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, label, hb.cX, hb.cY, textColor);
        if (!disabled && hovered) {
            sb.draw(highlight, hb.cX - BTN_W / 2F, hb.cY - BTN_H / 2F, BTN_W / 2F, BTN_H / 2F, BTN_W, BTN_H, 
                    Settings.scale, Settings.scale, 0F, 0, 0, BTN_W, BTN_H, false, false);
        }
    }
}