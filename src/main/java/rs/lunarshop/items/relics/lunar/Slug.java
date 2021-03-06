package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.SlugPower;

public class Slug extends LunarRelic {
    private int regen;
    private boolean firstTurn;
    
    public Slug() {
        super(ItemID.Slug);
        counter = -1;
        regen = 3;
    }
    
    @Override
    public void refreshStats() {
        regen = 2 + stack;
    }
    
    @Override
    public void constructInfo() {
        if (counter == -2) {
            createStatsInfo(DESCRIPTIONS[1], regen);
        } else {
            createStatsInfo(DESCRIPTIONS[2]);
        }
    }
    
    @Override
    public void onEnterRestRoom() {
        super.onEnterRestRoom();
        flash();
        counter = -2;
    }
    
    @Override
    public void atPreBattle() {
        super.atPreBattle();
        firstTurn = true;
    }
    
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        if (firstTurn) {
            firstTurn = false;
            if (counter == -2) {
                counter = -1;
                flash();
                addToTop(new ApplyPowerAction(cpr(), cpr(), new SlugPower(cpr(), regen, 2)));
            }
        }
    }
}
