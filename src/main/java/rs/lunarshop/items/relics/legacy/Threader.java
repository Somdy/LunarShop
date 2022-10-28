package rs.lunarshop.items.relics.legacy;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.utils.ItemHelper;

import java.util.ArrayList;
import java.util.List;

public class Threader extends LegacyRelic {
    private static final int BASE_EXTRA = 2;
    private static final int EXTRA_PER_STACK = 1;
    private static final float DMG_MULT = 0.5F;
    private int extras;
    
    public Threader() {
        super(75);
        extras = BASE_EXTRA;
    }
    
    @Override
    public void refreshStats() {
        extras = BASE_EXTRA + (stack - 1) * EXTRA_PER_STACK;
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.ATTACK) && action.target != null) {
            int finalDamage = MathUtils.ceil(card.damage * DMG_MULT);
            List<AbstractCreature> oldTargets = new ArrayList<>();
            oldTargets.add(action.target);
            List<AbstractMonster> newTargets = getAllExptMstrs(m -> !m.isDeadOrEscaped() && !oldTargets.contains(m));
            if (!newTargets.isEmpty()) {
                flash();
                for (int i = 0; i < extras; i++) {
                    AbstractMonster target = getRandom(newTargets, ItemHelper.GetItemRng()).orElse(null);
                    if (target != null) {
                        newTargets.remove(target);
                        addToBot(damage(target, action.source, finalDamage, card.damageTypeForTurn, 
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                        oldTargets.add(0, target);
                    }
                    if (newTargets.isEmpty()) break;
                }
            }
        }
    }
}