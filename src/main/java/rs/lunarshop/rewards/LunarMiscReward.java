package rs.lunarshop.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.RustyKey;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.LunarUtils;

import static rs.lunarshop.patches.MiscRewardEnum.LUNAR_COIN;
import static rs.lunarshop.patches.MiscRewardEnum.OLD_CHEST;

public class LunarMiscReward extends CustomReward implements LunarUtils {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("MiscReward"));
    public static final String[] TEXT = uiStrings.TEXT;
    
    public int coins;
    
    public LunarMiscReward(Texture icon, String text, RewardType type) {
        super(icon, text, type);
    }
    
    @Override
    public boolean claimReward() {
        if (type == LUNAR_COIN) {
            if (coins > 0) {
                LunarMaster.PickUpLunarCoin(coins);
            }
            return true;
        } else if (type == OLD_CHEST) {
            LunarMod.addToBot(new QuickAction(this::openOldChest));
            return true;
        } else {
            warn("Unexpected lunar misc reward type: " + type.name());
        }
        return false;
    }
    
    public void openOldChest() {
        for (AbstractRelic r : LMSK.Player().relics) {
            r.onChestOpen(false);
        }
        playSound("CHEST_OPEN");
        AbstractLunarRelic item = RustyKey.SpawnItemForChest();
        RustyKey.ConsumeAll();
        AbstractDungeon.combatRewardScreen.rewards.add(0, new RewardItem(item));
        AbstractDungeon.combatRewardScreen.positionRewards();
//        for (RewardItem reward : currRoom().rewards) {
//            log("Reward[" + currRoom().rewards.indexOf(reward) + "]: " + reward.text);
//        }
        for (AbstractRelic r : LMSK.Player().relics) {
            r.onChestOpenAfter(false);
        }
    }
}