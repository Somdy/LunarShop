package rs.lunarshop.actions.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import rs.lazymankits.abstracts.LMCustomGameAction;
import rs.lunarshop.powers.SlowdownPower;

public class ApplySlowdownAction extends LMCustomGameAction {
    private float percent;
    
    public ApplySlowdownAction(AbstractCreature target, AbstractCreature source, float percent, int turns) {
        this.target = target;
        this.source = source;
        this.percent = percent;
        this.amount = turns;
        actionType = ActionType.DEBUFF;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }
    
    @Override
    public void update() {
        isDone = true;
        if (target != null && !target.isDeadOrEscaped()) {
            if (target.powers.stream().anyMatch(p -> p.ID.equals(SlowdownPower.POWER_ID))) {
                try {
                    SlowdownPower p = (SlowdownPower) target.getPower(SlowdownPower.POWER_ID);
                    p.stackPower(amount);
                    p.stackSlow(percent);
                    p.updateDescription();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            addToTop(new ApplyPowerAction(target, source, new SlowdownPower(target, percent, amount)));
        }
    }
}