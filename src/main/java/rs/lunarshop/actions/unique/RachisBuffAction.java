package rs.lunarshop.actions.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.relics.lunar.PowerRachis;
import rs.lunarshop.powers.RachisPower;
import rs.lazymankits.abstracts.LMCustomGameAction;
import rs.lazymankits.actions.utility.QuickAction;

import java.util.Optional;

public class RachisBuffAction extends LMCustomGameAction {
    private final PowerRachis rachis;
    
    public RachisBuffAction(int times, PowerRachis rachis) {
        this.amount = times;
        this.rachis = rachis;
    }
    
    @Override
    public void update() {
//        if (clearBuffs) {
//            for (AbstractCreature crt : getAllExptCreatures(c -> c.hasPower(RachisPower.POWER_ID))) {
//                addToTop(new RemoveSpecificPowerAction(crt, crt, crt.getPower(RachisPower.POWER_ID)));
//            }
//            clearBuffs = false;
//        }
        if (amount > 0) {
            addToBot(new QuickAction(() -> {
                Optional<AbstractCreature> opt = rachis.RollNextLuckyDog();
                opt.ifPresent(c -> {
                    addToTop(new RelicAboveCreatureAction(c, rachis));
                    addToTop(new ApplyPowerAction(c, null, new RachisPower(c)));
                });
            }));
            addToTop(new RachisBuffAction(amount - 1, rachis));
        }
        isDone = true;
    }
}