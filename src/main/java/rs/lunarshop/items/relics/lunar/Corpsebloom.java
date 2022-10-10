package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Corpsebloom extends LunarRelic {
    private float healBuff;
    private float maxHeal;
    private int storeHeal;
    private int turnHeal;
    private boolean triggerEffect;
    
    public Corpsebloom() {
        super(2);
        healBuff = 1F;
        maxHeal = 0.25F;
        storeHeal = 0;
        turnHeal = 0;
        triggerEffect = true;
    }
    
    @Override
    public void refreshStats() {
        healBuff = 1F + 0.5F * (stack - 1);
        maxHeal = 0.25F * 1 / stack;
    }
    
    @Override
    public void constructInfo() {
        if (storeHeal > 0) {
            createStatsInfo(DESCRIPTIONS[2], SciPercent(healBuff), MathUtils.ceil(maxHeal * cpr().maxHealth), storeHeal);
        } else {
            createStatsInfo(DESCRIPTIONS[1], SciPercent(healBuff), MathUtils.ceil(maxHeal * cpr().maxHealth));
        }
    }
    
    @Override
    public int onPlayerHeal(int healAmount) {
        if (triggerEffect) {
            healAmount += healAmount * healBuff;
            int maxHealAmt = MathUtils.ceil(maxHeal * cpr().maxHealth);
            if (healAmount > maxHealAmt) {
                int diff = healAmount - maxHealAmt;
                healAmount = maxHealAmt;
                storeHealAmt(diff);
            }
        }
        return super.onPlayerHeal(healAmount);
    }
    
    private void storeHealAmt(int healAmt) {
        flash();
        storeHeal = healAmt;
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (storeHealLeft()) {
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new QuickAction(() -> {
                triggerEffect = false;
                cpr().heal(turnHeal, true);
                triggerEffect = true;
            }));
            storeHeal -= turnHeal;
        }
    }
    
    private boolean storeHealLeft() {
        if (storeHeal > 0) {
            int maxHealAmt = MathUtils.ceil(maxHeal * cpr().maxHealth);
            turnHeal = Math.min(storeHeal, maxHealAmt);
            updateExtraTips();
            return true;
        }
        storeHeal = 0;
        updateExtraTips();
        return false;
    }
    
    @Override
    protected void renderCustomValues(SpriteBatch sb, boolean inTopPanel) {
        if (storeHealLeft()) {
            renderText(sb, inTopPanel, String.valueOf(storeHeal), -15F, 0F, Color.GREEN.cpy());
        }
    }
}