package rs.lunarshop.items.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.Beetle;
import rs.lunarshop.subjects.AbstractLunarShop;
import rs.lunarshop.ui.campfire.LunarFireOption;
import rs.lunarshop.ui.cmdpicker.ItemContainer;
import rs.lunarshop.ui.cmdpicker.PickerCaller;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.ItemHelper;

import java.util.ArrayList;

public final class LunarPass extends LMCustomRelic implements PickerCaller {
    public static final String ID = LunarMod.Prefix("LunarPass");
    public static final int LUNAR = 0;
    public static final int VOID = 1;
    public static final int TABOO = 2;
    private static final boolean[] PASSES = new boolean[] {false, false, false};
    
    public LunarPass(int pass) {
        super(ID, ImageMaster.loadImage("LunarAssets/imgs/items/relics/prism.png"),
                ImageMaster.loadImage("LunarAssets/imgs/items/relics/prism.png"), 
                RelicTier.SPECIAL, LandingSound.MAGICAL);
        activatePass(pass);
    }
    
    public LunarPass() {
        super(ID, ImageMaster.loadImage("LunarAssets/imgs/items/relics/prism.png"),
                ImageMaster.loadImage("LunarAssets/imgs/items/relics/prism.png"), 
                RelicTier.SPECIAL, LandingSound.MAGICAL);
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
    
    public void activateRndPass() {
        int random = 0;
        activatePass(random);
    }
    
    @Override
    public void onUnequip() {
        deactivatePass(LUNAR);
        deactivatePass(VOID);
        deactivatePass(TABOO);
    }
    
    public boolean hasPass(int pass) {
        return PASSES[pass];
    }
    
    public void activatePass(int pass) {
        if (!PASSES[pass]) {
            PASSES[pass] = true;
            Log("Activated pass: " + pass);
            checkPassTips();
        }
    }
    
    public void deactivatePass(int pass) {
        if (PASSES[pass]) {
            PASSES[pass] = false;
            Log("Deactivated pass: " + pass);
            checkPassTips();
        }
    }
    
    public static void ResetPass() {
        PASSES[LUNAR] = false;
        PASSES[VOID] = false;
        PASSES[TABOO] = false;
    }
    
    private void checkPassTips() {
        if (PASSES[LUNAR] && tips.stream().noneMatch(t -> t.header.equals(DESCRIPTIONS[1]))) {
            addTips(DESCRIPTIONS[1], DESCRIPTIONS[2]);
        } else {
            tips.removeIf(t -> t.header.equals(DESCRIPTIONS[1]));
        }
        
        if (PASSES[VOID] && tips.stream().noneMatch(t -> t.header.equals(DESCRIPTIONS[3]))) {
            addTips(DESCRIPTIONS[3], DESCRIPTIONS[4]);
        } else {
            tips.removeIf(t -> t.header.equals(DESCRIPTIONS[3]));
        }
        
        if (PASSES[TABOO] && tips.stream().noneMatch(t -> t.header.equals(DESCRIPTIONS[5]))) {
            addTips(DESCRIPTIONS[5], DESCRIPTIONS[6]);
        } else {
            tips.removeIf(t -> t.header.equals(DESCRIPTIONS[5]));
        }
    }
    
    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        boolean active = LunarMaster.ShopManager.isBonfireReqsMet();
        options.add(new LunarFireOption(active));
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        LunarMod.SaveConfig();
        if (AbstractDungeon.shopScreen instanceof AbstractLunarShop && !LunarMaster.ShopManager.isVisitingShop()) {
            Log("Not visiting any lunar shops, returning vanilla shop");
            AbstractDungeon.shopScreen = new ShopScreen();
        }
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        float baseChance = LunarMaster.LunarCoin() > 5 ? 0.25F : 0.6F;
        if (ItemHelper.RollCloverLuck(-1, baseChance)) {
            int drops = cardRandomRng().random(2, 3);
            if (ItemHelper.RollCloverLuck(-1, 0.15F))
                drops += cardRandomRng().random(3, 6);
            currRoom().rewards.add(ItemHelper.GetLunarCoinReward(drops));
        }
    }
    
    @Override
    protected boolean onRightClick() {
//        Optional<AbstractMonster> opt = LMSK.GetExptMstr(m -> !m.isDeadOrEscaped());
//        opt.ifPresent(m -> {
//            effectToList(new MissileAttackEffect(m, cpr().hb.cX, cpr().hb.cY, 2F)
//                    .setColor(Color.GREEN.cpy())
//                    .build());
//        });
//        AchvHelper.UnlockAchv(0);
        return super.onRightClick();
    }
    
    @Override
    public void justEnteredRoom(AbstractRoom room) {
        super.justEnteredRoom(room);
        if (!PASSES[LUNAR] && !PASSES[VOID] && !PASSES[TABOO]) {
            Log("No activated passes, activate a random one");
            activateRndPass();
        }
    }
    
    @Override
    public boolean canSpawn() {
        return false;
    }
    
    private static void Log(Object what) {
        LunarMod.LogInfo(what);
    }
    
    @Override
    public void onSelectingItem(ItemContainer ic) {
        ic.pickupRelic();
    }
    
    @Override
    public void onCancelSelection() {}
}