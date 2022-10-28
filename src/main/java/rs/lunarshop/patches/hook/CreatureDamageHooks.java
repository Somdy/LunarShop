package rs.lunarshop.patches.hook;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.interfaces.relics.LayDyingRelic;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.utils.mechanics.CritHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreatureDamageHooks {
    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class MonsterDamagePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster _inst, DamageInfo info) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AbstractLunarRelic)
                    ((AbstractLunarRelic) r).preModifyDamage(info, _inst);
            }
            if (info.type == DamageInfo.DamageType.HP_LOSS && LunarUtils.EclipseLevel() >= LoadoutManager.ECLIPSES.LV4) {
                info.output = info.output * 2;
            }
            if (CritHelper.CanCrit(info)) {
                float critChance = CritHelper.GetCalculatedChance(info.owner, _inst);
                boolean critical = critChance >= 1F || LMSK.MiscRng().randomBoolean(critChance);
                if (critical) {
                    float critMult = CritHelper.GetCalculatedMult(info.owner, _inst);
                    int critHitDamage = MathUtils.ceil(info.output * critMult);
                    CritHelper.SetInfoCritical(info, critHitDamage);
                }
            }
        }
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst, DamageInfo info) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AbstractLunarRelic)
                    ((AbstractLunarRelic) r).afterOneDamaged(info, _inst);
            }
        }
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void Insert(AbstractMonster _inst, DamageInfo info, int damageAmt) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AbstractLunarRelic)
                    ((AbstractLunarRelic) r).onProbablyKillMonster(info, damageAmt, _inst);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class PlayerDamagePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer _inst, DamageInfo info) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AbstractLunarRelic)
                    ((AbstractLunarRelic) r).preModifyDamage(info, _inst);
            }
            if (info.type == DamageInfo.DamageType.HP_LOSS && LunarUtils.EclipseLevel() >= LoadoutManager.ECLIPSES.LV4) {
                info.output = info.output * 2;
            }
            if (CritHelper.CanCrit(info)) {
                float critChance = CritHelper.GetCalculatedChance(info.owner, _inst);
                boolean critical = LMSK.MiscRng().randomBoolean(critChance);
                if (critical) {
                    float critMult = CritHelper.GetCalculatedMult(info.owner, _inst);
                    int critHitDamage = MathUtils.ceil(info.output * critMult);
                    CritHelper.SetInfoCritical(info, critHitDamage);
                }
            }
        }
        
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst, DamageInfo info) {
            for (AbstractRelic r : LMSK.Player().relics) {
                if (r instanceof AbstractLunarRelic)
                    ((AbstractLunarRelic) r).afterOneDamaged(info, _inst);
            }
        }
        
        @SpireInsertPatch(locator = PDLocator.class, localvars = {"damageAmount"})
        public static SpireReturn PreDying(AbstractPlayer _inst, DamageInfo info, int damageAmount) {
            if (!_inst.relics.isEmpty()) {
                boolean revive = false;
                List<AbstractRelic> tmp = new ArrayList<>();
                _inst.relics.stream().filter(r -> r instanceof LayDyingRelic).forEach(tmp::add);
                if (!tmp.isEmpty()) {
                    tmp.sort(Comparator.comparingInt(o -> ((LayDyingRelic) o).priority()));
                    for (AbstractRelic r : tmp) {
                        revive = ((LayDyingRelic) r).onDyingPreTail(_inst, _inst.currentHealth, damageAmount, info);
                        if (revive) break;
                    }
                    tmp.clear();
                }
                if (revive) return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class PDLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, 
                        "hasRelic");
                int line = LineFinder.findAllInOrder(ctBehavior, matcher)[0];
                return new int[]{line};
            }
        }
    
        @SpireInsertPatch(locator = ADLocator.class, localvars = {"damageAmount"})
        public static SpireReturn AfterDying(AbstractPlayer _inst, DamageInfo info, int damageAmount) {
            if (!_inst.relics.isEmpty()) {
                boolean revive = false;
                List<AbstractRelic> tmp = new ArrayList<>();
                _inst.relics.stream().filter(r -> r instanceof LayDyingRelic).forEach(tmp::add);
                if (!tmp.isEmpty()) {
                    tmp.sort(Comparator.comparingInt(o -> ((LayDyingRelic) o).priority()));
                    for (AbstractRelic r : tmp) {
                        revive = ((LayDyingRelic) r).onDyingAfterTail(_inst, _inst.currentHealth, damageAmount, info);
                        if (revive) break;
                    }
                    tmp.clear();
                }
                if (revive) return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class ADLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class,
                        "isDead");
                int line = LineFinder.findAllInOrder(ctBehavior, matcher)[0];
                return new int[]{line};
            }
        }
    }
}