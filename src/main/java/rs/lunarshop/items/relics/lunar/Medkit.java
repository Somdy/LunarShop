package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.MedkitPower;
import rs.lunarshop.utils.NpcHelper;
import rs.lunarshop.utils.PotencyHelper;

public class Medkit extends LunarRelic {
    private int heals;
    private float extraH;
    
    public Medkit() {
        super(39);
        heals = 2;
        extraH = 0.05F;
    }
    
    @Override
    public void refreshStats() {
        heals = MathUtils.floor(2 + (0.05F * stack * (cpr() != null ? cpr().maxHealth : 1)));
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], heals);
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && !MedkitPower.OnHeal && NpcHelper.InCombat()) {
            flash();
            addToBot(new ApplyPowerAction(cpr(), cpr(), new MedkitPower(cpr(), heals, 2)));
            MedkitPower.OnHeal = true;
        }
        return super.onAttacked(info, damageAmount);
    }
    
    @Override
    public boolean canSpawnForShopping(int shopType) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV2) && super.canSpawnForShopping(shopType);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV2);
    }
}