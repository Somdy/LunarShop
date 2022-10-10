package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.rewards.chests.BossChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.ItemSpawner;

import java.util.function.Predicate;

public class RustyKey extends LunarRelic {
    private static int keyNum = 0;
    private static float opts[] = new float[3];
    private boolean treasure;
    
    public RustyKey() {
        super(60);
        keyNum = 1;
        treasure = false;
    }
    
    @Override
    public void refreshStats() {
        keyNum = stack;
        calcOpts();
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], SciPercent(opts[0]), SciPercent(opts[1]), SciPercent(opts[2]));
    }
    
    private void calcOpts() {
        int mainKeys = keyNum;
        float rrOpt = Math.max(0F, (mainKeys - 3F) / (1.5F * mainKeys));
        float ucOpt = (1F - (Math.max(rrOpt, (mainKeys - 2F) / (1.25F * mainKeys)))) / 1.25F;
        float cmOpt = 1F - (ucOpt + rrOpt);
        opts[0] = cmOpt;
        opts[1] = ucOpt;
        opts[2] = rrOpt;
    }
    
    @NotNull
    public static AbstractLunarRelic SpawnItemForChest() {
        Predicate<AbstractLunarRelic> expt;
        if (ItemHelper.RollCloverLuck(ItemHelper.GetProp(60), opts[2])) {
            expt = r -> r.prop.getRarity() == LunarRarity.RARE;
        } else if (ItemHelper.RollCloverLuck(ItemHelper.GetProp(60), opts[1])) {
            expt = r -> r.prop.getRarity() == LunarRarity.UNCOMMON;
        } else {
            expt = r -> r.prop.getRarity() == LunarRarity.COMMON;
        }
        AbstractLunarRelic item = ItemSpawner.ReturnRndExptItem(LMSK.TreasureRng(), expt);
        LunarMod.LogInfo("Get " + item.name + " from old chest");
        return item;
    }
    
    public static void ConsumeAll() {
        keyNum = 0;
        LunarMod.addToBot(new QuickAction(() -> LMSK.Player().loseRelic(ItemHelper.GetRelicID(60))));
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (room instanceof TreasureRoom) {
            TreasureRoom r = ((TreasureRoom) room);
            if (!(r.chest instanceof BossChest)) {
                treasure = true;
            }
        } else {
            treasure = false;
        }
    }
    
    @Override
    public void onChestOpen(boolean bossChest) {
        super.onChestOpen(bossChest);
        if (!bossChest && treasure) {
            flash();
            currRoom().rewards.add(ItemHelper.GetOldChestReward());
            treasure = false;
        }
    }
}