package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.interfaces.relics.LayDyingRelic;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.relics.special.DioConsumed;
import rs.lunarshop.powers.ImmunePower;

public class DioFriend extends LunarRelic implements LayDyingRelic {
    public DioFriend() {
        super(ItemID.DioFriend);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1]);
    }
    
    @Override
    public int priority() {
        return 10;
    }
    
    @Override
    public boolean onDyingPreTail(AbstractCreature who, int hp, int damage, DamageInfo info) {
        boolean revive = stack > 0 && who == cpr();
        who.currentHealth = 0;
        addToTop(new RelicAboveCreatureAction(cpr(), this));
        addToTop(new ApplyPowerAction(cpr(), cpr(), new ImmunePower(cpr(), 1)));
        int healAmount = cpr().maxHealth - cpr().currentHealth;
        cpr().currentHealth = cpr().maxHealth;
        AbstractDungeon.topPanel.panelHealEffect();
        AbstractDungeon.effectsQueue.add(new HealEffect(cpr().hb.cX - cpr().animX, cpr().hb.cY, healAmount));
        cpr().healthBarUpdatedEvent();
        loseStack(1);
        instantObtain(new DioConsumed());
        return revive;
    }
}