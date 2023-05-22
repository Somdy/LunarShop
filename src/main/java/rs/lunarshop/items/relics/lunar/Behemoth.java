package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.InfoTagHelper;

import java.util.List;

public class Behemoth extends LunarRelic {
    private static final float SECOND_DAMAGE_MULT = 0.8F;
    private static final float BASE_AFTERMATH_MULT = 0.25F;
    private float aftermathMult;
    
    public Behemoth() {
        super(64);
        aftermathMult = BASE_AFTERMATH_MULT;
        presetInfo(s -> createInfo(s, SciPercent(aftermathMult)));
    }
    
    @Override
    public void refreshStats() {
        aftermathMult = (float) (SECOND_DAMAGE_MULT - Math.pow(SECOND_DAMAGE_MULT - BASE_AFTERMATH_MULT, stack));
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
            int second_damage = MathUtils.round(damageAmount * SECOND_DAMAGE_MULT);
            addToBot(damage(target, second_damage, AbstractGameAction.AttackEffect.BLUNT_HEAVY, InfoTagHelper.EXPLOSIVE));
            List<AbstractMonster> rest = getAllExptMstrs(m -> m != target && !m.isDeadOrEscaped());
            if (!rest.isEmpty()) {
                int waveDamage = MathUtils.round(damageAmount * aftermathMult);
                addToBot(new DamageAllButOneEnemyAction(cpr(), target, DamageInfo.createDamageMatrix(waveDamage, 
                        true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }
    }
}