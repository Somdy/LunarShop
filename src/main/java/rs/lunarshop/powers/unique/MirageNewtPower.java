package rs.lunarshop.powers.unique;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lunarshop.actions.common.BanCardAction;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.interfaces.powers.ArmorModifierPower;
import rs.lunarshop.npcs.monsters.LunarSnecko;
import rs.lunarshop.subjects.AbstractLunarPower;

public class MirageNewtPower extends AbstractLunarPower implements ArmorModifierPower {
    public static final String POWER_ID = LunarMod.Prefix("MirageNewtPower");
    private final boolean nerf;
    private final boolean isNewt;
    private final int cardsPerTurn;
    private final int banTurn;
    private final int hpLossPerTurn;
    private int cardsPlayed;
    private int hpLost;
    private boolean enraged;
    
    public MirageNewtPower(AbstractCreature owner, boolean nerf) {
        super(POWER_ID, "mirage_newt", ExtraPowerType.SPECIAL, owner);
        setValues(-1);
        this.nerf = nerf;
        isNewt = owner instanceof LunarSnecko;
        cardsPerTurn = nerf ? 20 : 14;
        banTurn = nerf ? 2 : 4;
        hpLossPerTurn = nerf ? MathUtils.ceil(owner.maxHealth * 0.34F) : MathUtils.ceil(owner.maxHealth * 0.25F);
        enraged = false;
        updateDescription();
    }
    
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!owner.isDeadOrEscaped()) {
            flash();
            cardsPlayed = 0;
            hpLost = 0;
            amount = 0;
            extraAmt = 0;
        }
    }
    
    @Override
    public void atStartOfTurn() {
        if (cardsPlayed >= cardsPerTurn * 2) {
            addToBot(new GainBlockAction(owner, cpr(), nerf ? cardsPlayed * 2 : cardsPlayed * 4));
        }
        if (hpLost >= MathUtils.floor(hpLossPerTurn * 2.5F)) {
            int biteAmt = cardsPlayed;
            if (owner instanceof LunarSnecko)
                ((LunarSnecko) owner).useSevereBite(biteAmt);
        }
    }
    
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        cardsPlayed++;
        if (cardsPlayed > cardsPerTurn) {
            int excess = cardsPlayed - cardsPerTurn;
            amount = excess;
            int bans = banTurn + excess / 2;
            flash();
            addToBot(new BanCardAction(bans, card, DESCRIPTIONS[0]));
            if (cardsPlayed > MathUtils.ceil(cardsPerTurn * 1.5F)) {
                addToBot(new QuickAction(() -> {
                    card.baseDamage /= 2;
                    card.damage = card.baseDamage;
                    card.baseBlock /= 2;
                    card.block = card.baseBlock;
                    card.magicNumber /= 2;
                    card.baseMagicNumber = card.magicNumber;
                    card.applyPowers();
                }));
            }
        }
    }
    
    @Override
    public int onLoseHp(int damageAmount) {
        hpLost += damageAmount;
        if (hpLost > hpLossPerTurn) {
            int excess = hpLost - hpLossPerTurn;
            extraAmt = excess;
            if (hpLost > MathUtils.ceil(hpLossPerTurn * 1.25F)) {
                flash();
                if (owner instanceof LunarSnecko)
                    ((LunarSnecko) owner).useInstantEvolve();
            }
        }
        return super.onLoseHp(damageAmount);
    }
    
    @Override
    public int modifyArmor(int origin) {
        if (amount > 0)
            origin += amount;
        if (extraAmt > 0)
            origin += extraAmt;
        return ArmorModifierPower.super.modifyArmor(origin);
    }
    
    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (!isNewt) {
            log("Only newt can have this power");
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
    
    public void setEnraged() {
        enraged = true;
        updateDescription();
    }
    
    @Override
    public String preSetDescription() {
        String msg;
        if (nerf) {
            msg = DESCRIPTIONS[2];
        } else {
            msg = enraged ? DESCRIPTIONS[1] :  DESCRIPTIONS[0];
        }
        return msg;
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new MirageNewtPower(owner, nerf);
    }
}