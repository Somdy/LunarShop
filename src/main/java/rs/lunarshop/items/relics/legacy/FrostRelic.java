package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.items.abstracts.LegacyRelic;

public class FrostRelic extends LegacyRelic {
    public FrostRelic() {
        super(91);
    }
    
    @Override
    public void onProbablyKillMonster(DamageInfo info, int damageAmt, AbstractMonster m) {
        if (isDead(m)) {
            if (counter < 0) {
                counter = 3;
            } else {
                counter += 3;
            }
        }
        super.onProbablyKillMonster(info, damageAmt, m);
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        if (counter > 0 && who != cpr() && info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL) {
            counter--;
            info.output += 3;
            info.isModified = info.base != info.output;
        }
    }
}