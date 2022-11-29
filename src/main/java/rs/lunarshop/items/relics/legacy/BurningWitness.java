package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.powers.OnFirePower;
import rs.lunarshop.utils.mechanics.AttackHelper;

public class BurningWitness extends LegacyRelic {
    private static final int BASE_BLOCK = 2;
    private static final int DMG_PER_STACK = 1;
    private int fireDamage;
    
    public BurningWitness() {
        super(72);
        fireDamage = 0;
    }
    
    @Override
    public void refreshStats() {
        fireDamage = DMG_PER_STACK * (stack - 1);
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner != null && target != info.owner && damageAmount > 0) {
            int totalDamage = fireDamage + AttackHelper.GetAttack(info.owner);
            addToBot(new GainBlockAction(cpr(), cpr(), BASE_BLOCK));
            addToBot(new ApplyPowerAction(target, info.owner, new OnFirePower(target, totalDamage, 1, 
                    false)));
        }
    }
}