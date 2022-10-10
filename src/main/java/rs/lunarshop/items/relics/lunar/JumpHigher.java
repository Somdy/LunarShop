package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.actions.common.BetterDamageAllEnemiesAction;
import rs.lunarshop.interfaces.relics.BlockModifierRelic;
import rs.lunarshop.items.abstracts.LunarRelic;

public class JumpHigher extends LunarRelic implements BlockModifierRelic {
    private int extraBlock;
    
    public JumpHigher() {
        super(11);
        setBattleUse();
        extraBlock = 2;
    }
    
    @Override
    public void refreshStats() {
        extraBlock = 2 * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], extraBlock);
    }
    
    @Override
    public float onGainBlock(AbstractCreature target, float blockAmt) {
        if (target == cpr()) {
            blockAmt += extraBlock;
        }
        return BlockModifierRelic.super.onGainBlock(target, blockAmt);
    }
    
    @Override
    protected void use() {
        if (cpr().currentBlock > 0) {
            int damage = cpr().currentBlock;
            addToBot(new LoseBlockAction(cpr(), cpr(), damage));
            addToBot(new BetterDamageAllEnemiesAction(crtDmgInfo(cpr(), damage, DamageInfo.DamageType.THORNS), 
                    damage >= 45 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT, 
                    true).setPureDmg());
            deplete();
        }
    }
}