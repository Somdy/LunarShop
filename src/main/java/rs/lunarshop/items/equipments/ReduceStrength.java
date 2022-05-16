package rs.lunarshop.items.equipments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.powers.CripplePower;
import rs.lunarshop.subjects.AbstractLunarEquipment;

public class ReduceStrength extends LunarEquipment {
    private int useLeft;
    private final int strLoss;
    
    public ReduceStrength() {
        super(ItemID.ReduceStrength, 8);
        setTargetRequired(false);
        useLeft = 6;
        strLoss = 2;
    }
    
    @Override
    public void constructInfo() {
        super.constructInfo();
        createStatsInfo(DESCRIPTIONS[1], useLeft);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        useLeft = 6;
        updateProxyData(getProxy());
    }
    
    @Override
    protected void activate() {
        super.activate();
        if (useLeft > 0) {
            if (isProxy()) {
                addToBot(new VFXAction(new ShockWaveEffect(currentX, currentY, Color.SCARLET,
                        ShockWaveEffect.ShockWaveType.CHAOTIC), 0.5F));
                addToBot(new QuickAction(() -> {
                    for (AbstractCreature crt : getAllLivingCreatures()) {
                        addToTop(new ApplyPowerAction(crt, null, new CripplePower(crt, 1)));
                        addToTop(new ApplyPowerAction(crt, null, new StrengthPower(crt, -strLoss)));
                        addToBot(new VFXAction(crt, new VerticalAuraEffect(Color.ROYAL, crt.hb.cX, crt.hb.cY), 0F));
                    }
                }));
                startCooldown();
            }
            useLeft--;
            updateExtraTips();
        }
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (isCardTypeOf(card, AbstractCard.CardType.ATTACK))
            reduceCooldown();
    }
    
    @Override
    protected void updateProxyData(AbstractLunarEquipment proxy) {
        super.updateProxyData(proxy);
        if (proxy instanceof ReduceStrength)
            ((ReduceStrength) proxy).useLeft = useLeft;
    }
}