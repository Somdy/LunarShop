package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.BleedingPower;
import rs.lunarshop.utils.PotencyHelper;

public class Spleen extends LunarRelic {
    private float damageThreshold;
    private static float bleedPercent;
    
    public Spleen() {
        super(ItemID.Spleen);
        damageThreshold = 1.5F;
        bleedPercent = 0.05F;
    }
    
    @Override
    public void refreshStats() {
        bleedPercent = 0.05F + 0.0125F * (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(bleedPercent));
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == cpr() && isCrit(damageAmount, info)) {
            addToBot(new RelicAboveCreatureAction(target, this));
            int bleeds = MathUtils.ceil(damageAmount / 10F);
            if (bleeds < 2) bleeds = 2;
            addToBot(new ApplyPowerAction(target, info.owner, new BleedingPower(target, bleeds)));
        }
    }
    
    @Override
    public boolean canSpawnForShopping(int shopType) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV5) && super.canSpawnForShopping(shopType);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV5);
    }
    
    public static boolean On() {
        return LMSK.Player().hasRelic(ItemID.Spleen.internalID);
    }
    
    public static float BleedBuff() {
        return bleedPercent;
    }
}