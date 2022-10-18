package rs.lunarshop.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import rs.lazymankits.utils.LMSK;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.AchvID;
import rs.lunarshop.items.relics.RelicMst;
import rs.lunarshop.items.relics.planet.Pearl;
import rs.lunarshop.items.relics.planet.PerfectPearl;
import rs.lunarshop.abstracts.AbstractLunarRelic;
import rs.lunarshop.ui.cmdpicker.ItemContainer;
import rs.lunarshop.ui.cmdpicker.PickerCaller;
import rs.lunarshop.utils.AchvHelper;
import rs.lunarshop.utils.ItemHelper;
import rs.lunarshop.utils.LunarUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CleansingPoolEvent extends AbstractImageEvent implements LunarUtils, PickerCaller {
    public static final byte KEY = 0;
    public static final String ID = LunarMod.Prefix("CleansingPool");
    private static final EventStrings eventStrings = LunarMod.EventStrings(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final byte INTRO = 1;
    private static final byte WAITING = 2;
    private static final byte OUTER_MSG = 3;
    private static final byte ENDING = 4;
    private byte phase;
    private int intro;
    private final int lunarItemStacks;
    private final int baseLunarCount;
    private int lunarCount;
    private final boolean metBefore;
    
    public CleansingPoolEvent() {
        super(NAME, DESCRIPTIONS[0], "LunarAssets/imgs/ui/events/cleansing_pool.png");
        metBefore = EventManager.Record(KEY) > 0;
        lunarItemStacks = countLunarItemStacks();
        baseLunarCount = countLunarItems();
        init();
    }
    
    private void init() {
        if (baseLunarCount > 3) {
            lunarCount = baseLunarCount / 2;
        } else {
            lunarCount = baseLunarCount;
        }
        if (metBefore) {
            body = DESCRIPTIONS[4];
            intro = -1;
            phase = WAITING;
            imageEventText.setDialogOption(String.format(OPTIONS[3], lunarCount));
            imageEventText.setDialogOption(OPTIONS[4]);
            imageEventText.setDialogOption(OPTIONS[5]);
        } else {
            intro = 0;
            phase = INTRO;
            imageEventText.setDialogOption(OPTIONS[intro]);
        }
    }
    
    static int countLunarItems() {
        int count = 0;
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof AbstractLunarRelic && ((AbstractLunarRelic) r).canBeCleansed())
                count++;
        }
        return count;
    }
    
    static int countLunarItemStacks() {
        int count = 0;
        for (AbstractRelic r : LMSK.Player().relics) {
            if (r instanceof AbstractLunarRelic && ((AbstractLunarRelic) r).canBeCleansed())
                count += ((AbstractLunarRelic) r).getStack();
        }
        return count;
    }
    
    @Override
    protected void buttonEffect(int bp) {
        switch (phase) {
            case INTRO:
                if (intro < 2) {
                    intro++;
                    imageEventText.updateBodyText(DESCRIPTIONS[intro], DialogWord.AppearEffect.FADE_IN);
                    imageEventText.updateDialogOption(0, OPTIONS[intro]);
                    imageEventText.clearRemainingOptions();
                    break;
                }
                phase = WAITING;
                imageEventText.updateBodyText(DESCRIPTIONS[3], DialogWord.AppearEffect.FADE_IN);
                imageEventText.updateDialogOption(0, String.format(OPTIONS[3], lunarCount));
                imageEventText.updateDialogOption(1, OPTIONS[4]);
                imageEventText.updateDialogOption(2, OPTIONS[5]);
                break;
            case WAITING:
                switch (bp) {
                    case 0:
                        LunarMod.PreloadItemPicker(Color.SKY.cpy(), Color.SKY.cpy());
                        LunarMod.OpenCleansingPicker(this, lunarCount, true);
                        EventManager.DisableAllOptions(this);
                        break;
                    case 1:
                        cleanseAllLunarItems();
                        phase = OUTER_MSG;
                        constructOuterMsg();
                        break;
                    case 2:
                        openMap();
                        break;
                }
                break;
            case OUTER_MSG:
                phase = ENDING;
                imageEventText.updateBodyText(DESCRIPTIONS[MathUtils.random(8, 11)]
                        + " NL NL " + DESCRIPTIONS[MathUtils.random(12, 15)], DialogWord.AppearEffect.BUMP_IN);
                imageEventText.updateDialogOption(0, OPTIONS[7]);
                imageEventText.clearRemainingOptions();
                break;
            case ENDING:
                openMap();
                break;
        }
    }
    
    void cleanseAllLunarItems() {
        EventManager.AddRecord(KEY);
        int[] pearl = new int[lunarItemStacks];
        Arrays.fill(pearl, 0);
        for (int i = 0; i < lunarItemStacks; i++) {
            if (ItemHelper.RollLuck("CleansingPool", 0.2F)) pearl[0]++;
            else pearl[1]++;
        }
        if (pearl[0] == lunarItemStacks && pearl[1] == 0) {
            AchvHelper.UnlockAchv(AchvID.ClearlyClear);
        }
        List<AbstractRelic> removeList = copyRelicList(LMSK.Player().relics, 
                r -> r instanceof AbstractLunarRelic && ((AbstractLunarRelic) r).canBeCleansed());
        for (AbstractRelic r : removeList) {
            LMSK.Player().loseRelic(r.relicId);
        }
        if (pearl[0] > 0) {
            instantObtain(new PerfectPearl(pearl[0]));
        }
        if (pearl[1] > 0) {
            instantObtain(new Pearl(pearl[1]));
        }
    }
    
    @Override
    public void onSelectingItem(ItemContainer ic) {
        EventManager.AddRecord(KEY);
        int stack = ic.cleanseRelic();
        AbstractRelic pearl;
        if (ItemHelper.RollLuck("CleansingPool", 0.2F)) {
            pearl = RelicMst.Get(ItemHelper.GetProp(55), stack);
        } else {
            pearl = RelicMst.Get(ItemHelper.GetProp(54), stack);
        }
        instantObtain(pearl);
        if (phase != OUTER_MSG) {
            phase = OUTER_MSG;
            constructOuterMsg();
        }
    }
    
    void constructOuterMsg() {
        String msg;
        if (MathUtils.randomBoolean(0.85F)) {
            int msgIndex = MathUtils.random(16, 21);
            msg = String.format(DESCRIPTIONS[msgIndex], new Date());
        } else {
            msg = DESCRIPTIONS[MathUtils.random(5, 7)];
        }
        imageEventText.updateBodyText(msg, DialogWord.AppearEffect.GROW_IN);
        imageEventText.updateDialogOption(0, OPTIONS[6]);
        imageEventText.clearRemainingOptions();
    }
    
    @Override
    public void onCancelSelection() {
        phase = WAITING;
        imageEventText.updateBodyText(DESCRIPTIONS[3], DialogWord.AppearEffect.FADE_IN);
        imageEventText.updateDialogOption(0, String.format(OPTIONS[3], lunarCount));
        imageEventText.updateDialogOption(1, OPTIONS[4]);
        imageEventText.updateDialogOption(2, OPTIONS[5]);
        EventManager.EnableAllOptions(this);
    }
}