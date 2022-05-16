package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;

public class Infusion extends LunarRelic {
    private static int maxStack = 20;
    private static int perHp = 2;
    private static int currStack = 0;
    private int currHp;
    
    public Infusion() {
        super(ItemID.Infusion);
        currHp = 0;
    }
    
    @Override
    public void refreshStats() {
        maxStack = 20 * stack;
        currHp = currStack * perHp;
    }
    
    @Override
    public void constructInfo() {
        createStatsInfo(DESCRIPTIONS[1], currHp);
    }
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
        super.onMonsterDeath(m);
        if (currStack < maxStack) {
            currStack++;
            cpr().increaseMaxHp(perHp, true);
            updateExtraTips();
        }
    }
    
    @Override
    public void onUnequip() {
        super.onUnequip();
        if (currStack > 0) {
            refreshStats();
            int loss = Math.min(cpr().maxHealth - 1, currHp);
            cpr().decreaseMaxHealth(loss);
        }
    }
}