package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.utils.DamageInfoTag;

public class FiremanBoots extends LegacyRelic {
    private static final int BASE_DAMAGE = 15;
    private static final int DMG_PER_STACK = 5;
    private int damage;
    
    public FiremanBoots() {
        super(71);
        damage = BASE_DAMAGE;
    }
    
    @Override
    public void refreshStats() {
        damage = BASE_DAMAGE + DMG_PER_STACK * (stack - 1);
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            addToBot(damage(info.owner, damage, AbstractGameAction.AttackEffect.FIRE, DamageInfoTag.FIRE));
        }
        return super.onAttacked(info, damageAmount);
    }
}