package rs.lunarshop.items.relics.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FrailPower;
import rs.lunarshop.interfaces.relics.GetPurgeableCardRelic;
import rs.lunarshop.items.abstracts.PRVDAbstractCurse;

public class PRVDCRed extends PRVDAbstractCurse implements GetPurgeableCardRelic {
    private int count;
    
    public PRVDCRed(int curseIndex) {
        super(103, curseIndex);
        count = 0;
    }
    
    public PRVDCRed() {
        this(0);
    }
    
    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type, AbstractCreature owner, AbstractCreature target) {
        int curse = getCurseIndex();
        if (curse == 6) {
            if (owner != null && !owner.isPlayer && target.currentBlock > 0) {
                damage += damage * 0.5F;
            }
        }
        return super.atDamageReceive(damage, type, owner, target);
    }
    
    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        int curse = getCurseIndex();
        if (curse == 5) {
            addToBot(new LoseBlockAction(cpr(), cpr(), 2));
        }
        return super.onPlayerGainedBlock(blockAmount);
    }
    
    @Override
    public void onPlayerEndTurnPreDiscard() {
        int curse = getCurseIndex();
        if (curse == 4) {
            if (!cpr().hand.isEmpty()) {
                for (AbstractCard c : cpr().hand.group) {
                    if (isCardTypeOf(c, AbstractCard.CardType.CURSE) || isCardTypeOf(c, AbstractCard.CardType.STATUS)) {
                        attTmpAction(() -> cpr().hand.moveToDeck(c, true));
                    }
                }
            }
        }
    }
    
    @Override
    public void onExhaust(AbstractCard card) {
        int curse = getCurseIndex();
        if (curse == 3) {
            if (!isCardRarityOf(card, AbstractCard.CardRarity.RARE) && count < 2) {
                count++;
                AbstractCard copy = card.makeCopy();
                addToBot(new MakeTempCardInDrawPileAction(copy, 2, true, true));
            }
        }
    }
    
    @Override
    public void atBattleStart() {
        int curse = getCurseIndex();
        if (curse == 2) {
            int defenses = countSpecificCards(cpr().masterDeck, c -> c.hasTag(AbstractCard.CardTags.STARTER_DEFEND));
            int applies = 4 - defenses;
            if (applies > 0) {
                for (int i = 0; i < applies; i++) {
                    addToBot(new ApplyPowerAction(cpr(), cpr(), new FrailPower(cpr(), 1, false)));
                }
            }
        }
    }
    
    @Override
    public boolean canPurgeCard(AbstractCard card) {
        int curse = getCurseIndex();
        if (card.hasTag(AbstractCard.CardTags.STARTER_STRIKE))
            return curse != 1;
        return true;
    }
}