package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lunarshop.items.abstracts.LunarRelic;

import java.util.ArrayList;
import java.util.List;

public class Ukulele extends LunarRelic {
    private static final float CHANCE = 0.25F;
    private int connect;
    
    public Ukulele() {
        super(62);
        connect = 1;
    }
    
    @Override
    public void refreshStats() {
        connect = stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], connect);
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        if (info.owner == cpr() && damageAmount > 0 && rollCloverLuck(CHANCE)) {
            List<AbstractMonster> rest = getAllExptMstrs(m -> m != target && !m.isDeadOrEscaped());
            if (!rest.isEmpty()) {
                int damage = MathUtils.floor(damageAmount * 0.8F);
                List<AbstractMonster> others = new ArrayList<>();
                for (int i = 0; i < connect; i++) {
                    int random = cardRandomRng().random(rest.size() - 1);
                    others.add(rest.remove(random));
                    if (rest.isEmpty()) break;
                }
                for (AbstractMonster m : others) {
                    addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
                    addToBot(new VFXAction(new LightningEffect(m.hb.cX, m.hb.cY)));
                    addToBot(new RelicAboveCreatureAction(m, this));
                    addToBot(new NullableSrcDamageAction(m, crtDmgInfo(null, damage, DamageInfo.DamageType.THORNS)));
                }
            }
        }
    }
}