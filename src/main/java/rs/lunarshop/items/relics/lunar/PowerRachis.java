package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.actions.unique.RachisBuffAction;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.ItemHelper;

import java.util.List;
import java.util.Optional;

public class PowerRachis extends LunarRelic {
    private int buffTimes;
    
    public PowerRachis() {
        super(5);
        buffTimes = 2;
    }
    
    @Override
    public void refreshStats() {
        buffTimes = 2 + (stack - 1);
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], buffTimes);
    }
    
//    @Override
//    public void atBattleStart() {
//        addToBot(new RachisBuffAction(buffTimes, this, false));
//    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        addToBot(new RachisBuffAction(buffTimes, this));
    }
    
    public Optional<AbstractCreature> RollNextLuckyDog() {
        List<AbstractCreature> livings = getAllExptCreatures(c -> !c.isDeadOrEscaped() || c.isPlayer);
        float playerChance = (1F / (livings.size() + 0.25F));
        if (rollCloverLuck(playerChance)) {
            return Optional.of(cpr());
        }
        livings.removeIf(c -> c.isPlayer);
        return getRandom(livings, ItemHelper.GetItemRng());
    }
}
