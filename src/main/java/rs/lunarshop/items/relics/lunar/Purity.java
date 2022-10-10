package rs.lunarshop.items.relics.lunar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.interfaces.InstantOnUseCardInterface;
import rs.lunarshop.interfaces.relics.LuckModifierRelic;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.ItemHelper;

public final class Purity extends LunarRelic implements LuckModifierRelic, InstantOnUseCardInterface {
    private static int cardMulti;
    private static int maxEnergy;
    private int roomCount;
    
    public Purity() {
        super(6);
        cardMulti = 1;
        maxEnergy = 3;
        roomCount = 0;
    }
    
    @Override
    public void refreshStats() {
        cardMulti = 1 + (stack - 1);
        maxEnergy = 3 - (stack - 1);
        if (maxEnergy < 0) maxEnergy = 0;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], cardMulti, maxEnergy);
    }
    
    @Override
    public float modifyLuck(float origin) {
        return origin - stack;
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        beginPulse();
    }
    
    @Override
    public void onVictory() {
        super.onVictory();
        stopPulse();
    }
    
    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        roomCount++;
    }
    
    public void checkAchv() {
        if (roomCount >= 5 && stack >= 2) {
            AchvHelper.UnlockAchv(AchvID.PureCalm);
        }
    }
    
    @Override
    protected void saveThings(RelicConfigBuilder builder) {
        super.saveThings(builder);
        builder.map("PurityCount", Integer.toString(roomCount));
    }
    
    @Override
    protected void loadThings(LunarConfig config) {
        super.loadThings(config);
        if (config.hasMapKey("PurityCount")) {
            roomCount = Integer.parseInt(config.getMapValue("PurityCount"));
        }
    }
    
    private static void TriggerEffect() {
        AbstractDungeon.player.getRelic(ItemHelper.GetRelicID(6)).flash();
    }
    
    private static boolean Enabled() {
        return LMSK.Player().hasRelic(ItemHelper.GetRelicID(6));
    }
    
    @Override
    public void instantOnUseCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        if (cprHasLunarRelic(6) && card != null) {
            for (int i = 0; i < cardMulti; i++) {
                card.use(p, m);
            }
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class MultiCardUseEffectPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(AbstractPlayer _inst, AbstractCard card, AbstractMonster m, int e) {
            if (Enabled() && card != null) {
                for (int i = 0; i < cardMulti; i++) {
                    card.use(_inst, m);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
    
    public static class EnergyLimitPatches {
        @SpirePatch(clz = EnergyPanel.class, method = "addEnergy")
        public static class GainLimitPatch {
            @SpirePrefixPatch
            public static void Prefix(@ByRef int[] e) {
                if (Enabled() && e[0] + EnergyPanel.totalCount > maxEnergy) {
                    TriggerEffect();
                    e[0] = Math.abs(maxEnergy - EnergyPanel.totalCount);
                    if (e[0] < 0) e[0] = 0;
                }
            }
        }
    
        @SpirePatch(clz = EnergyPanel.class, method = "setEnergy")
        public static class SetLimitPatch {
            @SpirePrefixPatch
            public static void Prefix(@ByRef int[] e) {
                if (Enabled() && e[0] > maxEnergy) {
                    TriggerEffect();
                    e[0] = maxEnergy;
                    if (e[0] < 0) e[0] = 0;
                }
            }
        }
        
        @SpirePatch(clz = EnergyPanel.class, method = "useEnergy")
        public static class UseLimitPatch {
            @SpirePrefixPatch
            public static void Prefix(@ByRef int[] e) {
                if (Enabled() && e[0] < 0 && EnergyPanel.totalCount - e[0] > maxEnergy) {
                    TriggerEffect();
                    e[0] = EnergyPanel.totalCount - maxEnergy;
                }
            }
        }
    
        @SpirePatch(clz = EnergyPanel.class, method = "update")
        public static class UpdateCheckPatch {
            @SpirePrefixPatch
            public static void Prefix(EnergyPanel _inst) {
                if (Enabled() && EnergyPanel.totalCount > maxEnergy)
                    EnergyPanel.totalCount = maxEnergy;
            }
            @SpirePostfixPatch
            public static void Postfix(EnergyPanel _inst) {
                if (Enabled() && EnergyPanel.totalCount > maxEnergy)
                    EnergyPanel.totalCount = maxEnergy;
            }
        }
    }
}