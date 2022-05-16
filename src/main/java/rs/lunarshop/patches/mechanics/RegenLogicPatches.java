package rs.lunarshop.patches.mechanics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.RegenHelper;

import static rs.lunarshop.patches.mechanics.ArmorLogicPatches.BLOCK_ICON_X;
import static rs.lunarshop.patches.mechanics.ArmorLogicPatches.BLOCK_ICON_Y;

public class RegenLogicPatches {
    private static final Color REGEN = LMSK.Color(50, 205, 50);
    
    @SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
    public static class RenderRegenPatch {
        @SpireInsertPatch(rloc = 29, localvars = {"x", "y"})
        public static void Insert(AbstractCreature _inst, SpriteBatch sb, float x, float y) {
            if (RegenHelper.HasRegen(_inst) && LunarMod.OmniPanel.showDetails()) {
                renderRegen(_inst, sb, x, y);
            }
        }
        private static void renderRegen(AbstractCreature _inst, @NotNull SpriteBatch sb, float x, float y) {
            x += _inst.hb.width;
            sb.setColor(REGEN.cpy());
            sb.draw(LunarImageMst.Regen, x - BLOCK_ICON_X - 32F, y + BLOCK_ICON_Y - 65F, 32F, 32F,
                    64F, 64F, Settings.scale, Settings.scale, 0F, 0, 0,
                    64, 64, false, false);
            FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, String.valueOf(RegenHelper.GetRegen(_inst)),
                    x - BLOCK_ICON_X + Settings.scale, y - 53 * Settings.scale, Color.WHITE.cpy(), 1F);
        }
    }
    
    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPowers")
    public static class StartTurnHealPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst) {
            if (RegenHelper.HasRegen(_inst)) {
                RegenHelper.PublishRegen(_inst);
            }
        }
    }
    
    @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
    public static class EndTurnHealPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst) {
            if (RegenHelper.HasRegen(_inst)) {
                RegenHelper.PublishRegen(_inst);
            }
        }
    }
}