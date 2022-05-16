package rs.lunarshop.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import rs.lunarshop.core.LunarMod;

public class RenderLunarPanelPatches {
    @SpirePatch(clz = TopPanel.class, method = "render")
    public static class TopPanelRenderPatch {
        @SpireInsertPatch(rloc = 51)
        public static void Insert(TopPanel _inst, SpriteBatch sb) {
            LunarMod.RenderLunarTopPanel(sb);
        }
    }
    
    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class PlayerRenderPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer _inst, SpriteBatch sb) {
            LunarMod.RenderLunarPanel(sb);
        }
    }
}