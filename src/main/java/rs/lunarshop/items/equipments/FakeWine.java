package rs.lunarshop.items.equipments;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.items.relics.special.WineAffliction;
import rs.lunarshop.powers.TonicPower;
import rs.lunarshop.utils.AchvHelper;

public class FakeWine extends LunarEquipment {
    public FakeWine() {
        super(15, 240);
        setTargetRequired(false);
    }
    
    public static void IncreaseLoss() {
        new WineAffliction().instantObtain();
        AchvHelper.UnlockAchv(AchvID.FakeWineHarms);
    }
    
    @Override
    protected void use() {
        super.use();
        if (isProxy()) {
            addToBot(new ApplyPowerAction(cpr(), cpr(), new TonicPower(cpr(), 3)));
            startCooldown();
        }
        updateExtraTips();
    }
    
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (canReduceCooldown() && damageAmount > 0) {
            reduceCooldown(damageAmount);
        }
    }
    
    private boolean canReduceCooldown() {
        return cpr().powers.stream().noneMatch(p -> p instanceof TonicPower);
    }
}