package rs.lunarshop.items.relics.planet;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.abstracts.PlanetRelic;

public class ChargedDrill extends PlanetRelic {
    private static final float CHANCE = 0.1F;
    private float multiplier;
    
    public ChargedDrill() {
        super(24);
        multiplier = 1.5F;
        presetInfo(s -> createInfo(s, SciPercent(multiplier)));
    }
    
    @Override
    public void refreshStats() {
        multiplier = 1.5F + 0.5F * (stack - 1);
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.owner == cpr() && target != cpr() && !target.isDeadOrEscaped() && rollCloverLuck(CHANCE)) {
            addToBot(new RelicAboveCreatureAction(target, this));
            addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
            addToBot(new VFXAction(new LightningEffect(target.hb.cX, target.hb.cY)));
            addToBot(new WaitAction(0.1F));
            int damage = MathUtils.ceil(damageAmount * multiplier);
            addToBot(new NullableSrcDamageAction(target, crtDmgInfo(null, damage, DamageInfo.DamageType.THORNS), 
                    AbstractGameAction.AttackEffect.NONE));
        }
        super.onAttack(info, damageAmount, target);
    }
}