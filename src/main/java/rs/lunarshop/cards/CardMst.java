package rs.lunarshop.cards;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.abstracts.AbstractLunarCard;
import rs.lunarshop.abstracts.lunarprops.LunarCardProp;
import rs.lunarshop.cards.lunar.scepters.ASUBash;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.items.relics.lunar.Fealty;
import rs.lunarshop.patches.card.LunarCardEnum;
import rs.lunarshop.utils.MsgHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardMst {
    private static final List<Integer> SURVIVORS = new ArrayList<>();
    private static final List<Integer> MISC = new ArrayList<>();
    
    private static final Map<LunarCardProp, AbstractLunarCard> CARD_MAP = new HashMap<>();
    private static final Map<Integer, LunarCardProp> CARD_PROP_MAP = new HashMap<>();
    
    public static void Initialize() {
        MsgHelper.PreLoad("CARDS LOADED");
        new AutoAdd(LunarMod.MOD_ID)
                .packageFilter(ASUBash.class)
                .any(AbstractLunarCard.class, (i, c) -> {
                    BaseMod.addCard(c.makeCopy());
                    addCard(c, MISC);
                });
        MsgHelper.End();
    }
    
    private static void addCard(AbstractLunarCard card, @NotNull List<Integer> pool) {
        CARD_MAP.put(card.prop, card);
        CARD_PROP_MAP.put(card.prop.lunarID, card.prop);
        pool.add(card.prop.lunarID);
        MsgHelper.Append(card.prop.lunarID);
        if (LunarMod.DevMode) UnlockTracker.unlockCard(card.cardID);
    }
    
    public static void RegisterColor() {
        BaseMod.addColor(LunarCardEnum.LUNAR_CARD_COLOR, Color.NAVY.cpy(), 
                cardui("bg_attack_512"), cardui("bg_skill_512"), cardui("bg_power_512"),
                cardui("card_cost"),
                cardui("bg_attack_1024"), cardui("bg_skill_1024"), cardui("bg_power_1024"),
                cardui("card_orb"), cardui("energy_icon"));
    }
    
    @NotNull
    private static String cardui(String filename) {
        return "LunarAssets/imgs/cardui/" + filename + ".png";
    }
}