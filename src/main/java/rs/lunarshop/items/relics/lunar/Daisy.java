package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Daisy extends LunarRelic {
    private float percent;
    private int turn;
    
    public Daisy() {
        super(27);
        setBattleUse();
        percent = 0.2F;
        turn = 1;
    }
    
    @Override
    public void refreshStats() {
        percent = 0.2F + 0.05F * (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        int healAmt = MathUtils.ceil(cpr().maxHealth * percent);
        createStatsInfo(DESCRIPTIONS[1], healAmt);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        if (currRoom() instanceof MonsterRoomBoss) {
            turn = 1;
            beginLongPulse();
        }
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (turn >= 5 && currRoom() instanceof MonsterRoomBoss && canActivate()) {
            flash();
            addToBot(new VFXAction(new SanctityEffect(hb.cX, hb.cY)));
            int healAmt = MathUtils.ceil(cpr().maxHealth * percent);
            addToBot(new HealAction(cpr(), cpr(), healAmt));
            deplete();
            stopPulse();
        }
        if (turn < 5) turn++;
    }
}