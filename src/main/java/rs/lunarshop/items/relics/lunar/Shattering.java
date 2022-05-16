package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.ShatteredPower;
import rs.lunarshop.powers.ShatteringPower;

public class Shattering extends LunarRelic {
    public static final int SHATTERED_NEED = 4;
    public static int ArmorReduction = 30;
    
    public Shattering() {
        super(ItemID.Shattering);
    }
    
    @Override
    public void refreshStats() {
        ArmorReduction = 30 + MathUtils.ceil(30 * 0.1F * (stack - 1));
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], ArmorReduction);
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (canApplyShattering(target)) {
            addToTop(new ApplyPowerAction(target, info.owner, new ShatteringPower(target)));
        }
        super.onAttack(info, damageAmount, target);
    }
    
    private boolean canApplyShattering(AbstractCreature target) {
        return target.powers.stream().noneMatch(p -> p instanceof ShatteredPower);
    }
}