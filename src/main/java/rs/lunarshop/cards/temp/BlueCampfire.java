package rs.lunarshop.cards.temp;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lazymankits.abstracts.LMCustomCard;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.shops.ShopType;

public class BlueCampfire extends LMCustomCard {
    public static final String ID = LunarMod.Prefix("BlueCampfire");
    private static final String NAME = "蓝色火焰";
    public static final String IMG = "LunarAssets/imgs/items/cards/misc/blue_fire.png";
    public static final String DESCRIPTION = "尝试与凝滞在错乱时空中的存在联系 NL 测试版: 获得 6 *月亮币 。";
    
    public BlueCampfire() {
        super(ID, NAME, IMG, -2, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }
    
    @Override
    public void upgrade() {}
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}
    
    @Override
    public void onChoseThisOption() {
        LunarMaster.ShopManager.setCurrShop(ShopType.LUNAR);
        LunarMaster.PickUpLunarCoin(6);
        LunarMod.addToBot(new QuickAction(() -> LunarMaster.ShopManager.openShop()));
    }
}