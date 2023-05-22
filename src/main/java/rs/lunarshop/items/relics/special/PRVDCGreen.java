package rs.lunarshop.items.relics.special;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.cards.temp.NoEffectCopyCard;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.interfaces.relics.GetPurgeableCardRelic;
import rs.lunarshop.items.abstracts.PRVDAbstractCurse;
import rs.lunarshop.utils.ItemHelper;

public class PRVDCGreen extends PRVDAbstractCurse implements GetPurgeableCardRelic {
    private int selfcount;
    
    public PRVDCGreen(int curseIndex) {
        super(104, curseIndex);
        if (curseIndex == 5) counter = 0;
    }
    
    public PRVDCGreen() {
        this(0);
    }
    
    @Override
    public void onVictory() {
        int curse = getCurseIndex();
        if (curse == 6) {
            if (selfcount <= 12) selfcount++;
        }
    }
    
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        int curse = getCurseIndex();
        if (curse == 5) {
            if (counter < 5) {
                counter++;
            } else {
                counter = 0;
                NoEffectCopyCard copy = new NoEffectCopyCard(card);
                addToBot(new MakeTempCardInDrawPileAction(copy, 1, true, true));
            }
        }
    }
    
    @Override
    public int onMonsterDamagedFinally(int damage, DamageInfo info, AbstractMonster who) {
        int curse = getCurseIndex();
        if (curse == 4) {
            int max = MathUtils.ceil(who.maxHealth * 0.15F);
            if (damage > max) damage = max;
        }
        return super.onMonsterDamagedFinally(damage, info, who);
    }
    
    @Override
    public void atBattleStartPreDraw() {
        int curse = getCurseIndex();
        if (curse == 2) {
            cpr().gameHandSize--;
        }
    }
    
    @Override
    public void atBattleStart() {
        int curse = getCurseIndex();
        if (curse == 2) {
            cpr().gameHandSize++;
        } else if (curse == 3) {
            int defenses = countSpecificCards(cpr().masterDeck, c -> isCardRarityOf(c, AbstractCard.CardRarity.BASIC));
            int applies = 5 - defenses;
            if (applies > 0) {
                for (int i = 0; i < applies; i++) {
                    addToBot(new ApplyPowerAction(cpr(), cpr(), new DexterityPower(cpr(), -1)));
                }
            }
        }
    }
    
    @Override
    public boolean canPurgeCard(AbstractCard card) {
        int curse = getCurseIndex();
        if (card.hasTag(AbstractCard.CardTags.STARTER_DEFEND))
            return curse != 1;
        return true;
    }
    
    @Override
    protected void saveThings(RelicConfigBuilder builder) {
        super.saveThings(builder);
        builder.map("prvdcgreen_selfcount", selfcount);
    }
    
    @Override
    protected void loadThings(LunarConfig config) {
        super.loadThings(config);
        if (config.hasMapKey("prvdcgreen_selfcount")) {
            selfcount = Integer.parseInt(config.getMapValue("prvdcgreen_selfcount"));
            log("Loading self count [" + selfcount + "]");
        }
    }
    
    @SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class RemoveCardRewardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CombatRewardScreen __instance) {
            if (LMSK.Player().hasRelic(ItemHelper.GetRelicID(104))) {
                AbstractRelic r = LMSK.Player().getRelic(ItemHelper.GetRelicID(104));
                boolean cursed = r instanceof PRVDCGreen && ((PRVDCGreen) r).getCurseIndex() == 6 && ((PRVDCGreen) r).selfcount > 12;
                if (cursed) {
                    __instance.rewards.removeIf(reward -> reward.type == RewardItem.RewardType.CARD && reward.cards.size() > 0);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}