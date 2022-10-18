package rs.lunarshop.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LunarTip extends PowerTip {
    public static final Color H_DEFAULT = Color.LIGHT_GRAY;
    public static final Color B_DEFAULT = Color.BLACK;
    public static final Color F_DEFAULT = Color.WHITE;
    public Color hColor;
    public Color bColor;
    public Color hFontColor;
    public Color bFontColor;
    private String secretMsg;
    
    public LunarTip(String header, String body, Color hColor, Color bColor, Color hFontColor, Color bFontColor) {
        super(header, body);
        this.hColor = hColor;
        this.bColor = bColor;
        this.hFontColor = hFontColor;
        this.bFontColor = bFontColor;
        secretMsg = "";
    }
    
    public LunarTip(String header, String body, Color hColor, Color bColor) {
        this(header, body, hColor, bColor, Color.WHITE, Color.WHITE);
    }
    
    public LunarTip(String header, String body) {
        this(header, body, Color.LIGHT_GRAY, Color.BLACK);
    }
    
    public LunarTip setBgColors(Color hColor, Color bColor) {
        if (hColor != null)
            this.hColor = hColor;
        if (bColor != null)
            this.bColor = bColor;
        return this;
    }
    
    public LunarTip makeMsg(String secretMsg) {
        this.secretMsg = secretMsg;
        return this;
    }
    
    public String getMsg() {
        return secretMsg;
    }
    
    public LunarTip setImage(Texture image) {
        this.img = image;
        return this;
    }
    
    public LunarTip setImage(TextureAtlas.AtlasRegion image) {
        this.imgRegion = image;
        return this;
    }
    
    public LunarTip concat(String header, String body) {
        return concat(new LunarTip(header, body));
    }
    
    public LunarTip concat(@NotNull LunarTip other) {
        header = header + other.header;
        body = body + other.body;
        return this;
    }
    
    public void clean() {
        header = null;
        body = null;
    }
    
    public boolean hasTip() {
        return header != null && !header.isEmpty() && body != null && !body.isEmpty();
    }
    
    public LunarTip cpy() {
        return new LunarTip(header, body, hColor, bColor);
    }
    
    public void copy(LunarTip from) {
        header = from.header;
        body = from.body;
        hColor = from.hColor;
        bColor = from.bColor;
    }
    
    public static LunarTip FromPowerTip(Object source, boolean checkKeyword) {
        if (source instanceof PowerTip) {
            PowerTip sourceTip = ((PowerTip) source);
            Color hColor = Color.DARK_GRAY.cpy();
            Color hFont = Color.WHITE.cpy();
            if (checkKeyword && GameDictionary.keywords.containsKey(sourceTip.header)) {
                hColor = Color.NAVY;
                hFont = Color.GOLD;
            }
            return new LunarTip(sourceTip.header, sourceTip.body, hColor, B_DEFAULT, hFont, F_DEFAULT);
        }
        LunarMod.WarnInfo("[" + source + "] is not a power tip");
        return null;
    }
    
    public static List<LunarTip> FromPowerTips(List<? extends PowerTip> sources, boolean checkKeyword, 
                                               Consumer<LunarTip> colorSetter) {
        if (!sources.isEmpty()) {
            List<LunarTip> retVal = new ArrayList<>();
            for (PowerTip sourceTip : sources) {
                LunarTip tip = new LunarTip(sourceTip.header, sourceTip.body);
                if (colorSetter != null)
                    colorSetter.accept(tip);
                if (checkKeyword && GameDictionary.keywords.containsKey(sourceTip.header)) {
                    tip.hColor = Color.NAVY;
                    tip.hFontColor = Color.GOLD;
                }
                retVal.add(tip);
            }
            return retVal;
        }
        return new ArrayList<>();
    }
}