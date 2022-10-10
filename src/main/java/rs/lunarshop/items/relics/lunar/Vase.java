package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.AchvHelper;

public class Vase extends LunarRelic {
    private float percent;
    private int useLeft;
    
    public Vase() {
        super(21);
        percent = 0.5F;
        useLeft = 2;
    }
    
    @Override
    public void refreshStats() {
        percent = 0.5F + 0.25F * ((stack - 1) / 2F);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(percent), useLeft);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        useLeft = 2;
        grayscale = false;
        usedUp = false;
    }
    
    @Override
    protected void use() {
        if (useLeft > 0 && counter > 0) {
            int block = MathUtils.round(counter / 2F);
            if (block > 0)
                addToBot(new GainBlockAction(cpr(), cpr(), block));
            useLeft--;
            updateExtraTips();
            if (useLeft == 0) {
                grayscale = true;
                usedUp = true;
            }
            if (counter > 1) counter /= 2;
            else counter = 0;
        }
    }
    
    @Override
    public void atTurnStart() {
        if (cpr().lastDamageTaken > 0) {
            int store = MathUtils.round(cpr().lastDamageTaken * percent);
            if (store > 0) {
                for (int i = 0; i < store; i++) {
                    counter++;
                }
            }
        }
        if (counter >= 200) {
            AchvHelper.UnlockAchv(AchvID.StackingVase);
        }
    }
}