package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.relics.LuckModifierRelic;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.ItemHelper;

public class Clover extends LunarRelic implements LuckModifierRelic {
    
    public Clover() {
        super(9);
    }
    
    @Override
    public float modifyLuck(float origin) {
        return origin + stack;
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        beginPulse();
    }
    
    @Override
    public void onVictory() {
        super.onVictory();
        stopPulse();
    }
    
    public void gotLucky() {
        addToBot(new RelicAboveCreatureAction(cpr(), this));
    }
    
    public static int CloverCounts() {
        int count = 0;
        if (LMSK.Player().hasRelic(ItemHelper.GetRelicID(9))) {
            Clover r = (Clover) LMSK.Player().getRelic(ItemHelper.GetRelicID(9));
            if (r != null) count = r.stack;
        }
        return count;
    }
}