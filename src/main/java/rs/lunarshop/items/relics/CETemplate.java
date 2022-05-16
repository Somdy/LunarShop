package rs.lunarshop.items.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.lunarshop.core.LunarMod;

public class CETemplate extends LMCustomRelic {
    public static final String ID = LunarMod.Prefix("CETemplate");
    private static final CETemplate CET = new CETemplate();
    
    private CETemplate() {
        super(ID, ImageMaster.loadImage("LunarAssets/imgs/items/relics/command_essence.png"),
                ImageMaster.loadImage("LunarAssets/imgs/items/relics/command_essence.png"),
                RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    
    public static CETemplate Get() {
        return CET;
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    @Override
    public boolean canSpawn() {
        return false;
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return CET;
    }
}