package rs.lunarshop.items.relics.legacy;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.powers.ToxicPower;

public class DeadManFoot extends LegacyRelic {
    private static final float HP_THRESHOLD = 0.25F;
    private static final int BASE_TOXIC = 5;
    private int times;
    
    public DeadManFoot() {
        super(83);
        times = stack;
    }
    
    @Override
    public void refreshStats() {
        times = stack;
    }
    
    @Override
    public void atTurnStart() {
        if (cpr().currentHealth < MathUtils.round(cpr().maxHealth * HP_THRESHOLD)) {
            flash();
            for (int i = 0; i < times; i++) {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                addToBot(new ApplyPowerAction(m, cpr(), new ToxicPower(m, cpr(), BASE_TOXIC)));
            }
        }
    }
}