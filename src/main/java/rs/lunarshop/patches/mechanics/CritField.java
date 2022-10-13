package rs.lunarshop.patches.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

@SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
public class CritField {
    public static SpireField<Float> critChance = new SpireField<>(() -> 0F);
    public static SpireField<Float> critMult = new SpireField<>(() -> 0F);
}