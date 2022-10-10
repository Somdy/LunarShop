package rs.lunarshop.items.equipments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.items.abstracts.LunarEquipment;

import java.util.List;

public class Opus extends LunarEquipment {
    public Opus() {
        super(47, 10);
        setTargetRequired(false);
    }
    
    @Override
    protected void use() {
        super.use();
        if (isProxy()) {
            addToBot(new VFXAction(new ShockWaveEffect(hb.cX, hb.cY, Color.ORANGE.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC), 
                    0.2F));
            addToBot(new QuickAction(() -> {
                List<AbstractCard> attacks = LMSK.GetALLUnexhaustedCards(cpr().hand.group, cpr().drawPile.group, 
                        cpr().discardPile.group);
                attacks.removeIf(c -> !isCardTypeOf(c, AbstractCard.CardType.ATTACK));
                if (!attacks.isEmpty()) {
                    for (AbstractCard c : attacks) {
                        c.modifyCostForCombat(-1);
                        c.superFlash(Color.ORANGE.cpy());
                    }
                }
            }));
            startCooldown();
        }
        updateExtraTips();
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.ATTACK) && getCardRealCost(card) >= 1 && !card.isInAutoplay)
            reduceCooldown();
    }
}