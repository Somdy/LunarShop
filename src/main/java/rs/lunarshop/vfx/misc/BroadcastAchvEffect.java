package rs.lunarshop.vfx.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import rs.lazymankits.abstracts.LMCustomGameEffect;
import rs.lunarshop.abstracts.AbstractLunarAchievement;
import rs.lunarshop.utils.LunarUtils;

public class BroadcastAchvEffect extends LMCustomGameEffect implements LunarUtils {
    public static final int BOARD_W = 400;
    public static final int BOARD_H = 200;
    public static final int ACHV_OFFSET_Y = 100;
    public static final int ACHV_OFFSET_X = 115;
    public static final int MSG_OFFSET_X = 277;
    static Texture board = ImageMaster.loadImage("LunarAssets/imgs/ui/achv_board.png");
    AbstractLunarAchievement achv;
    Hitbox hb;
    Vector2 pos;
    Vector2 dest;
    Color msgColor;
    String title;
    String unlocked = "解锁成就";
    boolean fadingStarted;
    
    BroadcastAchvEffect(AbstractLunarAchievement achv) {
        this.achv = achv;
        title = achv.title;
        hb = new Hitbox(scale(BOARD_W), scale(BOARD_H));
        color = Color.WHITE.cpy();
        msgColor = achv.data.tier.color.cpy();
    }
    
    void init(float sX, float sY) {
        pos = new Vector2(sX, sY);
        hb.move(sX, sY);
        fadingStarted = false;
    }
    
    @Override
    public void update() {
        pos.x = MathHelper.fadeLerpSnap(pos.x, dest.x);
        pos.y = MathHelper.fadeLerpSnap(pos.y, dest.y);
        hb.move(pos.x, pos.y);
        hb.update();
        if (fadingStarted) {
            color.a = MathHelper.slowColorLerpSnap(color.a, 0F);
        } else {
            color.a = 1F;
        }
        msgColor.a = color.a;
        if (Math.abs(color.a - 0F) <= 0.05F) {
            color.a = 0F;
            msgColor.a = 0F;
            isDone = true;
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        hb.render(sb);
        sb.setColor(color);
        sb.draw(board, hb.cX - BOARD_W / 2F, hb.cY - BOARD_H / 2F,
                BOARD_W / 2F, BOARD_H / 2F, BOARD_W, BOARD_H,
                Settings.scale, Settings.scale, 0F, 0, 0, BOARD_W, BOARD_H, false, false);
        achv.renderAtBroadcast(sb, hb.cX - BOARD_W / 2F + ACHV_OFFSET_X,
                hb.cY - BOARD_H / 2F + ACHV_OFFSET_Y, scale * 0.875F, color);
        FontHelper.renderFontCentered(sb, FontHelper.charDescFont, title, 
                hb.cX - BOARD_W / 2F + MSG_OFFSET_X, hb.cY - hb.height * 0.15F, msgColor);
        FontHelper.renderFontCentered(sb, FontHelper.charDescFont, unlocked,
                hb.cX - BOARD_W / 2F + MSG_OFFSET_X, hb.cY + hb.height * 0.15F, msgColor);
    }
    
    @Override
    public void dispose() {
        
    }
}