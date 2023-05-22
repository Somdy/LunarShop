package rs.lunarshop.patches.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import rs.lunarshop.utils.InfoTagHelper;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class DamageInfoField {
    public static SpireField<Float> armorEfficiency = new SpireField<>(() -> 1F);
    public static SpireField<Boolean> critical = new SpireField<>(() -> false);
}