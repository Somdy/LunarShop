package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.ShatteredPower;
import rs.lunarshop.powers.ShatteringPower;

public class Shattering extends LunarRelic {
    public static final int SHATTERED_NEED = 4;
    public static int ArmorReduction = 40;
    
    public Shattering() {
        super(23);
        presetInfo(s -> createInfo(s, ArmorReduction));
    }
    
    @Override
    public void refreshStats() {
        ArmorReduction = 40 + MathUtils.ceil(40 * 0.1F * (stack - 1));
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