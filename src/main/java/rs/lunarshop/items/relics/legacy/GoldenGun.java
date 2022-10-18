package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.utils.LunarUtils;

public class GoldenGun extends LegacyRelic {
    private static final int BASE_MAX_DMG_STACK = 5;
    private static final int GOLD_RATE = 50;
    private static final int DMG_PER_GOLDS = 1;
    private int maxDmgStack;
    private int damage;
    
    public GoldenGun() {
        super(73);
        maxDmgStack = BASE_MAX_DMG_STACK;
        damage = 0;
        presetInfo(s -> {
            if (LunarUtils.RoomAvailable()) {
                damage = (cpr().gold / GOLD_RATE) * DMG_PER_GOLDS;
                if (damage > maxDmgStack)
                    damage = maxDmgStack;
                createInfo(s, damage);
            }
        });
    }
    
    @Override
    public void refreshStats() {
        maxDmgStack = BASE_MAX_DMG_STACK + (stack - 1);
        damage = (cpr().gold / GOLD_RATE) * DMG_PER_GOLDS;
        if (damage > maxDmgStack)
            damage = maxDmgStack;
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        if (info.owner == cpr() && damage > 0) {
            info.output += damage;
            info.isModified = info.base != info.output;
        }
    }
}