package rs.lunarshop.cards.temp;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.abstracts.LMCustomCard;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.shops.ShopType;

public class PurpleCampfire extends LMCustomCard {
    public static final String ID = LunarMod.Prefix("PurpleCampfire");
    private static final String NAME = "紫色火焰";
    public static final String IMG = "LunarAssets/imgs/items/cards/misc/purple_fire.png";
    public static final String DESCRIPTION = "尝试与存在于低语声中的入侵者联系";
    
    public PurpleCampfire() {
        super(ID, NAME, IMG, -2, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }
    
    @Override
    public void upgrade() {}
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}
    
    @Override
    public void onChoseThisOption() {
        LunarMaster.ShopManager.setCurrShop(ShopType.VOID);
        LunarMaster.PickUpLunarCoin(20);
        LunarMod.addToBot(new QuickAction(() -> LunarMaster.ShopManager.openShop()));
    }
}