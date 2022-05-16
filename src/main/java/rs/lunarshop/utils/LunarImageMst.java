package rs.lunarshop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lunarshop.core.LunarMod;

public class LunarImageMst {
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
    
    public static Texture[] EquipSlots;
    public static Texture[] EclipseIcons;
    
    public static void Initialize() {
        LunarMod.LogInfo("Loading texture assets");
        
        AchvAtlas = new TextureAtlas(Gdx.files.internal("LunarAssets/imgs/achvs/achvs.atlas"));
        BG_COMMON = AchvAtlas.findRegion("bg/COMMON");
        BG_RARE = AchvAtlas.findRegion("bg/RARE");
        BG_LEGEND = AchvAtlas.findRegion("bg/LEGEND");
        F_COMMON = AchvAtlas.findRegion("frame/COMMON");
        F_RARE = AchvAtlas.findRegion("frame/RARE");
        F_LEGEND = AchvAtlas.findRegion("frame/LEGEND");
        
        initArrays();
        
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
        
        for (int i = 0; i < EquipSlots.length; i++) {
            EquipSlots[i] = load("LunarAssets/imgs/ui/equipmentSlots/slot" + (i + 1) + ".png");
        }
        for (int i = 0; i < EclipseIcons.length; i++) {
            EclipseIcons[i] = load("LunarAssets/imgs/ui/eclipse/0" + (i + 1) + ".png");
        }
    }
    
    private static void initArrays() {
        EquipSlots = new Texture[3];
        EclipseIcons = new Texture[8];
    }
    
    private static Texture load(String path) {
        return ImageMaster.loadImage(path);
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
        return EclipseIcons[level];
    }
}