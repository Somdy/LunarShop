package rs.lunarshop.rewards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowReward;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.LunarPass;
import rs.lunarshop.items.relics.RelicManager;
import rs.lunarshop.patches.NeowLunarPassRewardPatches;
import rs.lunarshop.utils.LunarUtils;

public class LunarPassReward extends NeowReward implements LunarUtils {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(LunarMod.Prefix("LunarPassReward"));
    public static final String[] TEXT = uiStrings.TEXT;
    private static final String optionDescription = TEXT[0];
    
    public LunarPassReward() {
        super(0);
        optionLabel = optionDescription;
    }
    
    @Override
    public void activate() {
        if (!LunarMod.HasPass()) {
            LunarPass p = RelicManager.GetPass();
            p.activateRndPass();
            currRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, p);
            NeowLunarPassRewardPatches.ChosenPass = true;
        }
    }
}