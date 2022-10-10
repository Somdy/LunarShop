package rs.lunarshop.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.rewards.LunarPassReward;

import java.lang.reflect.Method;
import java.util.ArrayList;

public final class NeowLunarPassRewardPatches {
    private static int OptionIndex = -1;
    private static boolean Mini = false;
    public static boolean ChosenPass = false;
    
    @SpirePatch( clz = NeowEvent.class, method = "blessing" )
    public static class LunarPassBlessingPatch {
        @SpireInsertPatch(rloc = 18, localvars = {"rewards"})
        public static void Insert(NeowEvent _inst, ArrayList<NeowReward> rewards) {
            if (LunarMod.MakePassAStarterRelic) return;
            rewards.add(new LunarPassReward());
            _inst.roomEventText.addDialogOption(rewards.get(rewards.size() - 1).optionLabel);
            OptionIndex = RoomEventDialog.optionList.size() - 1;
        }
    }
    
    @SpirePatch( clz = NeowEvent.class, method = "miniBlessing" )
    public static class LunarPassMiniBlessingPatch {
        @SpireInsertPatch(rloc = 11, localvars = {"rewards"})
        public static void Insert(NeowEvent _inst, ArrayList<NeowReward> rewards) {
            if (LunarMod.MakePassAStarterRelic) return;
            Mini = true;
            rewards.add(new LunarPassReward());
            _inst.roomEventText.addDialogOption(rewards.get(rewards.size() - 1).optionLabel);
            OptionIndex = RoomEventDialog.optionList.size() - 1;
        }
    }
    
    @SpirePatch( clz = NeowReward.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {int.class})
    public static class LunarPassMiniBlessingFixPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(NeowReward _inst, int cat) {
            if (LunarMod.MakePassAStarterRelic) return;
            if (Mini && NeowEvent.rng == null) {
                NeowEvent.rng = new Random(Settings.seed);
                Mini = false;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
    
    @SpirePatch( clz = NeowEvent.class, method = "buttonEffect" )
    public static class ObtainLunarPassPatch {
        @SpireInsertPatch(rloc = 63, localvars = {"rewards"})
        public static void Insert(NeowEvent _inst, int pressed, ArrayList<NeowReward> rewards) throws Exception {
            if (LunarMod.MakePassAStarterRelic) return;
            if (pressed != OptionIndex) {
                return;
            }
            rewards.get(pressed).activate();
            Method talk = NeowEvent.class.getDeclaredMethod("talk", String.class);
            talk.setAccessible(true);
            talk.invoke(_inst, "");
        }
    }
    
    @SpirePatch( clz = NeowEvent.class, method = "talk" )
    public static class ObtainLunarPassTalkFixPatch {
        @SpirePrefixPatch
        public static void Prefix(NeowEvent _inst, @ByRef String[] msg) {
            if (LunarMod.MakePassAStarterRelic) return;
            if (ChosenPass) {
                ChosenPass = false;
                int index = MathUtils.random(1, 5);
                msg[0] = LunarPassReward.TEXT[index];
            }
        }
    }
}