package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.powers.PermafrostPower;

public class Permafrost extends LegacyRelic {
    private static final int LASTING_TURNS = 3;
    private int reduction;
    
    public Permafrost() {
        super(79);
        reduction = 1;
    }
    
    @Override
    public void refreshStats() {
        reduction = stack;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(new ApplyPowerAction(target, cpr(), new PermafrostPower(target, LASTING_TURNS, reduction)));
        }
    }
}