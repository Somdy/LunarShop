package rs.lunarshop.items.relics.legacy;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.vfx.combat.FlyingColoredOrbEffect;

public class MeatNugget extends LegacyRelic {
    private static final float HEAL_CHANCE = 0.5F;
    private static final int BASE_HEAL = 8;
    private static final int HEAL_STACK = 4;
    private int healAmt;
    
    public MeatNugget() {
        super(82);
        healAmt = BASE_HEAL;
    }
    
    @Override
    public void refreshStats() {
        healAmt = BASE_HEAL + (stack - 1) * HEAL_STACK;
    }
    
    @Override
    public void onProbablyKillMonster(DamageInfo info, int damageAmt, AbstractMonster m) {
        if (isDead(m) && rollCloverLuck(HEAL_CHANCE)) {
            addToBot(new VFXAction(new FlyingColoredOrbEffect(m.hb.cX, m.hb.cY, Color.LIME)));
            addToBot(new HealAction(cpr(), cpr(), healAmt));
        }
    }
    
    private boolean itIsDead(AbstractCreature who) {
        if (!(who instanceof AwakenedOne) && !(who instanceof Darkling)) {
            return (who.currentHealth <= 0 || who.isDying) && !who.halfDead;
        }
        return false;
    }
}