package rs.lunarshop.interfaces.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface LayDyingRelic {
    int priority();
    
    /**
     * check before fairy potion and lizard tail, not checking mark of bloom yet
     * @param who the creature dying
     * @param hp current health, may be below 0
     * @return true if the creature should be brought back to life
     */
    default boolean onDyingPreTail(AbstractCreature who, int hp, int damage, DamageInfo info) {
        return false;
    }
    
    /**
     * check after fairy potion or lizard tail has been checked
     * @param who the creature dying
     * @param hp current health, may be below 0
     * @return true if the creature should be brought back to life
     * @apiNote if this is called, fairy potion or lizard tail is not obtained by player or player has mark of bloom
     */
    default boolean onDyingAfterTail(AbstractCreature who, int hp, int damage, DamageInfo info) {
        return false;
    }
}