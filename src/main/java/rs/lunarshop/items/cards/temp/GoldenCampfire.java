package rs.lunarshop.items.cards.temp;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.abstracts.LMCustomCard;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.shops.ShopType;

public class GoldenCampfire extends LMCustomCard {
    public static final String ID = LunarMod.Prefix("GoldenCampfire");
    private static final String NAME = "金色火焰";
    public static final String IMG = "LunarAssets/imgs/items/cards/misc/golden_fire.png";
    public static final String DESCRIPTION = "尝试与囚禁在永恒财富中的存在联系";
    
    public GoldenCampfire() {
        super(ID, NAME, IMG, -2, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }
    
    @Override
    public void upgrade() {}
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}
    
    @Override
    public void onChoseThisOption() {
        LunarMaster.ShopManager.setCurrShop(ShopType.TABOO);
        LunarMaster.PickUpLunarCoin(20);
        LunarMod.addToBot(new QuickAction(() -> LunarMaster.ShopManager.openShop()));
    }
}