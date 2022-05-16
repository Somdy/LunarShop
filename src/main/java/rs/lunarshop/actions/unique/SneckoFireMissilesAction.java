package rs.lunarshop.actions.unique;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomGameAction;
import rs.lunarshop.utils.LunarUtils;
import rs.lunarshop.vfx.combat.MissileAttackEffect;

import java.util.ArrayList;
import java.util.List;

public class SneckoFireMissilesAction extends LMCustomGameAction implements LunarUtils {
    private final List<MissileAttackEffect> effects;
    private boolean missileLaunched;
    private boolean allCollided;
    
    public SneckoFireMissilesAction(@NotNull AbstractMonster m, int amount, @NotNull DamageInfo info) {
        this.source = m;
        this.amount = amount;
        this.info = convert(info);
        this.effects = new ArrayList<>();
        missileLaunched = false;
        allCollided = false;
        actionType = ActionType.DAMAGE;
    }
    
    @Override
    public void update() {
        if (!allCollided) {
            if (!missileLaunched) {
                for (int i = 0; i < amount; i++) {
                    effects.add(new MissileAttackEffect(cpr(), source.hb.cX + source.hb.width / 2F,
                            source.hb.cY + source.hb.height / 3F, MathUtils.random(2F, 2.5F))
                            .setColor(getRandom(listFromObjs(Color.BLUE.cpy(), Color.PURPLE.cpy())).get())
                            .build());
                }
                effects.forEach(this::effectToList);
                missileLaunched = true;
            }
            if (effects.stream().allMatch(e -> e.collided))
                allCollided = true;
        } else {
            for (int i = 0; i < amount; i++) {
                cpr().damage(info);
            }
            isDone = true;
        }
    }
}