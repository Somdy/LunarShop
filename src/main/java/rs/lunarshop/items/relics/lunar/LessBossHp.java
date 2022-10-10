package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.core.LunarTime;
import rs.lunarshop.items.abstracts.LunarRelic;

public class LessBossHp extends LunarRelic {
    private float hpLoss;
    private float waitTimer;
    private int timeLimits;
    private int timeLeft;
    private int timeCounter;
    private boolean inBossBattle;
    
    public LessBossHp() {
        super(3);
        hpLoss = 0.25F;
        timeLimits = 180;
        timeLeft = 180;
        timeCounter = 180;
        waitTimer = 0F;
        inBossBattle = false;
    }
    
    @Override
    public void refreshStats() {
        hpLoss = 0.25F + 0.25F * ((stack - 1) / 2F);
        timeLimits = 180 - MathUtils.ceil(60F * (stack - 1) / stack);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(hpLoss), timeLimits);
        if (timeLeft <= 0 && inBossBattle) {
            updateTip(new PowerTip(DESCRIPTIONS[2], DESCRIPTIONS[3]));
        }
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (timeLeft <= 0 && inBossBattle) {
            if (card.energyOnUse > 0) {
                addToBot(new RelicAboveCreatureAction(cpr(), this));
                addToBot(new QuickAction(() -> cpr().decreaseMaxHealth(MathUtils.floor(card.energyOnUse * 1.75F))));
            } else {
                addToBot(new LoseHPAction(cpr(), cpr(), currAct()));
            }
        }
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (isBossBattle()) {
            addToBot(new RelicAboveCreatureAction(cpr(), this));
            addToBot(new QuickAction(() -> {
                for (AbstractMonster m : getAllLivingMstrs()) {
                    if (m.type == AbstractMonster.EnemyType.BOSS) {
                        addToTop(new RelicAboveCreatureAction(m, this));
                        m.currentHealth = m.maxHealth = MathUtils.floor(m.maxHealth * (1 - hpLoss));
                        m.healthBarUpdatedEvent();
                    }
                }
            }));
            startCountdown();
        }
    }
    
    private void startCountdown() {
        timeLeft = timeLimits;
        timeCounter = timeLimits;
        waitTimer = 1F;
        inBossBattle = true;
    }
    
    private boolean isBossBattle() {
        return hasAnyExptMstr(c -> c instanceof AbstractMonster && ((AbstractMonster) c).type == AbstractMonster.EnemyType.BOSS);
    }
    
    @Override
    public void onVictory() {
        super.onVictory();
        resetCountdown();
    }
    
    private void resetCountdown() {
        inBossBattle = false;
        stopPulse();
        refreshStats();
    }
    
    @Override
    protected void renderCustomValues(SpriteBatch sb, boolean inTopPanel) {
        if (inBossBattle) {
            renderText(sb, inTopPanel, timeLeft > 0 ? String.valueOf(timeLeft) : "!!",
                    -10F, 0F, scale * 1.25F, Color.RED.cpy());
        }
    }
    
    @Override
    public void update() {
        super.update();
        calcTimeLeft();
    }
    
    private void calcTimeLeft() {
        if (inBossBattle && !isStopped()) {
            if (timeLeft > 0 ) {
                if (waitTimer > 0F) {
                    waitTimer -= Gdx.graphics.getDeltaTime();
                } else if (timeCounter != (int) LunarTime.DungeonTime()) {
                    timeCounter = (int) LunarTime.DungeonTime();
                    reduceTimeLeft();
                }
            }
        }
    }
    
    private void reduceTimeLeft() {
        timeLeft--;
        if (timeLeft <= 30)
            beginPulse();
        if (timeLeft == 0) {
            stopPulse();
            beginLongPulse();
            updateExtraTips();
        }
    }
    
    private boolean isStopped() {
        return CardCrawlGame.stopClock;
    }
}