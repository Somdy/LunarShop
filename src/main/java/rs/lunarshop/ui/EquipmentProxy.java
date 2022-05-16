package rs.lunarshop.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarEquipment;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;

import static java.lang.Math.sin;

public final class EquipmentProxy implements LunarUtils {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(LunarMod.Prefix("EquipmentProxy"));
    public static final String[] TEXT = uiStrings.TEXT;
    private boolean justClickedRight;
    private boolean clickedRight;
    private final EProxy proxy;
    private final Hitbox frame;
    private float scale;
    private float angel;
    
    public EquipmentProxy() {
        justClickedRight = false;
        clickedRight = false;
        proxy = new EProxy();
        frame = new Hitbox(100F, 100F);
        scale = 0.95F;
        angel = 0F;
    }
    
    public void attach(AbstractLunarEquipment equipment) {
        if (proxy.equipment != null)
            log("Replacing equipment proxy " + proxy.equipment.name + " by " + equipment.name);
        proxy.attach(equipment);
        if (cprHasLunarRelic(ItemID.DrownedGesture))
            equipment.setUseAutoActivate(true);
    }
    
    public void dissociate() {
        if (proxy.equipment != null) {
            log("Dropping equipment proxy: " + proxy.equipment.props.lunarID);
            LMSK.Player().relics.remove(proxy.equipment);
            proxy.dissociate();
        }
    }
    
    public void update() {
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT)
            return;
        locateFrame();
        updateBreath();
        updateFrame();
        proxy.update(frame.cX, frame.cY);
    }
    
    private void updateFrame() {
        frame.update();
        if (frame.hovered && !AbstractDungeon.isScreenUp) {
            if (InputHelper.justClickedRight && proxy.equipment != null) {
                justClickedRight = true;
            }
            if (justClickedRight && InputHelper.justReleasedClickRight) {
                clickedRight = true;
                justClickedRight = false;
            }
            if (clickedRight && proxy.equipment != null) {
                clickedRight = false;
                proxy.equipment.updateOnRightClick();
            }
        }
    }
    
    private void updateBreath() {
        angel += 0.01F;
        scale = (float) (0.85F + 0.02F * sin(angel));
    }
    
    private void locateFrame() {
        if (LMSK.Player() != null && AbstractDungeon.getCurrRoom() != null) {
            AbstractPlayer p = LMSK.Player();
            frame.move(p.drawX + p.hb.width / 1.25F, p.drawY + p.hb.height / 1.5F);
        }
    }
    
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT)
            return;
        frame.render(sb);
        if (LunarMod.OmniPanel.showDetails()) {
            renderFrame(sb);
            renderTips(sb);
        }
        proxy.render(sb);
    }
    
    private void renderTips(SpriteBatch sb) {
        if (frame.hovered) {
            PowerTip tip = new PowerTip();
            tip.header = proxy.equipment == null ? TEXT[0] : TEXT[1];
            tip.body = proxy.equipment == null ? TEXT[2] : (TEXT[3] + proxy.equipment.name + TEXT[4]);
            ArrayList<PowerTip> tips = new ArrayList<>();
            tips.add(tip);
            if (proxy.equipment != null) {
                tips.addAll(proxy.equipment.tips);
            }
            TipHelper.queuePowerTips(InputHelper.mX + scale(25F), InputHelper.mY - scale(10F), tips);
        }
    }
    
    private void renderFrame(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(LunarImageMst.EquipSlots[0], frame.x, frame.y, 50F, 50F, 100F, 100F, 
                scale, scale, 0, 0, 0, 100, 100, false, false);
    }
    
    public boolean hasEquipment() {
        return proxy.equipment != null;
    }
    
    public boolean isEquipping(int lunarID) {
        if (proxy.equipment == null)
            return false;
        return proxy.equipment.props.lunarID == lunarID;
    }
    
    public boolean isEquipping(AbstractLunarEquipment equipment) {
        if (proxy.equipment == null)
            return false;
        return proxy.equipment == equipment;
    }
    
    public AbstractLunarEquipment getEquipment() {
        return proxy.equipment;
    }
    
    private static class EProxy {
        private AbstractLunarEquipment equipment;
        
        private EProxy attach(AbstractLunarEquipment equipment) {
            this.equipment = equipment;
            return this;
        }
        
        private EProxy dissociate() {
            this.equipment = null;
            return this;
        }
        
        private void update(float cx, float cy) {
            if (equipment != null) {
                equipment.update();
                if (equipment.isUseAutoActivate() && equipment.canActivateEquipment())
                    equipment.autoActivate();
                equipment.targetX = equipment.currentX = cx;
                equipment.targetY = equipment.currentY = cy;
            }
        }
        
        private void render(SpriteBatch sb) {
            if (equipment != null) {
                equipment.renderWithoutAmount(sb, Color.WHITE.cpy());
            }
        }
    }
}