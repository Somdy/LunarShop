package rs.lunarshop.core;

import basemod.BaseMod;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;

@SpireInitializer
public class LunarTime implements PostUpdateSubscriber, PostDungeonUpdateSubscriber {
    private static double globalTime;
    private static float gameTime;
    private static float dungeonTime;
    
    public static void initialize() {
        globalTime = 0;
        gameTime = 0;
        dungeonTime = 0;
        BaseMod.subscribe(new LunarTime());
    }
    
    public static double GlobalTime() {
        return globalTime;
    }
    
    public static float GameTime() {
        return gameTime;
    }
    
    public static float DungeonTime() {
        return dungeonTime;
    }
    
    @Override
    public void receivePostUpdate() {
        globalTime += Gdx.graphics.getDeltaTime();
        gameTime = (float) ((globalTime % 3600) * 1.25);
    }
    
    @Override
    public void receivePostDungeonUpdate() {
        dungeonTime = CardCrawlGame.playtime % 60F;
    }
}