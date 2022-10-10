package rs.lunarshop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.ui.loadout.LoadoutManager;

import java.util.HashMap;
import java.util.Map;

public class LunarImageMst {
    public static final int LOADOUT_SELECTED_S = 68;
    public static final int LOADOUT_MARK_S = 23;
    public static final int LAYOUT_BG_W = 330;
    public static final int LAYOUT_BG_H = 30;
    public static final int INNER_FRAME_W = 262;
    public static final int INNER_FRAME_H = 10;
    public static final int TAB_GLOW_W = 308;
    public static final int TAB_GLOW_H = 67;
    
    public static TextureAtlas AchvAtlas;
    public static TextureAtlas.AtlasRegion BG_COMMON;
    public static TextureAtlas.AtlasRegion BG_RARE;
    public static TextureAtlas.AtlasRegion BG_LEGEND;
    public static TextureAtlas.AtlasRegion F_COMMON;
    public static TextureAtlas.AtlasRegion F_RARE;
    public static TextureAtlas.AtlasRegion F_LEGEND;
    
    public static Texture Badge;
    public static Texture Attack;
    public static Texture Regen;
    public static Texture LunarCoin;
    public static Texture InfoBtn;
    public static Texture InfoBg;
    public static Texture InfoGear;
    public static Texture Meteorite;
    public static Texture LunarFireOpt;
    public static Texture Dagger;
    public static Texture CE_Container;
    public static Texture CE_Bg;
    public static Texture CE_Frame;
    public static Texture CE_InnerLine;
    public static Texture CE_CoverBg;
    public static Texture BtnNaked;
    public static Texture BtnHighlighted;
    public static Texture BlurDot;
    public static Texture OldChest;
    public static Texture LoadoutSelectedOutline;
    public static Texture LoadoutSelectedMark;
    public static Texture LoadoutTabGlow;
    public static Texture LayoutBgTop;
    public static Texture LayoutBgMid;
    public static Texture LayoutBgBot;
    public static Texture InnerFrameTop;
    public static Texture InnerFrameMid;
    public static Texture InnerFrameBot;
    public static Texture RainLevelFrame;
    public static Texture RainLevelHoveredFrame;
    public static Texture EclipseHoveredFrame;
    public static Texture LunarTipBoxTop;
    public static Texture LunarTipBoxMid;
    public static Texture LunarTipBoxBot;
    public static Texture LunarTipHeaderBg;
    public static Texture LunarTipBodyBg;
    
    public static Texture[] EquipSlots;
    public static Texture[] EclipseIcons;
    public static Texture[] RainLevelIcons;
    public static Texture[] TierBgs;
    
    public static Map<String, Texture> ArtifactIcons;
    public static Map<String, Texture> ArtifactShadows;
    
    static  {
        EquipSlots = new Texture[3];
        EclipseIcons = new Texture[8];
        TierBgs = new Texture[6];
        RainLevelIcons = new Texture[3];
        
        ArtifactIcons = new HashMap<>();
        ArtifactShadows = new HashMap<>();
    }
    
    public static void Initialize() {
        LunarMod.LogInfo("Loading texture assets");
        
        AchvAtlas = new TextureAtlas(Gdx.files.internal("LunarAssets/imgs/achvs/achvs.atlas"));
        BG_COMMON = AchvAtlas.findRegion("bg/COMMON");
        BG_RARE = AchvAtlas.findRegion("bg/RARE");
        BG_LEGEND = AchvAtlas.findRegion("bg/LEGEND");
        F_COMMON = AchvAtlas.findRegion("frame/COMMON");
        F_RARE = AchvAtlas.findRegion("frame/RARE");
        F_LEGEND = AchvAtlas.findRegion("frame/LEGEND");
        
        Badge = load("LunarAssets/imgs/badge.png");
        Attack = load("LunarAssets/imgs/ui/attack.png");
        Regen = load("LunarAssets/imgs/ui/regen.png");
        LunarCoin = load("LunarAssets/imgs/ui/shop/lunar_coin.png");
        InfoBtn = load("LunarAssets/imgs/ui/information/info_btn.png");
        InfoBg = load("LunarAssets/imgs/ui/information/info_bg.png");
        InfoGear = load("LunarAssets/imgs/ui/information/info_gear.png");
        Meteorite = load("LunarAssets/imgs/vfx/Meteorite.png");
        LunarFireOpt = load("LunarAssets/imgs/ui/campfire/campfire_lunar.png");
        Dagger = load("LunarAssets/imgs/vfx/dagger.png");
        CE_Container = load("LunarAssets/imgs/ui/cmd_picker/container.png");
        CE_Bg = load("LunarAssets/imgs/ui/cmd_picker/bg.png");
        CE_Frame = load("LunarAssets/imgs/ui/cmd_picker/frame.png");
        CE_InnerLine = load("LunarAssets/imgs/ui/cmd_picker/innerLine.png");
        CE_CoverBg = load("LunarAssets/imgs/ui/cmd_picker/coverBg.png");
        BtnNaked = load("LunarAssets/imgs/ui/button_naked.png");
        BtnHighlighted = load("LunarAssets/imgs/ui/button_highlighted.png");
        BlurDot = load("LunarAssets/imgs/vfx/blurDot2.png");
        OldChest = load("LunarAssets/imgs/ui/reward/old_chest.png");
        LoadoutSelectedOutline = load("LunarAssets/imgs/ui/loadout/loadout_selected_outline.png");
        LoadoutSelectedMark = load("LunarAssets/imgs/ui/loadout/loadout_selected_mark.png");
        LoadoutTabGlow = load("LunarAssets/imgs/ui/loadout/selected_glow.png");
        LayoutBgTop = load("LunarAssets/imgs/ui/loadout/LayoutBgTop.png");
        LayoutBgMid = load("LunarAssets/imgs/ui/loadout/LayoutBgMid.png");
        LayoutBgBot = load("LunarAssets/imgs/ui/loadout/LayoutBgBot.png");
        InnerFrameTop = load("LunarAssets/imgs/ui/loadout/InnerFrameTop.png");
        InnerFrameMid = load("LunarAssets/imgs/ui/loadout/InnerFrameMid.png");
        InnerFrameBot = load("LunarAssets/imgs/ui/loadout/InnerFrameBot.png");
        RainLevelFrame = load("LunarAssets/imgs/ui/loadout/rainLevel/frame.png");
        RainLevelHoveredFrame = load("LunarAssets/imgs/ui/loadout/rainLevel/hovered_frame.png");
        EclipseHoveredFrame = load("LunarAssets/imgs/ui/loadout/eclipseLevel/hovered_frame.png");
        LunarTipBoxTop = load("LunarAssets/imgs/ui/tipBox/tipBoxTop.png");
        LunarTipBoxMid = load("LunarAssets/imgs/ui/tipBox/tipBoxMid.png");
        LunarTipBoxBot = load("LunarAssets/imgs/ui/tipBox/tipBoxBot.png");
        LunarTipBodyBg = load("LunarAssets/imgs/ui/tipBox/tipBg.png");
        LunarTipHeaderBg = load("LunarAssets/imgs/ui/tipBox/tipHeaderBg.png");
        
        for (int i = 0; i < EquipSlots.length; i++) {
            EquipSlots[i] = load("LunarAssets/imgs/ui/equipmentSlots/slot" + (i + 1) + ".png");
        }
        for (int i = 0; i < EclipseIcons.length; i++) {
            EclipseIcons[i] = load("LunarAssets/imgs/ui/loadout/eclipseLevel/level_" + (i + 1) + ".png");
        }
        for (int i = 0; i < RainLevelIcons.length; i++) {
            RainLevelIcons[i] = load("LunarAssets/imgs/ui/loadout/rainLevel/level_" + i + ".png");
        }
        for (int i = 0; i < TierBgs.length; i++) {
            TierBgs[i] = load("LunarAssets/imgs/ui/relictier/bg_tier" + (i + 1) + ".png");
        }
    }
    
    private static Texture load(String path) {
        return ImageMaster.loadImage(path);
    }
    
    public static Texture loadLocally(String prePath, String postPath) {
        String lang = LunarUtils.GetLang();
        return ImageMaster.loadImage(prePath + lang + postPath);
    }
    
    public static TextureAtlas.AtlasRegion FindAchvItem(int key) {
        TextureAtlas.AtlasRegion img = AchvAtlas.findRegion("item/" + key);
        if (img == null) {
            img = AchvAtlas.findRegion("item/test");
        }
        return img;
    }
    
    public static Texture EclipseOf(int level) {
        if (level > 8) level = 8;
        if (level < 1) level = 1;
        return EclipseIcons[level - 1];
    }
    
    public static Texture RainLevelOf(int level) {
        if (level < 0) level = 0;
        if (level > 2) level = 2;
        LunarMod.LogInfo("Returning rain leve of " + level);
        return RainLevelIcons[level];
    }
    
    public static Texture TierBgOf(int tier) {
        if (tier >= 7) tier = 6;
        if (tier < 1) tier = 1;
        return TierBgs[tier - 1];
    }
    
    public static Texture ArtifactOf(String ID) {
        if (ArtifactIcons.containsKey(ID))
            return ArtifactIcons.get(ID);
        Texture icon = load("LunarAssets/imgs/ui/loadout/artifacts/" + ID + ".png");
        if (icon != null) {
            ArtifactIcons.put(ID, icon);
            return icon;
        }
        return ArtifactIcons.get(LoadoutManager.ARTIFACTS.KIN.toLowerCase());
    }
    
    public static Texture ArtifactShadowOf(String ID) {
        if (ArtifactShadows.containsKey(ID))
            return ArtifactShadows.get(ID);
        Texture icon = load("LunarAssets/imgs/ui/loadout/artifacts/shadow/" + ID + ".png");
        if (icon != null) {
            ArtifactShadows.put(ID, icon);
            return icon;
        }
        return ArtifactShadows.get(LoadoutManager.ARTIFACTS.KIN.toLowerCase());
    }
}