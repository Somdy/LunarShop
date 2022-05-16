package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Scythe extends LunarRelic {
    private static final float CHANCE = 0.25F;
    private int healAmt;
    
    public Scythe() {
        super(ItemID.Scythe);
        healAmt = 4;
    }
    
    @Override
    public void refreshStats() {
        healAmt = 4 + 2 * (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], healAmt);
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.owner == cpr() && damageAmount > 0 && rollCloverLuck(CHANCE)) {
            for (int i = 0; i < healAmt / 2; i++) {
                addToBot(new VFXAction(new FlyingOrbEffect(target.hb.cX, target.hb.cY)));
            }
            addToBot(new HealAction(cpr(), cpr(), healAmt));
        }
    }
}