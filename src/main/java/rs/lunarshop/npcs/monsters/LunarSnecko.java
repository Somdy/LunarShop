package rs.lunarshop.npcs.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.*;
import rs.lazymankits.actions.utility.DamageCallbackBuilder;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.actions.unique.LunarSneckoConfuseAction;
import rs.lunarshop.actions.unique.SneckoFireMissilesAction;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.NpcID;
import rs.lunarshop.powers.BlazingPower;
import rs.lunarshop.powers.CelestinePower;
import rs.lunarshop.powers.GlacialPower;
import rs.lunarshop.powers.MalachitePower;
import rs.lunarshop.powers.unique.MirageNewtPower;
import rs.lunarshop.abstracts.AbstractLunarMonster;
import rs.lunarshop.utils.mechanics.ArmorHelper;
import rs.lunarshop.utils.PotencyHelper;
import rs.lunarshop.vfx.combat.FireIgniteEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LunarSnecko extends AbstractLunarMonster {
    private static final String ATLAS = "LunarAssets/imgs/npcs/merchants/lunar/skeleton.atlas";
    private static final String JSON = "LunarAssets/imgs/npcs/merchants/lunar/skeleton.json";
    
    private static final byte lightning = 0;
    private static final byte thrash = 1;
    private static final byte evolve = 2;
    private static final byte confuse = 3;
    private static final byte missile = 4;
    private static final byte fire = 5;
    
    private int missileCount;
    private int fireCount;
    private int lightningTimes;
    private int thrashTimes;
    private int evolveTimes;
    private int missileTimes;
    
    private boolean init;
    
    public LunarSnecko(float x, float y) {
        super(NpcID.LunarSnecko, x, y);
        type = EnemyType.BOSS;
        loadAnimation(ATLAS, JSON, 1F);
        AnimationState.TrackEntry e = state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.8F);
    }
    
    @Override
    protected void onInit() {
        super.onInit();
        if (PotencyHelper.LunarReached(PotencyHelper.LUNAR_LV1)) {
            int currMax = maxHealth;
            currMax = MathUtils.round(currMax / 2F);
            setHp(currMax);
        }
        init = false;
        missileCount = hardTime() ? 8 : 6;
        fireCount = 0;
        lightningTimes = thrashTimes = evolveTimes = missileTimes = 0;
        setDamage(lightning, 10, 1, 1);
        setDamage(thrash, 12, 2, 3);
        setDamage(evolve, 2, 2, 2);
        setDamage(confuse, 14, 0, 0);
        setDamage(missile, 2, 0, 0);
        setDamage(fire, 16, 4, 4);
        damage.add(new DamageInfo(this, getDamage(lightning)));
        damage.add(new DamageInfo(this, getDamage(thrash)));
        damage.add(new DamageInfo(this, getDamage(evolve)));
        damage.add(new DamageInfo(this, getDamage(confuse)));
        damage.add(new DamageInfo(this, getDamage(missile)));
        damage.add(new DamageInfo(this, getDamage(fire)));
        setBlock(lightning, 0, 8, 16);
        setBlock(evolve, 10, 5, 5);
        setBuff(evolve, 2, 2, 2);
        setDebuff(lightning, 2, 1, 1);
        setDebuff(thrash, 1, 1, 1);
        setDebuff(confuse, 3, 1, 1);
        setDebuff(missile, 2, 0, 1);
        setDebuff(fire, 1, 0, 1);
        ArmorHelper.SetArmor(this, 50);
    }
    
    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");
        addToBot(new ApplyPowerAction(this, this, new MirageNewtPower(this, true)));
    }
    
    @Override
    public void act() {
        switch (nextMove) {
            case lightning:
                addToBot(new ChangeStateAction(this, "ATTACK_2"));
                addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
                addToBot(new VFXAction(new LightningEffect(cpr().drawX, cpr().drawY), 0.25F));
                if (hardTime()) {
                    addToBot(new LoseBlockAction(cpr(), this, getBlock(lightning)));
                }
                addToBot(new DamageAction(cpr(), damage.get(lightning), AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(cpr(), this, new FrailPower(cpr(), getDebuff(lightning), 
                        true)));
                lightningTimes++;
                if (lightningTimes % 4 == 0) {
                    addToBot(new ApplyPowerAction(cpr(), this, new DexterityPower(cpr(), -2)));
                }
                updateFireCount();
                break;
            case thrash:
                addToBot(new AnimateHopAction(this));
                addToBot(new VFXAction(new SearingBlowEffect(cpr().hb.cX, cpr().hb.cY, thrashTimes)));
                addToBot(new DamageAction(cpr(), damage.get(thrash), AbstractGameAction.AttackEffect.NONE));
                thrashTimes++;
                modifyDamage(thrash, thrashTimes);
                damage.set(thrash, new DamageInfo(this, getDamage(thrash)));
                updateFireCount();
                break;
            case evolve:
                addToBot(new VFXAction(new InflameEffect(this), 0.1F));
                addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, getBuff(evolve))));
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, getBuff(evolve))));
                evolveTimes++;
                if (evolveTimes % 2 == 0) 
                    addToBot(new ApplyPowerAction(this, this, new BlazingPower(this, 1)));
                if (evolveTimes % 3 == 0)
                    addToBot(new ApplyPowerAction(this, this, new GlacialPower(this, 2)));
                if (evolveTimes % 4 == 0) {
                    if (monsterAiRng().randomBoolean())
                        addToBot(new ApplyPowerAction(this, this, new AngryPower(this, 2)));
                    else 
                        addToBot(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 2)));
                }
                if (evolveTimes % 5 == 0)
                    addToBot(new ApplyPowerAction(this, this, new MalachitePower(this, 1)));
                if (evolveTimes % 6 == 0)
                    addToBot(new ApplyPowerAction(this, this, new CelestinePower(this, 1)));
                updateFireCount();
                break;
            case confuse:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new SFXAction("MONSTER_SNECKO_GLARE"));
                addToBot(new VFXAction(this, new IntimidateEffect(hb.cX, hb.cY), 0.5F));
                addToBot(new FastShakeAction(cpr(), 1F, 1F));
                addToBot(new LunarSneckoConfuseAction(getDebuff(confuse)));
                updateFireCount();
                break;
            case missile:
                addToBot(new SneckoFireMissilesAction(this, missileCount, damage.get(missile)));
                missileTimes++;
                missileCount++;
                if (missileTimes % 3 == 0) {
                    missileCount = hardTime() ? 8 : 6;
                    modifyDamage(missile, missileTimes);
                    damage.set(missile, new DamageInfo(this, getDamage(missile)));
                }
                updateFireCount();
                break;
            case fire:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                for (int i = 0; i < fireCount; i++) {
                    addToBot(new VFXAction(this, new FireIgniteEffect(cpr().hb.cX + scale(MathUtils.random(-120.0F, 120.0F)), 
                            cpr().hb.cY + scale(MathUtils.random(-120.0F, 120.0F)), Color.BLUE.cpy()), 0.05F));
                    if (MathUtils.randomBoolean()) {
                        addToBot(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
                    } else {
                        addToBot(new SFXAction("GHOST_ORB_IGNITE_2", 0.3F));
                    }
                    addToBot(new DamageCallbackBuilder(cpr(), convert(damage.get(fire)), AbstractGameAction.AttackEffect.NONE, 
                            c -> {
                        if (c.lastDamageTaken > 0) {
                            addToTop(new QuickAction(() -> {
                                List<AbstractPower> powers = new ArrayList<>(cpr().powers);
                                powers.removeIf(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF));
                                Optional<AbstractPower> opt = getRandom(powers, monsterAiRng());
                                opt.ifPresent(p -> addToTop(new ReducePowerAction(cpr(), this, p, getDebuff(fire))));
                            }));
                        }
                            }));
                }
                updateFireCount();
                break;
        }
        addToBot(new RollMoveAction(this));
    }
    
    @Override
    public void move(int roll) {
//        setMove(lightning, Intent.ATTACK_DEBUFF, damage.get(lightning).base);
//        setMove(thrash, Intent.ATTACK, damage.get(thrash).base);
//        setMove(evolve, Intent.BUFF);
//        setMove(confuse, Intent.MAGIC);
//        setMove(missile, Intent.ATTACK, damage.get(missile).base, missileCount, true);
//        setMove(fire, Intent.ATTACK, damage.get(fire).base, fireCount, true);
        if (!init) {
            init = true;
            setMove(lightning, Intent.ATTACK_DEBUFF, damage.get(lightning).base);
            return;
        }
        if (lastMove(lightning)) {
            if (hardTime()) {
                setMove(evolve, Intent.BUFF);
            } else {
                setMove(thrash, Intent.ATTACK, damage.get(thrash).base);
            }
        } else if (lastMove(thrash)) {
            setMove(evolve, Intent.BUFF);
        } else if (lastMove(evolve)) {
            setMove(confuse, Intent.MAGIC);
        } else if (lastMove(confuse)) {
            if (fireCount > 0)
                setMove(fire, Intent.ATTACK, damage.get(fire).base, fireCount, true);
            else
                setMove(missile, Intent.ATTACK, damage.get(missile).base, missileCount, true);
        } else if (lastMove(fire)) {
            setMove(missile, Intent.ATTACK, damage.get(missile).base, missileCount, true);
        } else if (lastMove(missile)) {
            setMove(lightning, Intent.ATTACK_DEBUFF, damage.get(lightning).base);
        } else {
            log("???? Why not a circle");
            setMove(missile, Intent.ATTACK, damage.get(missile).base, missileCount, true);
        }
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
    
    private void updateFireCount() {
        int count = thrashTimes + evolveTimes + missileTimes + lightningTimes;
        count /= 2;
        if (count > (harderTime() ? 10 : 14)) {
            lightningTimes = 0;
            missileTimes /= 2;
            fireCount = count;
        }
        else 
            fireCount = 0;
    }
    
    @Override
    public void die() {
        super.die();
        playSound("SNECKO_DEATH");
        if (!currRoom().cannotLose) {
            onBossVictoryLogic();
            onFinalBossVictoryLogic();
            LunarMod.DefeatLunarSnecko();
        }
    }
    
    public void useSevereBite(int amount) {
        addToBot(new ChangeStateAction(this, "ATTACK_2"));
        addToBot(new VFXAction(new BiteEffect(cpr().drawX, cpr().drawY, Color.RED.cpy())));
        addToBot(new DamageCallbackBuilder(cpr(), convert(new DamageInfo(this, amount, 
                DamageInfo.DamageType.NORMAL)), AbstractGameAction.AttackEffect.NONE, c -> {
            if (c.lastDamageTaken >= amount / 2F) {
                addToTop(new QuickAction(() -> c.decreaseMaxHealth(c.lastDamageTaken)));
                addToTop(new FastShakeAction(cpr(), 1F, 1F));
            }
        }));
    }
    
    public void useInstantEvolve() {
        addToBot(new VFXAction(new InflameEffect(this), 0.1F));
        addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, getBuff(evolve))));
        addToBot(new RemoveAllPowersAction(this, true));
        evolveTimes++;
        updateFireCount();
    }
}