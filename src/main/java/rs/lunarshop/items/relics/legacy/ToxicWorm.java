package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.powers.ToxicPower;

public class ToxicWorm extends LegacyRelic {
    private int attackedAmt;
    private int attackAmt;
    
    public ToxicWorm() {
        super(90);
        attackAmt = 2;
        attackedAmt = 2;
    }
    
    @Override
    public void refreshStats() {
        attackAmt = stack + 1;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(new ApplyPowerAction(target, cpr(), new ToxicPower(target, cpr(), attackAmt)));
        }
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(new ApplyPowerAction(info.owner, cpr(), new ToxicPower(info.owner, cpr(), attackedAmt)));
        }
        return super.onAttacked(info, damageAmount);
    }
}