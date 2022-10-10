package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LunarRelic;

import java.util.ArrayList;
import java.util.List;

public class Brooch extends LunarRelic {
    private static final int BASE_BLOCK = 2;
    private int block;
    private List<AbstractCreature> targets;
    
    public Brooch() {
        super(68);
        block = BASE_BLOCK;
        targets = new ArrayList<>();
    }
    
    @Override
    public void refreshStats() {
        block = BASE_BLOCK * stack;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (!targets.contains(target) && info.owner == cpr()
                && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
            targets.add(target);
            addToBot(new RelicAboveCreatureAction(target, this));
            addToBot(new GainBlockAction(cpr(), block));
        }
    }
    
    @Override
    public void onPlayerEndTurn() {
        flash();
        targets.clear();
    }
}