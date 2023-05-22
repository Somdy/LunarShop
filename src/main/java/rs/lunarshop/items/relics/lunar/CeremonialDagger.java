package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.config.LunarConfig;
import rs.lunarshop.config.RelicConfigBuilder;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.vfx.combat.CeremonialDaggerPreEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CeremonialDagger extends LunarRelic {
    private final List<Integer> daggerList = new ArrayList<>();
    private final List<CeremonialDaggerPreEffect> effects = new ArrayList<>();
    private float multiplier;
    private boolean justStartBattle;
    
    public CeremonialDagger() {
        super(36);
        multiplier = 0.5F;
        counter = 0;
        justStartBattle = false;
        presetInfo(this::setInfo);
    }
    
    @Override
    public void refreshStats() {
        multiplier = 0.5F + 0.25F * (stack - 1);
    }
    
    private void setInfo(String[] rawStrings) {
        if (counter > 0 && daggerList.size() > 0) {
            int damage = MathUtils.ceil(daggerList.get(0) * multiplier);
            rawStrings[0] = createInfo(DESCRIPTIONS[1], damage);
        } else {
            rawStrings[0] = createInfo(DESCRIPTIONS[2]);
        }
    }
    
    @Override
    public void onProbablyKillMonster(DamageInfo info, int damageAmt, AbstractMonster m) {
        super.onProbablyKillMonster(info, damageAmt, m);
        if (info.owner != null && info.owner != m && isDead(m)) {
            int damage = Math.max(damageAmt, 1);
            daggerList.add(damage);
            counter += 3;
        }
    }
    
    private boolean itIsDead(AbstractCreature who) {
        if (!(who instanceof AwakenedOne) && !(who instanceof Darkling)) {
            return (who.currentHealth <= 0 || who.isDying) && !who.halfDead;
        }
        return false;
    }
    
    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStartPreDraw();
        justStartBattle = true;
        if (counter > 0 && daggerList.size() > 0) {
            final int damage = MathUtils.ceil(daggerList.get(0) * 0.5F);
            addToBot(new QuickAction(() -> {
                for (int i = 1; i <= 3; i++) {
                    final float offsetX = i == 1 ? -90F : (i == 2 ? -70 : -50F);
                    Optional<AbstractMonster> opt = getRandom(getAllLivingMstrs(), cardRandomRng());
                    opt.ifPresent(m -> {
                        addToTop(new VFXAction(new CeremonialDaggerPreEffect(this, m, offsetX,
                                crtDmgInfo(cpr(), damage, DamageInfo.DamageType.THORNS))));
                    });
                }
            }));
            counter--;
            if (counter % 3 == 0)
                daggerList.remove(0);
        }
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (justStartBattle) {
            justStartBattle = false;
            return;
        }
        if (counter > 0 && daggerList.size() > 0) {
            final int damage = MathUtils.ceil(daggerList.get(0) * 0.5F);
            if (effects.isEmpty()) {
                warn("No left effects for rest daggers???");
                return;
            }
            for (CeremonialDaggerPreEffect e : effects) {
                Optional<AbstractMonster> opt = getRandom(getAllLivingMstrs(), cardRandomRng());
                opt.ifPresent(m -> addToTop(new VFXAction(e.load(m, crtDmgInfo(cpr(), damage, DamageInfo.DamageType.THORNS)))));
            }
            effects.clear();
            counter--;
            if (counter % 3 == 0)
                daggerList.remove(0);
        }
    }
    
    public void preLoadDaggers() {
        effects.clear();
        if (counter > 0 && daggerList.size() > 0) {
            for (int i = 1; i <= 3; i++) {
                final float offsetX = i == 1 ? -90F : (i == 2 ? -70 : -50F);
                effects.add(new CeremonialDaggerPreEffect(this, offsetX));
            }
        }
    }
    
    @Override
    public void update() {
        super.update();
        if (!effects.isEmpty()) {
            for (CeremonialDaggerPreEffect e : effects) {
                e.update();
            }
        }
    }
    
    @Override
    protected void renderCustomValues(SpriteBatch sb, boolean inTopPanel) {
        super.renderCustomValues(sb, inTopPanel);
        if (!effects.isEmpty()) {
            for (CeremonialDaggerPreEffect e : effects) {
                e.render(sb);
            }
        }
    }
    
    @Override
    protected void saveThings(RelicConfigBuilder builder) {
        if (!daggerList.isEmpty()) {
            builder.map("RDaggerSize", String.valueOf(daggerList.size()));
            for (int i = 0; i < daggerList.size(); i++) {
                builder.map("RDaggerList_" + i, Integer.toString(daggerList.get(i)));
                log("Saving damage of " + i + " at " + daggerList.get(i));
            }
        }
    }
    
    @Override
    protected void loadThings(LunarConfig config) {
        if (config.hasMapKey("RDaggerSize")) {
            daggerList.clear();
            int size = Integer.parseInt(config.getMapValue("RDaggerSize"));
            for (int i = size - 1; i >= 0; i--) {
                if (config.hasMapKey("RDaggerList_" + i)) {
                    daggerList.add(0, Integer.parseInt(config.getMapValue("RDaggerList_" + i)));
                    log("Loading saved damage of " + i + " at " + config.getMapValue("RDaggerList_" + i));
                }
            }
        }
    }
}