package rs.lunarshop.items.relics.legacy;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.patches.mechanics.DamageInfoField;

public class HeavenCracker extends LegacyRelic {
    private static final int CARDS_NEED = 3;
    private static final float BASE_MULT = 1.5F;
    private static final float MULT_PER_STACK = 0.5F;
    private float damageMult;
    private boolean onCharged;
    
    public HeavenCracker() {
        super(74);
        onCharged = false;
        counter = 0;
        damageMult = BASE_MULT;
        presetInfo(s -> createInfo(s, SciPercent(damageMult)));
    }
    
    @Override
    public void refreshStats() {
        damageMult = BASE_MULT + (stack - 1) * MULT_PER_STACK;
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.ATTACK)) {
            if (!onCharged) {
                counter++;
                if (counter > 0 && counter % CARDS_NEED == 0) {
                    onCharged = true;
                    beginLongPulse();
                }
            } else if (card.damage > 0) {
                counter = 0;
                onCharged = false;
                stopPulse();
                addToBot(new RelicAboveCreatureAction(cpr(), this));
                atbTmpAction(() -> {
                    int finalDamage = MathUtils.ceil(card.damage * damageMult);
                    addToTop(new VFXAction(new MindblastEffect(cpr().dialogX, cpr().hb.cY, cpr().flipHorizontal)));
                    for (AbstractMonster m : getAllLivingMstrs()) {
                        CustomDmgInfo info = crtDmgInfo(cpr(), finalDamage, DamageInfo.DamageType.THORNS);
                        DamageInfoField.armorEfficiency.set(info, 0.5F);
                        addToTop(new NullableSrcDamageAction(m, info, AbstractGameAction.AttackEffect.NONE));
                    }
                });
            }
        }
    }
}