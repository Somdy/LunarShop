package rs.lunarshop.cards.temp;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;

public class NoEffectCopyCard extends CustomCard {
    private final AbstractCard source;
    
    public NoEffectCopyCard(@NotNull AbstractCard source) {
        super(source.cardID, source.name, "LunarAssets/imgs/items/cards/lunar/1.png", source.cost, source.rawDescription, 
                source.type, source.color, source.rarity, source.target);
        this.source = source;
        copyValues();
    }
    
    public void copyValues() {
        portrait = source.portrait;
        jokePortrait = source.portrait;
        baseDamage = damage = source.baseDamage;
        baseBlock = block = source.baseBlock;
        baseMagicNumber = magicNumber = source.baseMagicNumber;
        misc = source.misc;
        cost = source.cost;
        costForTurn = source.costForTurn;
        type = source.type;
        color = source.color;
        rarity = source.rarity;
        target = source.target;
        rawDescription = source.rawDescription;
        name = source.name;
        initializeDescription();
        initializeTitle();
    }
    
    @Override
    public void upgrade() {
        if (source.canUpgrade()) {
            source.upgrade();
            copyValues();
        }
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}
    
    @Override
    public void triggerOnGlowCheck() {
        glowColor = source.glowColor;
    }
    
    @Override
    public AbstractCard makeCopy() {
        return new NoEffectCopyCard(source);
    }
}