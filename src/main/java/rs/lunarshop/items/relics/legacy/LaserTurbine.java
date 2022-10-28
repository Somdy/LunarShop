package rs.lunarshop.items.relics.legacy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.utils.DamageInfoTag;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.vfx.combat.LunarSweepingBeamEffect;

public class LaserTurbine extends LegacyRelic {
    private static final int CHARGE_THRESHOLD = 100;
    private static final int ULT_CHARGE_THRESHOLD = 500;
    private static final float DAMAGE_RATE = 0.5F;
    private static final float RATE_PER_STACK = 0.25F;
    private static final float CHARGE_MULT = 1.5F;
    private float damageRate;
    private boolean usable;
    
    public LaserTurbine() {
        super(78);
        damageRate = DAMAGE_RATE;
        counter = 0;
        usable = false;
        presetInfo(s -> createInfo(s, SciPercent(damageRate)));
    }
    
    @Override
    public void refreshStats() {
        damageRate = DAMAGE_RATE + (stack - 1) * RATE_PER_STACK;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == cpr() && damageAmount > 0 && !DamageInfoTag.HasTag(info, DamageInfoTag.TURBINE_IGNORED)) {
            int charge = MathUtils.round(damageAmount * damageRate);
            if (charge > 0) {
                charge(charge);
            }
        }
    }
    
    @Override
    public void atBattleStart() {
        if (counter >= ULT_CHARGE_THRESHOLD) {
            doDamage(true);
        }
    }
    
    @Override
    protected boolean onRightClick() {
        if (usable && LunarUtils.RoomChecker(AbstractRoom.RoomPhase.COMBAT)) {
            doDamage(false);
        }
        return true;
    }
    
    private void doDamage(boolean ult) {
        int damage = MathUtils.ceil(counter * CHARGE_MULT);
        counter = 0;
        usable = false;
        stopPulse();
        addToBot(new SFXAction("ATTACK_DEFECT_BEAM"));
        addToBot(new VFXAction(new LunarSweepingBeamEffect(currentX, currentY, cpr().flipHorizontal, Color.FIREBRICK)));
        if (ult) {
            addToBot(new VFXAction(new LunarSweepingBeamEffect(currentX, currentY, !cpr().flipHorizontal, Color.FIREBRICK)));
            addToBot(damage(cpr(), damage, AbstractGameAction.AttackEffect.BLUNT_HEAVY, DamageInfoTag.TURBINE_IGNORED));
        }
        addToBot(damageAll(cpr(), damage, ult ? AbstractGameAction.AttackEffect.BLUNT_HEAVY
                : AbstractGameAction.AttackEffect.BLUNT_LIGHT, DamageInfoTag.TURBINE_IGNORED));
    }
    
    private void charge(int amount) {
        counter += amount;
        if (counter >= CHARGE_THRESHOLD) {
            usable = true;
            if (!pulse) {
                beginLongPulse();
            }
            if (counter >= ULT_CHARGE_THRESHOLD && LunarUtils.RoomChecker(AbstractRoom.RoomPhase.COMBAT)) {
                doDamage(true);
            }
        }
    }
}