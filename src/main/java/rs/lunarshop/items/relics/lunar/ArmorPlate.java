package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.items.abstracts.LunarRelic;

public class ArmorPlate extends LunarRelic {
    private int reduction;
    private boolean negated;
    
    public ArmorPlate() {
        super(37);
        setTurnUse(2);
        reduction = 3;
        negated = false;
    }
    
    @Override
    public void refreshStats() {
        reduction = 1 + 2 * stack;
    }
    
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (canActivate() && damageAmount > 1) {
            damageAmount -= reduction;
            if (damageAmount <= 0) damageAmount = 1;
            flash();
            negated = true;
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (negated) {
            negated = false;
            deplete();
        }
    }
}