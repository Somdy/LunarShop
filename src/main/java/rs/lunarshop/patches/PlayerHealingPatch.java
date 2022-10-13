package rs.lunarshop.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.utils.LunarUtils;

@SpirePatch2(clz = AbstractPlayer.class, method = "heal")
public class PlayerHealingPatch {
    @SpirePrefixPatch
    public static void Prefix(@ByRef int[] healAmount) {
        if (LunarUtils.EclipseLevel() >= LoadoutManager.ECLIPSES.LV7 && healAmount[0] > 0) {
            healAmount[0] = MathUtils.floor(healAmount[0] * 0.5F);
        }
    }
}