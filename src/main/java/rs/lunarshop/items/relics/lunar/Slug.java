package rs.lunarshop.items.relics.lunar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.powers.SlugPower;

public class Slug extends LunarRelic {
    private int regen;
    private boolean firstTurn;
    
    public Slug() {
        super(59);
        counter = -1;
        regen = 3;
        presetInfo(this::setInfo);
    }
    
    @Override
    public void refreshStats() {
        regen = 2 + stack;
    }

    private void setInfo(String[] s) {
        if (counter == -2) {
            s[0] = createInfo(DESCRIPTIONS[1], regen);
        } else {
            s[0] = createInfo(DESCRIPTIONS[2]);
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
