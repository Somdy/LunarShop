package rs.lunarshop.ui.cmdpicker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.ui.LunarButton;
import rs.lunarshop.utils.LunarImageMst;
import rs.lunarshop.utils.LunarUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PickerScreen implements LunarUtils {
    private static final UIStrings uiStrings = LunarMod.UIStrings(LunarMod.Prefix("PickerScreen"));
    public static final String[] TEXT = uiStrings.TEXT;
    public static final int BG_W = 570;
    public static final int BG_H = 570;
    public static final int OFFSET_Y = 100;
    public static final int LINE_W = 410;
    public static final int LINE_H = 410;
    public static final int LINE_DX = 76;
    public static final int LINE_DY = 82;
    public static final float CONTAINER_OFFSET_X = 8.5F;
    public static final float CONTAINER_OFFSET_Y = 4F;
    public static final int MAX_CONTAINERS_PER_PAGE = 25;
    public static final int MAX_PER_ROW = 5;
    public static float PAGE_DIFF = 420;
    
    private final List<ItemContainer> containers;
    private final List<ItemContainer> selections;
    private final List<AbstractRelic> relics;
    private final Hitbox hb;
    private final Texture pickerBg;
    private final Texture coverBg;
    private final Texture pickerFrame;
    private final Texture innerLine;
    private LunarButton confirm;
    private LunarButton cancel;
    private Color outline;
    private Color theme;
    private Vector2 pos;
    private String title;
    private PickerCaller caller;
    private int maxPage;
    private int currPage;
    private int amount;
    private float lowestRenderLine;
    private float highestRenderLine;
    private boolean distinctItems;
    private boolean anyNumber;
    private boolean canCancel;
    private boolean anySelected;
    private boolean forPurification;
    private boolean forCommand;
    public boolean opening;
    
    public PickerScreen() {
        containers = new ArrayList<>();
        selections = new ArrayList<>();
        relics = new ArrayList<>();
        hb = new Hitbox(scale(BG_W), scale(BG_H));
        pickerBg = LunarImageMst.CE_Bg;
        coverBg = LunarImageMst.CE_CoverBg;
        pickerFrame = LunarImageMst.CE_Frame;
        innerLine = LunarImageMst.CE_InnerLine;
        confirm = new LunarButton();
        cancel = new LunarButton();
        outline = Color.WHITE.cpy();
        theme = Color.WHITE.cpy();
    }
    
    public void clear() {
        containers.clear();
        selections.clear();
        relics.clear();
        confirm = null;
        cancel = null;
    }
    
    public void close() {
        clear();
        AbstractDungeon.isScreenUp = false;
        AbstractDungeon.overlayMenu.hideBlackScreen();
        AbstractDungeon.closeCurrentScreen();
        opening = false;
        dealWithCampfire();
    }
    
    public PickerScreen distinctItems(boolean distinctItems) {
        this.distinctItems = distinctItems;
        return this;
    }
    
    public PickerScreen setColor(Color outline, Color theme) {
        this.outline = outline.cpy();
        this.theme = theme.cpy();
        return this;
    }
    
    public PickerScreen setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public void open(@NotNull PickerCaller caller, Collection<? extends AbstractRelic> relics, int amount, boolean anyNumber, 
                     boolean canCancel, boolean purify, boolean command) {
        this.caller = caller;
        this.relics.clear();
        this.relics.addAll(relics);
        this.amount = amount;
        this.anyNumber = anyNumber;
        this.canCancel = canCancel;
        forPurification = purify;
        forCommand = command;
        anySelected = false;
        selections.clear();
        position();
        distinct();
        calcPages();
        assignContainers();
        initLabels();
        initButtons();
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.previousScreen = AbstractDungeon.screen;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
        if (!AbstractDungeon.isScreenUp)
            AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen();
        opening = true;
        dealWithCampfire();
    }
    
    public void open(PickerCaller caller, Collection<? extends AbstractRelic> relics, int amount, boolean anyNumber, 
                     boolean canCancel) {
        open(caller, relics, amount, anyNumber, canCancel, false, false);
    }
    
    private void dealWithCampfire() {
        if (currRoom() instanceof RestRoom) {
            if (!CampfireUI.hidden && opening)
                CampfireUI.hidden = true;
            else if (!opening && CampfireUI.hidden) {
                if (!((RestRoom) currRoom()).campfireUI.somethingSelected
                        || currRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE)
                    CampfireUI.hidden = false;
            }
        }
    }
    
    private void initButtons() {
        if (canCancel) {
            cancel = new LunarButton(TEXT[1]);
        }
        confirm = new LunarButton(TEXT[0]);
        if (cancel != null) {
            confirm.move(pos.x - scale(LunarButton.BTN_W), pos.y - scale(BG_H / 2F));
            cancel.move(pos.x + scale(LunarButton.BTN_W), pos.y - scale(BG_H / 2F));
            cancel.btnColor = theme.cpy();
            cancel.enable();
            cancel.show();
        } else {
            confirm.move(pos.x, pos.y - scale(BG_H / 2F));
        }
        confirm.btnColor = theme.cpy();
        confirm.enable();
        confirm.show();
    }
    
    private void initLabels() {
        if (forCommand)
            title = TEXT[2];
        if (forPurification)
            title = String.format(TEXT[4], amount);
    }
    
    private void assignContainers() {
        if (!relics.isEmpty()) {
            containers.clear();
            int row = 0;
            int col = 0;
            for (int i = 0; i < relics.size(); i++) {
                float sX = hb.x + scale(LINE_DX) + scale(CONTAINER_OFFSET_X) + scale(col * ItemContainer.CONTAINER_W);
                float sY = highestRenderLine - scale(ItemContainer.CONTAINER_H * 2) - scale(CONTAINER_OFFSET_Y)
                        - scale(row * ItemContainer.CONTAINER_H);
                ItemContainer ic = ItemContainer.GetContainer(relics.get(i), sX, sY);
                containers.add(i, ic);
                col++;
                if (col % MAX_PER_ROW == 0) {
                    col = 0;
                    row++;
                }
            }
        }
    }
    
    private void position() {
        pos = new Vector2(Settings.WIDTH / 2F, Settings.HEIGHT / 2F + scale(OFFSET_Y));
        hb.move(pos.x, pos.y);
        highestRenderLine = pos.y + scale(BG_H / 2F);
        lowestRenderLine = pos.y - scale(BG_H / 2F);
        log("Top limit: " + highestRenderLine + ", bot limit: " + lowestRenderLine);
    }
    
    private void distinct() {
        if (!distinctItems) return;
        if (!relics.isEmpty()) {
            log("Processing distinction...");
            List<AbstractRelic> tmp = new ArrayList<>();
            int size = relics.size();
            for (AbstractRelic r : relics) {
                if (tmp.stream().noneMatch(re -> re.relicId.equals(r.relicId)))
                    tmp.add(r);
            }
            int distSize = tmp.size();
            relics.clear();
            relics.addAll(tmp);
            log("Removed " + (size - distSize) + " duplicated relics in total.");
        }
    }
    
    private void calcPages() {
        int relicNum = relics.size();
        boolean q = relicNum % MAX_CONTAINERS_PER_PAGE == 0;
        int least = relicNum / MAX_CONTAINERS_PER_PAGE;
        int pages = q ? least : least + 1;
        maxPage = pages - 1;
        currPage = 0;
        log("Pages are " + pages + " with " + relicNum + " relics in total");
    }
    
    public void update() {
        updateMouseInput();
        updateContainers();
        anySelected = !selections.isEmpty();
        updateHbLogic();
    }
    
    private void updateMouseInput() {
        if (maxPage <= 0) return;
        if (InputHelper.scrolledDown) {
            if (currPage >= maxPage) {
                log("已经到最后一页了，煞笔玩家还在往下滑");
                return;
            }
            currPage++;
            moveContainers(1);
        } else if (InputHelper.scrolledUp) {
            if (currPage <= 0) {
                log("都已经到第一页了，煞笔玩家还在往上滑");
                return;
            }
            currPage--;
            moveContainers(-1);
        }
    }
    
    private void moveContainers(float diff) {
        float direction = diff >= 0 ? 1 : -1F;
        float pageDiff = 5 * containers.get(0).hb.height;
        if (PAGE_DIFF != pageDiff)
            PAGE_DIFF = pageDiff;
        for (ItemContainer ic : containers) {
            ic.updateDest(0, PAGE_DIFF * direction);
        }
    }
    
    private void updateContainers() {
        if (!containers.isEmpty()) {
            for (ItemContainer ic : containers) {
                boolean updateRelic = ic.pos.y < highestRenderLine && ic.pos.y - (ic.hb.height / 2F) > lowestRenderLine;
                ic.update(updateRelic);
                if (ic.leftClicked) {
                    ic.leftClicked = false;
                    if (!selections.contains(ic)) {
                        ic.selected = true;
                        if (selections.size() >= amount) {
                            selections.get(0).selected = false;
                            selections.remove(0);
                        }
                        selections.add(ic);
                    } else {
                        selections.remove(ic);
                        ic.selected = false;
                    }
                }
            }
        }
    }
    
    private void updateHbLogic() {
        hb.update();
        boolean enableConfirm = (anyNumber && anySelected) || (selections.size() >= amount);
        if (enableConfirm)
            confirm.enable();
        else 
            confirm.disable();
        confirm.update();
        if (confirm.leftClicked) {
            confirm.leftClicked = false;
            if (!selections.isEmpty()) {
                for (ItemContainer ic : selections) {
                    caller.onSelectingItem(ic);
                }
            }
            close();
        }
        if (cancel != null) {
            cancel.update();
            if (cancel.leftClicked) {
                cancel.leftClicked = false;
                caller.onCancelSelection();
                close();
            }
        }
    }
    
    public void render(SpriteBatch sb) {
        hb.render(sb);
        sb.setColor(theme);
        sb.draw(pickerBg, hb.cX - BG_W / 2F, hb.cY - BG_H / 2F, BG_W / 2F, BG_H / 2F, BG_W, BG_H, 
                Settings.scale, Settings.scale, 
                0F, 0, 0, BG_W, BG_H, false, false);
        sb.setColor(Color.WHITE.cpy());
        for (ItemContainer ic : containers) {
            if (ic.pos.y + ic.hb.height < highestRenderLine && ic.pos.y > lowestRenderLine)
                ic.render(sb);
        }
        sb.setColor(theme);
        sb.draw(coverBg, hb.cX - BG_W / 2F, hb.cY - BG_H / 2F, BG_W / 2F, BG_H / 2F, BG_W, BG_H, 
                Settings.scale, Settings.scale, 0F, 0, 0, BG_W, BG_H, false, false);
        sb.setColor(outline);
        sb.draw(innerLine, hb.cX - LINE_W / 2F, hb.cY - LINE_H / 2F, LINE_W / 2F, LINE_H / 2F, 
                LINE_W, LINE_H, Settings.scale, Settings.scale, 0F, 0, 0, LINE_W, LINE_H, 
                false, false);
        sb.draw(pickerFrame, hb.cX - BG_W / 2F, hb.cY - BG_H / 2F, BG_W / 2F, BG_H / 2F, BG_W, BG_H, 
                Settings.scale, Settings.scale, 0F, 0, 0, BG_W, BG_H, false, false);
        sb.setColor(Color.WHITE.cpy());
        if (title != null) {
            FontHelper.renderFontCentered(sb, FontHelper.charDescFont, title, pos.x, 
                    highestRenderLine - (ItemContainer.CONTAINER_H / 2F), theme);
        }
        if (maxPage > 0) {
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, ((currPage + 1) + " / " + (maxPage + 1)), pos.x, 
                    lowestRenderLine + (ItemContainer.CONTAINER_H / 2F), theme);
        }
        if (cancel != null)
            cancel.render(sb);
        if (confirm != null)
            confirm.render(sb);
    }
}