package rs.lunarshop.items.relics.lunar;

import rs.lazymankits.utils.LMSK;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.ItemHelper;

public class Fealty extends LunarRelic {
    public Fealty() {
        super(0);
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        int coins = 2;
        if (rollCloverLuck(0.15F)) {
            coins += ItemHelper.GetItemRng().random(1, 2);
        }
        currRoom().rewards.add(ItemHelper.GetLunarCoinReward(coins));
    }
    
    public static boolean SpawnNewtForHeart() {
        return LMSK.Player().hasRelic(ItemHelper.GetRelicID(0));
    }
}
