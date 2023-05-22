package rs.lunarshop.items.equipments;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.utils.InfoTagHelper;
import rs.lunarshop.utils.mechanics.AttackHelper;

public class Capacitor extends LunarEquipment {
    private static final float DAMAGE_MULT = 10F;
    private static final int STR_LOSS = 5;
    
    public Capacitor() {
        super(48, 18);
        setTargetRequired(true);
    }
    
    @Override
    protected void use(AbstractCreature s, AbstractCreature t) {
        super.use(s, t);
        if (isProxy()) {
            int attack = AttackHelper.GetAttack(s);
            addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
            addToBot(new VFXAction(new BorderFlashEffect(Color.ROYAL.cpy())));
            addToBot(new VFXAction(new LightningEffect(t.hb.cX, t.hb.cY)));
            addToBot(damage(t, s, MathUtils.round(attack * DAMAGE_MULT), DamageInfo.DamageType.THORNS, 
                    AbstractGameAction.AttackEffect.NONE, InfoTagHelper.ENERGETIC));
            addToBot(new ApplyPowerAction(t, s, new StrengthPower(t, -STR_LOSS)));
            startCooldown();
        }
        updateExtraTips();
    }
    
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard != null)
            reduceCooldown();
    }
}
