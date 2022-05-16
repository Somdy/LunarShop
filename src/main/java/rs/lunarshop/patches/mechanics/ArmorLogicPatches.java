package rs.lunarshop.patches.mechanics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.ArmorHelper;

import java.lang.reflect.Field;

public class ArmorLogicPatches {
    private static final Color ARMOR = LMSK.Color(255, 193, 37);
    public static float BLOCK_ICON_X = -1F;
    public static float BLOCK_ICON_Y = -1F;
    
    @SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
    public static class RenderArmorPatch {
        @SpireInsertPatch(rloc = 29, localvars = {"x", "y"})
        public static void Insert(AbstractCreature _inst, SpriteBatch sb, float x, float y) throws Exception {
            if (ArmorHelper.HasArmor(_inst) && LunarMod.OmniPanel.showDetails()) {
                if (BLOCK_ICON_X == -1) {
                    Field blockX = AbstractCreature.class.getDeclaredField("BLOCK_ICON_X");
                    blockX.setAccessible(true);
                    BLOCK_ICON_X = blockX.getFloat(_inst);
                }
                if (BLOCK_ICON_Y == -1) {
                    Field blockY = AbstractCreature.class.getDeclaredField("BLOCK_ICON_Y");
                    blockY.setAccessible(true);
                    BLOCK_ICON_Y = blockY.getFloat(_inst);
                }
                renderArmor(_inst, sb, x, y);
            }
        }
        private static void renderArmor(AbstractCreature _inst, @NotNull SpriteBatch sb, float x, float y) {
            x += _inst.hb.width;
            sb.setColor(ARMOR.cpy());
            sb.draw(ImageMaster.BLOCK_ICON, x - BLOCK_ICON_X - 32F, y + BLOCK_ICON_Y - 32F, 32F, 32F, 
                    64F, 64F, Settings.scale, Settings.scale, 0F, 0, 0, 
                    64, 64, false, false);
            FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, String.valueOf(ArmorHelper.GetArmor(_inst)), 
                    x - BLOCK_ICON_X + Settings.scale, y - 16F * Settings.scale, Color.WHITE.cpy(), 1F);
        }
    }
    
    @SpirePatches({
            @SpirePatch(clz = AbstractPlayer.class, method = "damage"),
            @SpirePatch(clz = AbstractMonster.class, method = "damage")
    })
    public static class PlayerDamagePatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void Insert(AbstractCreature _inst, DamageInfo info, @ByRef int[] dmg) {
            if (ArmorHelper.HasArmor(_inst)) {
                dmg[0] = MathUtils.round(dmg[0] * ArmorHelper.DamageMultiplier(_inst));
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher matcher = new Matcher.FieldAccessMatcher(DamageInfo.class, "owner");
                int line = LineFinder.findAllInOrder(ctBehavior, matcher)[0];
                return new int[] {line};
            }
        }
    }
}