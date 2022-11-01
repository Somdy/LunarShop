package rs.lunarshop.cards.lunar.scepters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import rs.lunarshop.abstracts.AbstractLunarCard;

public class ASUZap extends AbstractLunarCard {
    public ASUZap() {
        super(4, CardTarget.SELF);
        useCustomBg("vanilla_blue");
    }
    
    @Override
    public void play(AbstractCreature s, AbstractCreature t) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ChannelAction(new Lightning()));
        }
        atbTmpAction(() -> {
            if (cpr().orbs.stream().anyMatch(o -> o instanceof Lightning)) {
                cpr().orbs.stream().filter(o -> o instanceof Lightning)
                        .forEach(o -> {
                            int baseAmt = ReflectionHacks.getPrivate(o, AbstractOrb.class, "basePassiveAmount");
                            o.passiveAmount += getEM();
                            baseAmt += getEM();
                            ReflectionHacks.setPrivate(o, AbstractOrb.class, "basePassiveAmount", baseAmt);
                        });
            }
        });
    }
}