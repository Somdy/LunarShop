package rs.lunarshop.rewards;

import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import rs.lunarshop.utils.LunarUtils;

import static rs.lunarshop.patches.MiscRewardEnum.LINKED_REWARD;

public class LunarLinkRewardItem extends CustomReward implements LunarUtils {
    
    public LunarLinkRewardItem(AbstractRelic relic, RewardItem linkedReward) {
        super(relic.img, "Blah-Blah-Blah", LINKED_REWARD);
        this.relic = relic;
        relicLink = linkedReward;
        linkedReward.relicLink = this;
    }
    
    
    @Override
    public boolean claimReward() {
        if (!ignoreReward) {
            relic.instantObtain();
        }
        relicLink.isDone = true;
        relicLink.ignoreReward = true;
        return true;
    }
}
