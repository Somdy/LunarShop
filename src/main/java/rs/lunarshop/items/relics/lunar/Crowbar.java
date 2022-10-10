package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Crowbar extends LunarRelic {
    private float damageMult;
    
    public Crowbar() {
        super(35);
        damageMult = 0.5F;
    }
    
    @Override
    public void refreshStats() {
        damageMult = 0.5F * stack;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(damageMult));
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        super.preModifyDamage(info, who);
        if (info.owner == cpr() && info.type == DamageInfo.DamageType.NORMAL
                && who.currentHealth > MathUtils.floor(who.maxHealth * 0.85F)) {
            info.output = info.output + MathUtils.ceil(info.output * damageMult);
            info.isModified = info.base != info.output;
        }
    }
}