package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.BleedingPower;

public class BleederDagger extends LunarRelic {
    private float chance;
    
    public BleederDagger() {
        super(ItemID.BleederDagger);
        chance = 0.1F;
    }
    
    @Override
    public void refreshStats() {
        chance = 0.1F * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(chance));
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && rollCloverLuck(chance)) {
            int bleeds = MathUtils.ceil(damageAmount / 10F);
            if (bleeds < 2) bleeds = 2;
            addToBot(new ApplyPowerAction(target, info.owner, new BleedingPower(target, bleeds)));
        }
    }
}