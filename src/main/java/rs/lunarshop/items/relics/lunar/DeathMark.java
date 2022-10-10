package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.DeathMarkPower;

import java.util.ArrayList;
import java.util.List;

public class DeathMark extends LunarRelic {
    private static final List<AbstractCreature> deaths = new ArrayList<>();
    private int turns;
    
    public DeathMark() {
        super(18);
        turns = 2;
    }
    
    @Override
    public void refreshStats() {
        turns = stack + 1;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], turns);
    }
    
    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.getCurrMapNode() != null && currRoom() != null && currRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            for (AbstractCreature c : getAllExptCreatures(crt -> !crt.isDeadOrEscaped() && crt != cpr())) {
                if (hasEnoughDebuffs(c) && notMarked(c)) {
                    deaths.add(c);
                    addToTop(new ApplyPowerAction(c, cpr(), new DeathMarkPower(c, turns)));
                }
            }
        }
    }
    
    public static boolean RemoveDeath(AbstractCreature c) {
        return deaths.remove(c);
    }
    
    private boolean hasEnoughDebuffs(@NotNull AbstractCreature c) {
        return c.powers.stream().filter(p -> isPowerTypeOf(p, AbstractPower.PowerType.DEBUFF)
                && !(p instanceof DeathMarkPower)).count() > 3;
    }
    
    private boolean notMarked(AbstractCreature c) {
        return !deaths.contains(c) && c.powers.stream().noneMatch(p -> p instanceof DeathMarkPower);
    }
}