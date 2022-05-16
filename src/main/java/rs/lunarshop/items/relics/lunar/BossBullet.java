package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;

public class BossBullet extends LunarRelic {
    private float extra;
    
    public BossBullet() {
        super(ItemID.BossBullet);
        extra = 0.25F;
    }
    
    @Override
    public void refreshStats() {
        extra = 0.25F + 0.1F * (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(extra));
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        if (info.owner == cpr() && belongsToBoss(who)) {
            info.output = info.output + MathUtils.round(info.output * extra);
        }
    }
    
    private boolean belongsToBoss(AbstractCreature who) {
        if (who instanceof AbstractMonster) {
            return ((AbstractMonster) who).type == AbstractMonster.EnemyType.BOSS;
        }
        return false;
    }
}