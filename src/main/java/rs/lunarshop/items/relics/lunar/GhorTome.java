package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.ItemSpawner;

import java.util.Optional;

public class GhorTome extends LunarRelic {
    private static final float EXTRA_GOLD = 0.15F;
    private static final int baseGold = 30;
    private static final int extraGold = 25;
    private float chance;
    private float chanceForItem;
    
    public GhorTome() {
        super(29);
        chance = 0.30F;
        chanceForItem = chance / 4F;
        presetInfo(s -> createInfo(s, SciPercent(chance), baseGold, (baseGold + extraGold), SciPercent(chanceForItem)));
    }
    
    @Override
    public void refreshStats() {
        chance = 0.30F + 0.05F * (stack - 1);
        chanceForItem = chance / (4F - ((stack - 1) / 10F));
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(chance), baseGold, (baseGold + extraGold), SciPercent(chanceForItem));
    }
    
    public float getChanceForItem() {
        refreshStats();
        return chanceForItem;
    }
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (rollCloverLuck(chance)) {
            int golds = baseGold;
            if (rollCloverLuck(EXTRA_GOLD))
                golds += ItemHelper.GetItemRng().random(extraGold);
            currRoom().addGoldToRewards(golds);
            flash();
        }
    }
}