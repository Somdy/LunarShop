package rs.lunarshop.items.relics.legacy;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.lunarshop.items.abstracts.LegacyRelic;
import rs.lunarshop.powers.HitMarkPower;
import rs.lunarshop.utils.ItemHelper;

import java.util.List;

public class HitList extends LegacyRelic {
    private int markTime;
    
    public HitList() {
        super(86);
        markTime = 1;
    }
    
    @Override
    public void refreshStats() {
        markTime = stack;
    }
    
    @Override
    public void atTurnStart() {
        flash();
        atbTmpAction(() -> {
            List<AbstractMonster> monsterList = getAllLivingMstrs();
            int[] moMarks = new int[monsterList.size()];
            if (moMarks.length > 0 && markTime > 0) {
                for (int i = 0; i < markTime; i++) {
                    int index = ItemHelper.GetItemRng().random(moMarks.length - 1);
                    moMarks[index]++;
                }
                for (int i = 0; i < moMarks.length; i++) {
                    if (moMarks[i] > 0) {
                        AbstractMonster m = monsterList.get(i);
                        addToTop(new ApplyPowerAction(m, cpr(), new HitMarkPower(m, moMarks[i])));
                    }
                }
            }
        });
    }
    
    @Override
    public void preModifyDamage(DamageInfo info, AbstractCreature who) {
        if (who.hasPower(HitMarkPower.POWER_ID)) {
            int markAmt = who.getPower(HitMarkPower.POWER_ID).amount;
            info.output += markAmt;
            info.isModified = info.base != info.output;
        }
    }
}