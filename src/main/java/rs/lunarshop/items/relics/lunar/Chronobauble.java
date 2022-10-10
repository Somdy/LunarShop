package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.actions.common.ApplySlowdownAction;
import rs.lunarshop.items.abstracts.LunarRelic;

import java.util.HashMap;
import java.util.Map;

public class Chronobauble extends LunarRelic {
    private static final Map<AbstractCreature, Integer> map = new HashMap<>();
    private static final float slowRate = 0.6F;
    private int turns;
    
    public Chronobauble() {
        super(33);
        turns = 2;
    }
    
    @Override
    public void refreshStats() {
        turns = stack + 1;
    }
    
    @Override
    public void atPreBattle() {
        super.atPreBattle();
        map.clear();
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner == cpr() && !target.isDeadOrEscaped()) {
            if (map.containsKey(target)) {
                int times = map.get(target);
                if (times % 2 == 0) {
                    addToBot(new ApplySlowdownAction(target, cpr(), slowRate, turns));
                    map.remove(target);
                }
                else
                    map.put(target, times + 1);
            } else {
                map.put(target, 1);
            }
        }
    }
}