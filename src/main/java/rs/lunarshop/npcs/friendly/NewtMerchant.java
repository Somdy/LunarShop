package rs.lunarshop.npcs.friendly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import rs.lunarshop.data.NpcID;
import rs.lunarshop.powers.unique.MirageNewtPower;
import rs.lunarshop.subjects.AbstractLunarMonster;
import rs.lunarshop.vfx.combat.NewtChargingGlowEffect;

public class NewtMerchant extends AbstractLunarMonster {
    private static final String ATLAS = "LunarAssets/imgs/npcs/merchants/lunar/skeleton.atlas";
    private static final String JSON = "LunarAssets/imgs/npcs/merchants/lunar/skeleton.json";
    
    private NewtChargingGlowEffect chargingEffect;
    
    public NewtMerchant(float x, float y) {
        super(NpcID.MerchSnecko, x, y);
        loadAnimation(ATLAS, JSON, 1F);
        AnimationState.TrackEntry e = state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.8F);
        chargingEffect = new NewtChargingGlowEffect(hb.x, hb.cY + hb.height / 2F, Color.SKY.cpy());
        chargingEffect.setHiding(false);
    }
    
    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        CardCrawlGame.music.unsilenceBGM();
        addToBot(new ApplyPowerAction(this, this, new MirageNewtPower(this, true)));
    }
    
    @Override
    public void act() {
        addToBot(new RollMoveAction(this));
    }
    
    @Override
    public void move(int roll) {
        setMove((byte) 0, Intent.UNKNOWN);
    }
    
    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                state.setAnimation(0, "Attack", false);
                state.addAnimation(0, "Idle", true, 0.0F);
            case "ATTACK_2":
                state.setAnimation(0, "Attack_2", false);
                state.addAnimation(0, "Idle", true, 0.0F);
        }
    }
    
    @Override
    public void update() {
        super.update();
        chargingEffect.update();
    }
    
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        chargingEffect.render(sb);
    }
}