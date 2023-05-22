package rs.lunarshop.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import rs.lunarshop.core.LunarMod;

public class GIFPlayer {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float frameDuration;
    private float width;
    private float height;
    private final String sheetPath;
    private final int columns;
    private final int rows;
    private final int blanks;
    private Vector2 scale;
    private Vector2 pos;
    private boolean loop;
    private boolean hidden;
    private boolean paused;
    private boolean finished;
    
    public GIFPlayer(String sheetPath, int columns, int rows, int blanks, boolean loop) {
        this.sheetPath = sheetPath;
        this.columns = columns;
        this.rows = rows;
        this.blanks = blanks;
        this.loop = loop;
        frameDuration = 0.025F;
        hidden = false;
        paused = false;
        finished = false;
        scale = new Vector2(Settings.scale, Settings.scale);
        pos = new Vector2(0F, 0F);
    }
    
    public GIFPlayer loop(boolean loop) {
        this.loop = loop;
        return this;
    }
    
    public GIFPlayer setScale(float scaleXY) {
        scale = new Vector2(scaleXY, scaleXY);
        return this;
    }
    
    public GIFPlayer setScale(float scaleX, float scaleY) {
        scale = new Vector2(scaleX, scaleY);
        return this;
    }
    
    public GIFPlayer move(float cX, float cY) {
        pos = new Vector2(cX, cY);
        return this;
    }
    
    public GIFPlayer setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
        return this;
    }
    
    public void hide() {
        hidden = true;
    }
    
    public void show() {
        hidden = false;
    }
    
    public void create() {
        Texture texture = ImageMaster.loadImage(sheetPath);
        TextureRegion[][] regions = TextureRegion.split(texture, texture.getWidth() / columns, 
                texture.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[columns * rows - blanks];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns && (i != rows - 1 || j != columns - blanks); j++) {
                frames[index++] = regions[i][j];
            }
        }
        width = texture.getWidth() * 1F / columns;
        height = texture.getHeight() * 1F / rows;
        animation = new Animation<>(frameDuration, frames);
        stateTime = 0F;
    }
    
    public void update() {
        if (!paused) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }
    
    public void render(SpriteBatch sb) {
        if (hidden) return;
        TextureRegion frame = animation.getKeyFrame(stateTime, loop);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(frame, pos.x, pos.y, (frame.getTexture().getWidth() * 1F / columns) * scale.x, 
                (frame.getTexture().getHeight() * 1F / rows) * scale.y);
        if (animation.isAnimationFinished(stateTime)) {
            if (loop) {
                stateTime = 0F;
            } else if (!finished) {
                finished = true;
            }
        }
    }
    
    public TextureRegion getKeyFrameTexture() {
        TextureRegion frame = animation.getKeyFrame(stateTime, loop);
        if (animation.isAnimationFinished(stateTime)) {
            if (loop) {
                stateTime = 0F;
            } else if (!finished) {
                finished = true;
            }
        }
        return frame;
    }
    
    public void pause() {
        paused = true;
    }
    
    public void resume() {
        paused = false;
    }
    
    public void resetPlaytime() {
        stateTime = 0;
    }
}