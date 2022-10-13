package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.mechanics.AttackHelper;

public class Razorwire extends LunarRelic {
    private static final int BASE_DAMAGE = 2;
    private int damage;
    
    public Razorwire() {
        super(69);
        damage = BASE_DAMAGE;
    }
    
    @Override
    public void refreshStats() {
        damage = BASE_DAMAGE * stack;
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && !areMstrBasicallyDead()) {
            flash();
            int attack = AttackHelper.GetAttack(cpr());
            addToBot(new VFXAction(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx())));
            addToBot(damageAll(cpr(), damage + attack, AbstractGameAction.AttackEffect.NONE));
        }
        return super.onAttacked(info, damageAmount);
    }
}