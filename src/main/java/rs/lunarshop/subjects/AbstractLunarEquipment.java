package rs.lunarshop.subjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.Nullable;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarClass;

public abstract class AbstractLunarEquipment extends AbstractLunarRelic {
    private AbstractLunarEquipment origin;
    private AbstractLunarEquipment proxy;
    private boolean isProxy;
    
    protected AbstractLunarEquipment(int lunarID, LunarClass clazz, int stack, int cooldown) {
        super(lunarID, clazz, stack);
        setEquipment(true);
        setCooldown(cooldown, true);
        isProxy = false;
    }
    
    @Override
    public AbstractLunarRelic setUseAutoActivate(boolean useAutoActivate) {
        if (proxy != null)
            proxy.setUseAutoActivate(useAutoActivate);
        return super.setUseAutoActivate(useAutoActivate);
    }
    
    /**
     * This method adds {@code proxy} to {@code EquipmentProxy} pre battle
     * Try not to override this
     */
    @Override
    public void atPreBattle() {
        super.atPreBattle();
        if (proxy == null) {
            proxy = makeProxy();
        }
        if (proxy != null) {
            LunarMod.EqmtProxy.attach(proxy);
        }
    }
    
    /**
     * This method removes {@code proxy} from {@code EquipmentProxy} after battle
     * Try not to forget to call super if overriding this
     */
    @Override
    public void onVictory() {
        super.onVictory();
        LunarMod.EqmtProxy.dissociate();
    }
    
    public final void updateOnRightClick() {
        if (isProxy) {
            log("Using [" + prop.localname + "]");
            onRightClick();
        }
    }
    
    @Override
    protected final void updateEquipmentTargetMode() {
        if (!isProxy) return;
        super.updateEquipmentTargetMode();
    }
    
    @Override
    public boolean canActivateEquipment() {
        return super.canActivateEquipment() && isProxy;
    }
    
    @Override
    protected void use(AbstractCreature s, AbstractCreature t) {
        super.use(s, t);
        if (origin != null)
            origin.use(s, t);
    }
    
    @Override
    protected void use() {
        super.use();
        if (origin != null) {
            origin.use();
            if (!isFunder) {
                for (AbstractRelic r : cpr().relics) {
                    if (r instanceof AbstractLunarRelic && !((AbstractLunarRelic) r).isEquipment()) {
                        ((AbstractLunarRelic) r).afterEqmtActivated(this);
                    }
                }
            }
        }
    }
    
    @Override
    public void autoUse() {
        super.autoUse();
        if (origin != null) {
            origin.autoUse();
        }
    }
    
    @Override
    protected void startCooldown() {
        super.startCooldown();
        if (origin != null)
            origin.startCooldown();
    }
    
    @Override
    protected void reduceCooldown(int reduce) {
        super.reduceCooldown(reduce);
        if (proxy != null)
            proxy.reduceCooldown(reduce);
    }
    
    @Override
    public void constructInfo() {
        super.constructInfo();
        if (proxy != null)
            proxy.constructInfo();
    }
    
    @Override
    protected void updateExtraTips() {
        super.updateExtraTips();
        if (proxy != null)
            proxy.updateExtraTips();
    }
    
    @Override
    protected void updateTip(String head, String body) {
        super.updateTip(head, body);
        if (proxy != null)
            proxy.updateTip(head, body);
    }
    
    @Override
    protected void updateTip(PowerTip tip) {
        super.updateTip(tip);
        if (proxy != null)
            proxy.updateTip(tip);
    }
    
    /**
     * equipments shall not override this method
     * @param p the dungeon player
     */
    @Override
    protected void obtainEquipment(AbstractPlayer p) {
        super.obtainEquipment(p);
        proxy = makeProxy();
        if (AbstractDungeon.getCurrMapNode() != null && currRoom() != null
                && currRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (!LunarMod.EqmtProxy.isEquipping(proxy)) {
                LunarMod.EqmtProxy.attach(proxy);
            }
        }
    }
    
    @Override
    public void renderWithoutAmount(SpriteBatch sb, Color c) {
        super.renderWithoutAmount(sb, c);
        if (isProxy() && cooldown > 0) {
            try {
                renderCooldown(sb, false);
            } catch (Exception e) {
                log("Failed to render cooldown: " + cooldown);
                e.printStackTrace();
            }
        }
    }
    
    // TODO: Fix SL mess
    @Override
    protected void loadThings(LunarConfig config) {
        super.loadThings(config);
        log("Retrieving proxy on SL");
        proxy = makeProxy();
        updateProxyData(proxy);
        proxy.updateExtraTips();
    }
    
    protected void updateProxyData(AbstractLunarEquipment proxy) {
        
    }
    
    @Nullable
    private  AbstractLunarEquipment makeProxy() {
        if (isProxy) {
            log("A proxy can't have others proxies");
            return null;
        }
        try {
            AbstractLunarEquipment proxy = this.getClass().newInstance();
            proxy.isProxy = true;
            proxy.origin = this;
            proxy.cooldown = this.cooldown;
            return proxy;
        } catch (Exception e) {
            log("Failed to make a proxy of " + name);
            throw new RuntimeException(name + " required a constructor without parameters");
        }
    }
    
    public AbstractLunarEquipment getOrigin() {
        return origin;
    }
    
    public AbstractLunarEquipment getProxy() {
        return proxy;
    }
    
    public boolean isProxy() {
        return isProxy;
    }
}