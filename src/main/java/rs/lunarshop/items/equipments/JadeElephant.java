package rs.lunarshop.items.equipments;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.interfaces.relics.GainedBlockRelic;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.powers.ElephantPower;

public class JadeElephant extends LunarEquipment implements GainedBlockRelic {
    public JadeElephant() {
        super(ItemID.JadeElephant, 140);
        setTargetRequired(false);
    }
    
    @Override
    protected void activate() {
        super.activate();
        if (isProxy()) {
            addToBot(new ApplyPowerAction(cpr(), cpr(), new ElephantPower(cpr(), 100, 3)));
            startCooldown();
        }
        updateExtraTips();
    }
    
    @Override
    public void onBlockGained(AbstractCreature who, int blockAmt, float modifiedBlockAmt, boolean newBlock) {
        if (who == cpr() && modifiedBlockAmt > 0) {
            reduceCooldown(MathUtils.floor(modifiedBlockAmt));
        }
    }
}