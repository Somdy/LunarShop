package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.PredatoryPower;

public class PredatoryMask extends LunarRelic {
    public static int MaxStr = 6;
    
    public PredatoryMask() {
        super(61);
    }
    
    @Override
    public void refreshStats() {
        MaxStr = 4 * stack + 2;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == cpr() && isCrit(damageAmount, info)) {
            addToBot(new RelicAboveCreatureAction(target, this));
            addToBot(new ApplyPowerAction(cpr(), cpr(), new PredatoryPower(cpr(), 2, 2)));
        }
    }
}