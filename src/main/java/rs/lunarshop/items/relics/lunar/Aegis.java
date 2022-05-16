package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.PotencyHelper;

public class Aegis extends LunarRelic {
    private float percent;
    
    public Aegis() {
        super(ItemID.Aegis);
        percent = 0.5F;
    }
    
    @Override
    public void refreshStats() {
        percent = 0.5F + 0.25F * (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(percent));
    }
    
    @Override
    public int preModifyHeal(AbstractCreature who, int healAmt) {
        if (who instanceof AbstractPlayer) {
            int overflow = MathUtils.ceil((who.currentHealth + healAmt - who.maxHealth) * percent);
            if (overflow > 0) {
                addToBot(new GainBlockAction(who, who, overflow));
            }
        }
        return super.preModifyHeal(who, healAmt);
    }
    
    @Override
    public boolean canSpawnForShopping(int shopType) {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV3) && super.canSpawnForShopping(shopType);
    }
    
    @Override
    public boolean canSpawnForReward() {
        return PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV3);
    }
}
