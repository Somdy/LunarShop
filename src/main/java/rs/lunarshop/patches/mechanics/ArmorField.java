package rs.lunarshop.patches.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

@SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
public class ArmorField {
    public static SpireField<Integer> armor = new SpireField<>(() -> 0);
}