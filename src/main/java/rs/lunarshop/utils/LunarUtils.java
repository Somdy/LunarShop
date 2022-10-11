package rs.lunarshop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.actions.CustomDmgInfo;
import rs.lazymankits.actions.DamageSource;
import rs.lazymankits.utils.LMGameGeneralUtils;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.subjects.AbstractLunarRelic;
import rs.lunarshop.subjects.lunarprops.LunarItemProp;
import rs.lunarshop.ui.loadout.LoadoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public interface LunarUtils extends LMGameGeneralUtils {
    
    default String getSupportedLang() {
        return LMGameGeneralUtils.super.getSupportedLanguage(Settings.language);
    }
    
    default void log(Object what) {
        LunarMod.LogInfo(what);
    }
    
    default void deLog(Object what) {
        LunarMod.DebugLog(what);
    }
    
    default void warn(Object what) {
        LunarMod.WarnInfo(what);
    }

    default boolean cprHasLunarRelic(Predicate<AbstractLunarRelic> expt) {
        return player.relics.stream().anyMatch(r -> r instanceof AbstractLunarRelic && expt.test((AbstractLunarRelic) r));
    }

    default boolean cprHasLunarRelic(int lunarID) {
        return cprHasLunarRelic(r -> r.prop.lunarID == lunarID);
    }
    
    default boolean cprHasLunarRelic(LunarItemProp lunarID) {
        return cprHasLunarRelic(lunarID.lunarID);
    }
    
    default Optional<AbstractLunarRelic> cprExptRelic(Predicate<AbstractLunarRelic> expt) {
        return player.relics.stream().filter(r -> r instanceof AbstractLunarRelic && expt.test((AbstractLunarRelic) r)).
                findFirst().map(r -> (AbstractLunarRelic) r);
    }
    
    default Optional<AbstractLunarRelic> cprExptRelic(int lunarID) {
        return cprExptRelic(r -> r.prop.lunarID == lunarID);
    }
    
    default void repositionMouseOnRelicPopup() {
        if (InputHelper.mX > Settings.WIDTH * 0.65F) {
            Gdx.input.setCursorPosition(MathUtils.floor(Settings.WIDTH * 0.64F), InputHelper.mY);
        } else if (InputHelper.mX < Settings.WIDTH * 0.35F) {
            Gdx.input.setCursorPosition(MathUtils.floor(Settings.WIDTH * 0.36F), InputHelper.mY);
        }
    }
    
    default void playSound(String key, float pitchVar) {
        CardCrawlGame.sound.play(key, pitchVar);
    }
    
    default CustomDmgInfo convert(@NotNull DamageInfo info) {
        CustomDmgInfo cInfo = new CustomDmgInfo(new DamageSource(info.owner), info.output, info.type);
        return cInfo;
    }
    
    default <T> void distinctList(@NotNull List<T> list, Predicate<T> toRemove) {
        list.removeIf(toRemove);
    }
    
    default List<AbstractRelic> copyRelicList(@NotNull List<AbstractRelic> from, Predicate<AbstractRelic> expt) {
        return CopyRelicList(from, expt);
    }
    
    default void instantObtain(@NotNull AbstractRelic r) {
        r.instantObtain();
    }
    
    default boolean isCrit(int damageAmount, DamageInfo info) {
        return damageAmount >= MathUtils.floor(info.base * 1.5F);
    }
    
    @NotNull
    static List<AbstractRelic> CopyRelicList(@NotNull List<AbstractRelic> from, Predicate<AbstractRelic> expt) {
        List<AbstractRelic> copy = new ArrayList<>();
        from.forEach(r -> {
            if (expt.test(r))
                copy.add(r.makeCopy());
        });
        return copy;
    }
    
    @NotNull
    static List<AbstractRelic> CopyRelicList(@NotNull List<AbstractRelic> from) {
        return CopyRelicList(from, r -> true);
    }
    
    static int RainLevel() {
        return LoadoutManager.Inst.getRainLevel();
    }
    
    static int EclipseLevel() {
        return LoadoutManager.Inst.getEclipseLevel();
    }
    
    static boolean ArtifactEnabled(String artifactID) {
        return LoadoutManager.Inst.isArtifactEnabled(artifactID);
    }
    
    static String GetLang() {
        switch(Settings.language) {
            case ZHS:
                return "zhs";
            case ZHT:
                return "zht";
            default:
                return "eng";
        }
    }
}