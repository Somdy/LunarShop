package rs.lunarshop.items.relics.legacy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.utils.DamageInfoTag;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.mechanics.AttackHelper;
import rs.lunarshop.vfx.combat.CustomFireballEffect;

import java.util.List;

public class IfritHorn extends LegacyRelic {
    private static final float BASE_MULT = 5F;
    private static final float MULT_PER_STACK = 0.5F;
    private static final float TRIGGER_CHANCE = 0.1F;
    private float damageMult;
    
    public IfritHorn() {
        super(76);
        damageMult = BASE_MULT;
        presetInfo(s -> {
            if (LunarUtils.RoomAvailable()) {
                int attack = AttackHelper.GetAttack(cpr());
                int finalDamage = MathUtils.ceil(attack * damageMult);
                createInfo(s, finalDamage);
            }
        });
    }
    
    @Override
    public void refreshStats() {
        damageMult = BASE_MULT + (stack - 1) * MULT_PER_STACK;
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL && rollCloverLuck(TRIGGER_CHANCE)) {
            int attack = AttackHelper.GetAttack(cpr());
            int finalDamage = MathUtils.ceil(attack * damageMult);
            if (finalDamage > 0) {
                flash();
                List<AbstractMonster> monsters = getAllLivingMstrs();
                for (AbstractMonster m : monsters) {
                    addToBot(new VFXAction(new CustomFireballEffect(currentX, currentY, m.hb.cX, m.hb.cY, Color.GOLDENROD)));
                    addToBot(damage(m, finalDamage, AbstractGameAction.AttackEffect.FIRE, DamageInfoTag.FIRE));
                }
            }
        }
    }
}