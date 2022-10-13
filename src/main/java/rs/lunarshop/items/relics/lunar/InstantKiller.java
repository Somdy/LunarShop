package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.AchvHelper;

public class InstantKiller extends LunarRelic {
    private float threshold;
    
    public InstantKiller() {
        super(17);
        threshold = 0.11F;
        presetInfo(s -> createInfo(s, threshold));
    }
    
    @Override
    public void refreshStats() {
        threshold = 1 - 1 / (1 + 0.125F * stack);
    }
    
    @Override
    public void afterOneDamaged(DamageInfo info, AbstractCreature who) {
        super.afterOneDamaged(info, who);
        if (info.owner == cpr() && who.currentHealth > 0 && belowThreshold(who)) {
            if (who instanceof CorruptHeart)
                checkInstantKillHeart((CorruptHeart) who);
            addToTop(new InstantKillAction(who));
            addToTop(new VFXAction(new WeightyImpactEffect(who.hb.cX, who.hb.cY, Color.RED.cpy())));
            addToBot(new RelicAboveCreatureAction(who, this));
        }
    }
    
    private void checkInstantKillHeart(@NotNull CorruptHeart heart) {
        InvinciblePower p = (InvinciblePower) heart.getPower(InvinciblePower.POWER_ID);
        if (p != null) {
            int left = p.amount;
            if (left <= 0) {
                AchvHelper.UnlockAchv(AchvID.InstantKillHeart);
            }
        }
    }
    
    private boolean belowThreshold(AbstractCreature target) {
        return target.currentHealth <= MathUtils.ceil(target.maxHealth * threshold);
    }
}