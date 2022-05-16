package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.ItemSpawner;

import java.util.Optional;

public class GhorTome extends LunarRelic {
    private static final float EXTRA_GOLD = 0.15F;
    private static int baseGold = 30;
    private static int extraGold = 25;
    private float chance;
    private float chanceForItem;
    
    public GhorTome() {
        super(ItemID.GhorTome);
        chance = 0.30F;
        chanceForItem = chance / 4F;
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
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (rollCloverLuck(chance)) {
            int golds = baseGold;
            if (rollCloverLuck(EXTRA_GOLD))
                golds += ItemHelper.GetItemRng(29).random(extraGold);
            currRoom().addGoldToRewards(golds);
            flash();
        }
        if (rollCloverLuck(chanceForItem)) {
            Optional<AbstractLunarRelic> opt = ItemSpawner.PopulateLimitedRndRelicForReward(LunarRarity.RARE);
            opt.ifPresent(r -> {
                log("Get " + r.name + " by killing " + m.name);
                currRoom().addRelicToRewards(r.makeCopy());
            });
            flash();
        }
    }
}