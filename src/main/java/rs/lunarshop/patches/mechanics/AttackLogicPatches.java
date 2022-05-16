package rs.lunarshop.patches.mechanics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.utils.AttackHelper;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.RegenHelper;

import static rs.lunarshop.patches.mechanics.ArmorLogicPatches.BLOCK_ICON_X;
import static rs.lunarshop.patches.mechanics.ArmorLogicPatches.BLOCK_ICON_Y;

public class AttackLogicPatches {
    private static final Color ATTACK = LMSK.Color(255, 0, 0);
    
    @SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
    public static class RenderAttackPatch {
        @SpireInsertPatch(rloc = 29, localvars = {"x", "y"})
        public static void Insert(AbstractCreature _inst, SpriteBatch sb, float x, float y) {
            if (AttackHelper.HasAttack(_inst) && LunarMod.OmniPanel.showDetails()) {
                renderAttack(_inst, sb, x, y);
            }
        }
        private static void renderAttack(AbstractCreature _inst, @NotNull SpriteBatch sb, float x, float y) {
            x += _inst.hb.width;
            sb.setColor(ATTACK.cpy());
            sb.draw(LunarImageMst.Attack, x - BLOCK_ICON_X - 32F, y + BLOCK_ICON_Y - 5F, 32F, 32F,
                    64F, 64F, Settings.scale, Settings.scale, 0F, 0, 0,
                    64, 64, false, false);
            FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, String.valueOf(AttackHelper.GetAttack(_inst)),
                    x - BLOCK_ICON_X + Settings.scale, y + 22F * Settings.scale, Color.WHITE.cpy(), 1F);
        }
    }
    
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyCardPowersPatch {
        @SpireInsertPatch(rloc = 34, localvars = {"tmp"})
        public static void Insert1(AbstractCard _inst, @ByRef float[] tmp) {
            tmp[0] = AttackHelper.ApplyPowersToCard(AbstractDungeon.player, tmp[0]);
        }
        @SpireInsertPatch(rloc = 83, localvars = {"tmp"})
        public static void Insert2(AbstractCard _inst, float[] tmp) {
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = AttackHelper.ApplyPowersToCard(AbstractDungeon.player, tmp[i]);
            }
        }
    }
    
    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardDamagePatch {
        @SpireInsertPatch(rloc = 44, localvars = {"tmp"})
        public static void Insert1(AbstractCard _inst, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = AttackHelper.ApplyPowersToCard(AbstractDungeon.player, tmp[0]);
        }
        @SpireInsertPatch(rloc = 113, localvars = {"tmp"})
        public static void Insert2(AbstractCard _inst, AbstractMonster mo, float[] tmp) {
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = AttackHelper.ApplyPowersToCard(AbstractDungeon.player, tmp[i]);
            }
        }
    }
    
    @SpirePatch(clz = AbstractMonster.class, method = "calculateDamage")
    public static class CalculateMonsterDamagePatch {
        @SpireInsertPatch(rloc = 41, localvars = {"tmp"})
        public static void Insert(AbstractMonster _inst, int dmg, @ByRef float[] tmp) {
            tmp[0] = AttackHelper.ApplyPowersToMonster(_inst, tmp[0]);
        }
    }
}