package rs.lunarshop.abstracts;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.enums.NpcTier;
import rs.lunarshop.abstracts.lunarprops.LunarNpcData;
import rs.lunarshop.utils.LunarUtils;

public abstract class AbstractLunarMonster extends AbstractMonster implements LunarUtils {
    
    public final LunarNpcData data;
    protected MonsterStrings strings;
    protected String NAME;
    protected String[] MOVES;
    protected String[] DIALOG;
    
    private int[] damages;
    private int[] blocks;
    private int[] buffs;
    private int[] debuffs;
    
    public AbstractLunarMonster(@NotNull LunarNpcData data, float x, float y) {
        super("", data.ID, data.maxHp(), 0, 0, data.width(), data.height(), null, x, y);
        this.data = data;
        loadStrings();
        loadData();
        onInit();
    }
    
    private void loadData() {
        this.name = NAME;
        int minHp = data.minHp();
        int maxHp = data.maxHp();
        if (harderTime() && data.tier != NpcTier.BOSS) {
            minHp += Math.round(data.tier.tier * 2.25F);
            maxHp += Math.round(data.tier.tier * 2.25F);
        } else if (hardTime() && data.tier != NpcTier.BOSS) {
            minHp += Math.round(data.tier.tier * 1.25F);
            maxHp += Math.round(data.tier.tier * 1.25F);
        }
        setHp(minHp, maxHp);
    }
    
    private void loadStrings() {
        strings = CardCrawlGame.languagePack.getMonsterStrings(data.ID);
        NAME = strings.NAME;
        MOVES = strings.MOVES;
        DIALOG = strings.DIALOG;
    }
    
    protected void onInit() {
        damages = new int[6];
        blocks = new int[6];
        buffs = new int[6];
        debuffs = new int[6];
    }
    
    protected boolean hardTime() {
        return ascenLv() >= 6 + data.tier.tier;
    }
    
    protected boolean harderTime() {
        return ascenLv() >= 16 + data.tier.tier;
    }
    
    /**
     * set damages[slot] value to what you want
     * @param slot the index of damage value
     * @param base the base damage if {@link #hardTime()} and {@link #harderTime()} are both not true
     * @param hard the additional damage if {@link #hardTime()} is true while {@link #harderTime()} is not true
     * @param harder the additional damage if {@link #harderTime()} is true, this is calculated with hard value
     */
    protected void setDamage(int slot, int base, int hard, int harder) {
        damages[slot] = base + Math.round(data.tier.tier);
        if (hardTime())
            damages[slot] += hard;
        if (harderTime())
            damages[slot] += harder;
    }
    
    protected void modifyDamage(int slot, int amount) {
        damages[slot] += amount;
    }
    
    protected int getDamage(int slot) {
        return damages[slot];
    }
    
    protected void setBlock(int slot, int base, int hard, int harder) {
        blocks[slot] = base + Math.round(data.tier.tier);
        if (hardTime())
            blocks[slot] += hard;
        if (harderTime())
            blocks[slot] += harder;
    }
    
    protected void modifyBlock(int slot, int amount) {
        blocks[slot] += amount;
    }
    
    protected int getBlock(int slot) {
        return blocks[slot];
    }
    
    protected void setBuff(int slot, int base, int hard, int harder) {
        buffs[slot] = base;
        if (hardTime())
            buffs[slot] += hard;
        if (harderTime())
            buffs[slot] += harder;
    }
    
    protected void modifyBuff(int slot, int amount) {
        buffs[slot] += amount;
    }
    
    protected int getBuff(int slot) {
        return buffs[slot];
    }
    
    protected void setDebuff(int slot, int base, int hard, int harder) {
        debuffs[slot] = base;
        if (hardTime())
            debuffs[slot] += hard;
        if (harderTime())
            debuffs[slot] += harder;
    }
    
    protected int getDebuff(int slot) {
        return debuffs[slot];
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    @Override
    public void takeTurn() {
        act();
    }
    
    public abstract void act();
    
    @Override
    protected void getMove(int roll) {
        move(roll);
    }
    
    public abstract void move(int roll);
}