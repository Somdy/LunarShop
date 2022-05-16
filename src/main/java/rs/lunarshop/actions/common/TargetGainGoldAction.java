package rs.lunarshop.actions.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import rs.lazymankits.abstracts.LMCustomGameAction;

public class TargetGainGoldAction extends LMCustomGameAction {
    
    public TargetGainGoldAction(AbstractCreature target, AbstractCreature source, int golds) {
        this.target = target;
        this.source = source;
        this.amount = golds;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        if (duration == startDuration) {
            if (amount == 0 || target == null || target.isDeadOrEscaped()) {
                isDone = true;
                return;
            }
            for (int i = 0; i < amount; i++) {
                effectToList(new GainPennyEffect(target, source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, target.isPlayer));
            }
        }
        tickDuration();
        if (isDone && target.isPlayer) {
            cpr().gainGold(amount);
        }
    }
}
