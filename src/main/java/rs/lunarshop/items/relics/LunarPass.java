package rs.lunarshop.items.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import rs.lazymankits.abstracts.LMCustomRelic;
import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.lunar.GhorTome;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.abstracts.AbstractLunarShop;
import rs.lunarshop.abstracts.lunarprops.LunarItemProp;
import rs.lunarshop.abstracts.lunarprops.LunarNpcProp;
import rs.lunarshop.ui.campfire.LunarFireOption;
import rs.lunarshop.ui.cmdpicker.ItemContainer;
import rs.lunarshop.ui.cmdpicker.PickerCaller;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.ItemSpawner;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.NpcHelper;

import java.util.ArrayList;

public final class LunarPass extends LMCustomRelic implements LunarUtils, PickerCaller {
    public static final String ID = LunarMod.Prefix("LunarPass");
    public static final int LUNAR = 0;
    public static final int VOID = 1;
    public static final int TABOO = 2;
    private static final boolean[] PASSES = new boolean[] {false, false, false};
    private static final float BASE_DROP_CHANCE = 0.1F;
    private static float DROP_CHANCE = BASE_DROP_CHANCE;
    
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
        if (ItemHelper.RollCloverLuck("LunarPass", baseChance)) {
            int drops = cardRandomRng().random(2, 3);
            if (ItemHelper.RollCloverLuck("LunarPass", 0.15F))
                drops += cardRandomRng().random(3, 6);
            currRoom().rewards.add(ItemHelper.GetLunarCoinReward(drops));
        }
    }
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        dropItemOnMonsterDeath(m);
    }
    
    private void dropItemOnMonsterDeath(AbstractMonster m) {
        LunarNpcProp prop = NpcHelper.GetProp(m.id);
        if (prop != null) {
            float baseDropRate = prop.getDropRate();
            float dropRate = weightDropRate(baseDropRate);
            boolean canDropItem = ItemHelper.RollCloverLuck("LunarPass", dropRate);
            if (!canDropItem) {
                DROP_CHANCE += miscRng().random(0.1F, 0.15F) * dropRate;
                log("no dropping item [" + DROP_CHANCE + "]");
                return;
            }
            DROP_CHANCE = BASE_DROP_CHANCE;
            int minTier = prop.getDropTierMin();
            int maxTier = prop.getDropTierMax();
            if (minTier < maxTier) {
                float higherTierChance = 0.25F;
                boolean rollTier = ItemHelper.RollLuck("LunarPass", higherTierChance);
                while (minTier < maxTier && rollTier) {
                    minTier++;
                    higherTierChance *= 0.5F;
                    rollTier = ItemHelper.RollLuck("LunarPass", higherTierChance);
                }
                maxTier = minTier;
                minTier = prop.getDropTierMin();
            }
            maxTier = minTier < maxTier ? miscRng().random(minTier, maxTier) : minTier;
            final float min = minTier;
            final float max = maxTier;
            AbstractLunarRelic r = ItemSpawner.ReturnRndExptItem(relicRng(), relic -> {
                LunarItemProp data = relic.prop;
                return data.getTier() >= min && data.getTier() <= max;
            });
            effectToList(new RelicAboveCreatureEffect(m.hb.cX - m.animX, m.hb.cY + m.hb.height / 2F - m.animY, r.makeCopy()));
            currRoom().addRelicToRewards(r);
        }
    }
    
    private float weightDropRate(float baseRate) {
        if (baseRate <= 0 || !currRoom().rewardAllowed) return 0;
        float rate = baseRate + DROP_CHANCE;
        if (!currRoom().rewards.isEmpty()) {
            for (RewardItem reward : currRoom().rewards) {
                if (reward.type == RewardItem.RewardType.RELIC && reward.relic instanceof AbstractLunarRelic) {
                    rate = rate * (1F - 0.05F);
                }
            }
        }
        if (cprHasLunarRelic(ItemHelper.GetProp(29))) {
            AbstractRelic tome = cpr().getRelic(ItemHelper.GetRelicID(29));
            if (tome instanceof GhorTome)
                rate += ((GhorTome) tome).getChanceForItem();
        }
        return rate;
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