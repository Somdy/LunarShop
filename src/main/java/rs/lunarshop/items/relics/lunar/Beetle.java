package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.data.AchvID;
import rs.lunarshop.data.ItemID;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.BarricadePower;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.AchvHelper;

public final class Beetle extends LunarRelic {
    private float hpBuff;
    private int armor;
    private boolean damagedThisTurn;
    
    public Beetle() {
        super(ItemID.Beetle);
        hpBuff = 0.5F;
        armor = 0;
    }
    
    @Override
    public void refreshStats() {
        hpBuff = 0.5F + 0.2F * (stack - 1);
        int tHp = MathUtils.ceil(cpr().maxHealth * (1 + hpBuff));
        armor = tHp - 1;
        if (stack >= 3) {
            AchvHelper.UnlockAchv(AchvID.ManyBeetles);
        }
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], armor, MathUtils.ceil(armor * 0.5F));
    }
    
    @Override
    protected void onStackAmt(int amt, boolean stacking) {
//        checkPlayerMaxHp();
    }
    
    @Override
    public void atPreBattle() {
        super.atPreBattle();
        counter = cpr().maxHealth - 1;
        int tHp = MathUtils.ceil(cpr().maxHealth * (1 + hpBuff));
        cpr().increaseMaxHp(tHp - cpr().maxHealth, false);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        flash();
        armor = cpr().maxHealth - 1;
        cpr().decreaseMaxHealth(armor);
        addToTop(new GainBlockAction(cpr(), cpr(), armor));
        addToTop(new ApplyPowerAction(cpr(), cpr(), new BarricadePower(cpr())));
        damagedThisTurn = false;
    }
    
    @Override
    public void onVictory() {
        super.onVictory();
        if (counter > 0) {
            flash();
            cpr().increaseMaxHp(counter, true);
            counter = -1;
        }
    }
    
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.output > damageAmount && info.output > 0 && !damagedThisTurn) {
            damagedThisTurn = true;
            log("player got damaged this turn");
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
    
    @Override
    public void onPlayerEndTurn() {
        if (!damagedThisTurn && cpr().currentBlock < armor) {
            int restore = MathUtils.ceil(armor * 0.5F);
            if (cpr().currentBlock < restore)
                addToTop(new GainBlockAction(cpr(), cpr(), restore, true));
            else
                addToTop(new GainBlockAction(cpr(), cpr(), (armor - cpr().currentBlock), true));
        }
        if (damagedThisTurn) damagedThisTurn = false;
    }
    
    private void checkPlayerMaxHp() {
        int origin = counter;
        int tHp = MathUtils.ceil(origin * (1 + hpBuff));
        int diff = tHp - cpr().maxHealth;
        if (diff != 0) {
            log("Diff detected: " + diff);
        }
        if (diff > 0) {
            cpr().increaseMaxHp(diff, true);
        } else if (diff < 0) {
            cpr().decreaseMaxHealth(-diff);
        }
    }
}