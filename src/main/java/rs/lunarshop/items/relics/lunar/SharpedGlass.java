package rs.lunarshop.items.relics.lunar;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.utils.ItemHelper;

public final class SharpedGlass extends LunarRelic {
    private static boolean ReturningMaxHp;
    private static int HpLimit;
    private float damageBuff;
    
    public SharpedGlass() {
        super(7);
        ReturningMaxHp = false;
        HpLimit = 12;
        damageBuff = 2F;
        presetInfo(s -> createInfo(s, HpLimit, SciPercent(damageBuff)));
    }
    
    @Override
    public void refreshStats() {
        HpLimit = MathUtils.ceil((float) (12 * Math.pow(0.5F, stack - 1)));
        if (HpLimit <= 0) HpLimit = 1;
        damageBuff = 2F + 1F * (stack - 1);
    }
    
    @Override
    protected void onStackAmt(int amt, boolean stacking) {
        checkPlayerHealth();
    }
    
    @Override
    public void onEquip() {
        ReturningMaxHp = false;
        counter = cpr().maxHealth;
        refreshStats();
        checkPlayerHealth();
    }
    
    @Override
    public void onUnequip() {
        returnPlayerHealth();
    }
    
    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        damageAmount += damageAmount * damageBuff;
        return super.onAttackToChangeDamage(info, damageAmount);
    }
    
    private void checkPlayerHealth() {
        if (cpr().maxHealth > HpLimit || cpr().currentHealth > HpLimit) {
            cpr().decreaseMaxHealth(Math.max(cpr().maxHealth - HpLimit, 0));
            if (cpr().currentHealth > cpr().maxHealth)
                cpr().currentHealth = cpr().maxHealth;
            cpr().healthBarUpdatedEvent();
        }
    }
    
    private void returnPlayerHealth() {
        if (counter > 0) {
            int retVal = counter - cpr().maxHealth;
            ReturningMaxHp = true;
            cpr().increaseMaxHp(retVal, true);
        }
    }
    
    private static boolean Enabled() {
        return LMSK.Player().relics.stream().anyMatch(r -> r instanceof SharpedGlass);
    }
    
    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp")
    public static class PreventMaxHpChangePatch {
        @SpirePrefixPatch
        public static SpireReturn PreventChanging(AbstractCreature _inst, @ByRef int[] amt, boolean effect) {
            if (Enabled() && !ReturningMaxHp) {
                if (amt[0] + _inst.maxHealth > HpLimit)
                    amt[0] = HpLimit - _inst.maxHealth;
                if (amt[0] < 0) amt[0] = 0;
                if (amt[0] <= 0) {
                    LunarMod.PatchLog("Preventing " + _inst.name + " from increasing max health");
                    LMSK.Player().getRelic(ItemHelper.GetRelicID(7)).flash();
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }
}