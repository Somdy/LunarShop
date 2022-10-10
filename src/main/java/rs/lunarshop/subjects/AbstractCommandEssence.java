package rs.lunarshop.subjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.EssCallerType;
import rs.lunarshop.interfaces.EssenceCaller;
import rs.lunarshop.ui.cmdpicker.ItemContainer;
import rs.lunarshop.ui.cmdpicker.PickerCaller;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.vfx.misc.EssenceCoreEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommandEssence implements LunarUtils, PickerCaller {
    private EssenceCoreEffect effect;
    private final List<PowerTip> tips;
    protected final List<AbstractRelic> relics;
    protected Color core;
    protected Color glow;
    public Hitbox hb;
    protected UIStrings uiStrings;
    protected String[] TEXT;
    protected EssenceCaller caller;
    public float scale;
    public boolean hovered;
    public boolean clicked;
    public boolean used;
    
    public AbstractCommandEssence(String ID) {
        relics = new ArrayList<>();
        tips = new ArrayList<>();
        core = glow = Color.WHITE.cpy();
        hb = new Hitbox(64F, 64F);
        uiStrings = LunarMod.UIStrings(ID);
        TEXT = uiStrings.TEXT;
        scale = 1F;
        hovered = clicked = used = false;
    }
    
    public void init() {
        effect = new EssenceCoreEffect(core, glow, MathUtils.random(1F, 2F));
        effect.setScale(scale).setPause(false);
        effect.move(hb.cX, hb.cY);
    }
    
    public AbstractCommandEssence setCaller(EssenceCaller caller) {
        this.caller = caller;
        return this;
    }
    
    public AbstractCommandEssence spin(boolean spin) {
        effect.setPause(!spin);
        return this;
    }
    
    public void move(float cX, float cY) {
        hb.move(cX, cY);
        effect.move(hb.cX, hb.cY);
    }
    
    public abstract int getPrice();
    
    public boolean canSpawn(EssCallerType type) {
        return true;
    }
    
    public void update() {
        effect.update();
        hb.update();
        effect.move(hb.cX, hb.cY);
        hovered = hb.hovered;
        if (hb.justHovered) {
            tips.clear();
            updateTips(tips);
        }
        if (hb.hovered) {
            if (InputHelper.justClickedLeft) {
                hb.clickStarted = true;
            }
            if (hb.clicked) {
                hb.clicked = false;
                clicked = true;
            }
        }
    }
    
    protected void updateTips(List<PowerTip> tips) {}
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    public void onClick() {
        List<AbstractRelic> list = new ArrayList<>();
        relics.forEach(r -> list.add(r.makeCopy()));
        LunarMod.PreloadItemPicker(core, glow);
        LunarMod.OpenCommandPicker(this, list);
    }
    
    @Override
    public void onSelectingItem(ItemContainer ic) {
        ic.pickupRelic();
        used = true;
        if (caller != null)
            caller.onEssenceUsedUp(this);
    }
    
    @Override
    public void onCancelSelection() {
        used = false;
    }
    
    public void render(SpriteBatch sb) {
        effect.setScale(scale);
        effect.render(sb);
        if (hovered) {
            TipHelper.queuePowerTips(InputHelper.mX + scale(20F), InputHelper.mY, (ArrayList<PowerTip>) tips);
        }
    }
    
    public void dispose() {
        effect.dispose();
    }
}