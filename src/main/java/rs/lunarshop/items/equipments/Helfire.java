package rs.lunarshop.items.equipments;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.powers.HelfirePower;

public class Helfire extends LunarEquipment {
    public Helfire() {
        super(ItemID.Helfire, 12);
        setTargetRequired(false);
    }
    
    @Override
    protected void activate() {
        super.activate();
        if (isProxy()) {
            addToBot(new QuickAction(() -> {
                for (AbstractCreature crt : getAllLivingCreatures()) {
                    addToTop(new ApplyPowerAction(crt, cpr(), new HelfirePower(crt, 3, !crt.isPlayer)));
                    addToTop(new VFXAction(new FlashAtkImgEffect(crt.hb.cX, crt.hb.cY, AbstractGameAction.AttackEffect.FIRE)));
                }
            }));
            startCooldown();
        }
        updateExtraTips();
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.SKILL))
            reduceCooldown();
    }
}