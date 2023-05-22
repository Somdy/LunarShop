package rs.lunarshop.interfaces.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnMonsterDamagedRelic {
    default int onMonsterDamagedFinally(int damage, DamageInfo info, AbstractMonster who) {
        return damage;
    }
}