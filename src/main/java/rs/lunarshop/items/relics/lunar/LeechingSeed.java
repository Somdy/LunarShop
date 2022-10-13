package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LunarRelic;

public class LeechingSeed extends LunarRelic {
    private int heals;
    
    public LeechingSeed() {
        super(19);
        heals = 1;
    }
    
    @Override
    public void refreshStats() {
        heals = stack;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.owner == cpr()) {
            addToBot(new HealAction(cpr(), cpr(), heals, 0.01F));
        }
        super.onAttack(info, damageAmount, target);
    }
}