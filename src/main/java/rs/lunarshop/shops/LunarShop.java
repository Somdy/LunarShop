package rs.lunarshop.shops;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.enums.EssCallerType;
import rs.lunarshop.enums.ShopRelicEffect;
import rs.lunarshop.events.LunarMerchantEvent;
import rs.lunarshop.interfaces.EssenceCaller;
import rs.lunarshop.subjects.AbstractCommandEssence;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.subjects.AbstractLunarShop;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.EssenceSpawner;
import rs.lunarshop.utils.ItemSpawner;
import rs.lunarshop.utils.PotencyHelper;

import java.util.ArrayList;
import java.util.List;

public class LunarShop extends AbstractLunarShop implements EssenceCaller {
    private static final int MAX_RELICS = 12;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(LunarMod.Prefix("LunarShop"));
    public static final String[] TEXT = uiStrings.TEXT;
    private final List<LunarStoreEssence> essenceList;
    private LunarMerchantEvent event;
    private boolean isWild;
    
    public LunarShop(LunarMerchantEvent event) {
        super("LunarShop", ShopType.LUNAR, "LunarAssets/imgs/ui/shop/rug_lunar.png");
        essenceList = new ArrayList<>();
        this.event = event;
    }
    
    public LunarShop() {
        this(null);
    }
    
    @Override
    protected void init() {
        isWild = event != null;
        initRelics();
        initEssences();
    }
    
    private void initRelics() {
        int maxRelics = 6;
        if (cprHasLunarRelic(-1)) {
            maxRelics += cpr().getRelic("").counter;
        }
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV5))
            maxRelics += AbstractDungeon.merchantRng.random(2, 3);
        if (isWild) maxRelics += 3;
        maxRelics = Math.min(MAX_RELICS, maxRelics);
        relics.clear();
        List<AbstractLunarRelic> list = ItemSpawner.PopulateRndRelicListForShop(type, maxRelics);
        LunarStoreRelic r;
        for (AbstractLunarRelic relic : list) {
            log("Spawning relic " + relic.name + " for lunar shop");
            r = new LunarStoreRelic(relic.makeCopy(), this);
            if (!PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1))
                r.price += AbstractDungeon.merchantRng.random(2, 3);
            if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV2))
                r.price -= AbstractDungeon.merchantRng.random(0, 1);
            r.addEffect(ShopRelicEffect.FLOATING);
            relics.add(r);
        }
    }
    
    private void initEssences() {
        essenceList.clear();
        LunarStoreEssence se;
        se = new LunarStoreEssence(EssenceSpawner.GetRndLunarEssence(LMSK.MiscRng(), this), this);
        essenceList.add(se);
        se = new LunarStoreEssence(EssenceSpawner.GetRndVanillaEssence(LMSK.MiscRng(), this), this);
        essenceList.add(se);
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV3)) {
            if (AbstractDungeon.merchantRng.randomBoolean()) {
                se = new LunarStoreEssence(EssenceSpawner.GetRndLunarEssence(LMSK.MiscRng(), this), this);
            } else {
                se = new LunarStoreEssence(EssenceSpawner.GetRndVanillaEssence(LMSK.MiscRng(), this), this);
            }
            essenceList.add(se);
        }
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV4)) {
            for (LunarStoreEssence e : essenceList) {
                e.applyDiscount(0.25F);
            }
        }
    }
    
    @Override
    protected void discount() {
        log("Lunar shop doesn't accept any discount");
    }
    
    @Override
    public boolean canBuyRelic(LunarStoreRelic relic) {
        return LunarMaster.LunarCoin() >= relic.price;
    }
    
    @Override
    public void purchaseRelic(LunarStoreRelic relic) {
        if (LunarMaster.LunarCoin() >= relic.price) {
            LunarMaster.SpendLunarCoin(relic.price);
            playSound("SHOP_PURCHASE", 0.1F);
            CardCrawlGame.metricData.addShopPurchaseData(relic.relic.relicId);
            currRoom().relics.add(relic.relic);
            relic.relic.instantObtain(cpr(), cpr().relics.size(), true);
            relic.relic.flash();
            playBuySfx();
            createSpeech(buyMsg());
            relic.isPurchased = true;
        } else {
            playCantBuySfx();
            createSpeech(cantBuyMsg());
        }
    }
    
    @Override
    public boolean canBuyEssence(LunarStoreEssence essence) {
        return LunarMaster.LunarCoin() >= essence.price;
    }
    
    @Override
    public void purchaseEssence(LunarStoreEssence essence) {
        if (LunarMaster.LunarCoin() >= essence.price) {
            essence.essence.onClick();
        } else {
            playCantBuySfx();
            createSpeech(cantBuyMsg());
        }
    }
    
    private void checkRichInCoins() {
        if (essenceList.isEmpty() && relics.isEmpty())
            AchvHelper.UnlockAchv(AchvID.RichInCoins);
    }
    
    @Override
    public String buyMsg() {
        return TEXT[MathUtils.random(9, 14)];
    }
    
    @Override
    public String cantBuyMsg() {
        return TEXT[MathUtils.random(0, 8)];
    }
    
    @Override
    public void open() {
        visiting = true;
        playSound("SHOP_OPEN");
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.SHOP;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.show(ShopScreen.NAMES[12]);
        rugY = Settings.HEIGHT;
        for (LunarStoreRelic r : relics) {
            r.hide();
            if (!r.relic.isSeen) 
                UnlockTracker.markRelicAsSeen(r.relic.relicId);
        }
        for (LunarStoreEssence e : essenceList) {
            e.hide();
            e.essence.spin(false);
        }
    }
    
    @Override
    public void update() {
        updateEssences();
        updateRelics();
        updateRug();
        updateSpeech();
    }
    
    private void updateEssences() {
        essenceList.removeIf(e -> {
            boolean removed = e.isPurchased;
            if (removed) e.essence.dispose();
            return removed;
        });
        checkRichInCoins();
        float y = rugY + scaleY(300F);
        for (int i = 0; i < essenceList.size(); i++) {
            float x = Settings.WIDTH / 2F - scaleX(240F) + scale(i * 240F);
            essenceList.get(i).update(x, y);
            if (rugY <= 0) {
                essenceList.get(i).essence.spin(true);
            }
        }
    }
    
    private void updateRelics() {
        relics.removeIf(r -> r.isPurchased);
        checkRichInCoins();
        if (rugY > 0) {
            float y = rugY + scaleY(720F);
            float row = 0;
            float col = 0;
            for (int i = 0; i < relics.size(); i++) {
                if (i > 0 && i % 6 == 0) {
                    col = 0;
                    row++;
                    y -= scaleY(180F * row);
                }
                float x = Settings.WIDTH / 2F - scale(480F) + scale( col * 180F);
                col++;
                relics.get(i).update(x, y);
            }
            return;
        }
        relics.forEach(r -> r.update(-1, -1));
    }
    
    @Override
    protected void updateRug() {
        super.updateRug();
    }
    
    @Override
    public void render(SpriteBatch sb) {
        renderRug(sb);
        renderRelics(sb);
        renderEssences(sb);
        renderSpeech(sb);
    }
    
    private void renderEssences(SpriteBatch sb) {
        for (LunarStoreEssence e : essenceList) {
            if (!e.isPurchased)
                e.render(sb);
        }
    }
    
    @Override
    public void onEssenceUsedUp(AbstractCommandEssence essence) {
        if (!essenceList.isEmpty() && essenceList.stream().anyMatch(s -> s.essence == essence)) {
            LunarStoreEssence se = essenceList.stream().filter(s -> s.essence == essence).findFirst().get();
            se.isPurchased = true;
            LunarMaster.SpendLunarCoin(se.price);
            playBuySfx();
            createSpeech(buyMsg());
            AchvHelper.UnlockAchv(AchvID.LunarCmdEss);
        }
    }
    
    @Override
    public EssCallerType getType() {
        return EssCallerType.LunarShop;
    }
}