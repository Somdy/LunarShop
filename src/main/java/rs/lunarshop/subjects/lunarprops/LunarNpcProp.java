package rs.lunarshop.subjects.lunarprops;

import rs.lunarshop.core.LunarMaster;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.data.DifficultyMod;
import rs.lunarshop.ui.loadout.LoadoutManager;
import rs.lunarshop.utils.LunarUtils;

public class LunarNpcProp {
    public final String ID;
    private final int armor;
    private final int attack;
    private final int regen;
    private final float dropRate;
    private final int dropTierMin;
    private final int dropTierMax;
    
    public LunarNpcProp(String ID, int armor, int attack, int regen, float dropRate, int dropTierMin, int dropTierMax) {
        this.ID = ID;
        this.armor = armor;
        this.attack = attack;
        this.regen = regen;
        this.dropRate = dropRate;
        this.dropTierMin = dropTierMin;
        this.dropTierMax = dropTierMax;
    }
    
    public int getArmor() {
        return armor;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public int getRegen() {
        return regen;
    }
    
    public float getDropRate() {
        return dropRate;
    }
    
    public int getDropTierMin() {
        return dropTierMin;
    }
    
    public int getDropTierMax() {
        return dropTierMax;
    }
    
    public LunarNpcProp modify() {
        int level = LoadoutManager.Inst.getDifficultyLevel();
        DifficultyMod mod = LunarMaster.GetDifficultyMod(level);
        if (mod != null) {
            LunarMod.DebugLog("getting [" + level + "]-modified prop for [" + this.ID + "]");
            int armor = this.armor + mod.statsMod[0];
            int attack = this.attack + mod.statsMod[1];
            int regen = this.regen + mod.statsMod[2];
            float dropRate = this.dropRate * mod.dropMod;
            int dropTierMax = this.dropTierMax;
            if (LunarUtils.ArtifactEnabled(LoadoutManager.ARTIFACTS.SACRIFICE)) {
                attack++;
                dropRate *= 1.2F;
                if (LunarUtils.EclipseLevel() >= 6)
                    dropTierMax++;
            }
            if (LunarMaster.ProgModCount > 0) {
                int prog = LunarMaster.ProgModCount;
                armor += mod.progMod[0] * prog;
                attack += mod.progMod[1] * prog;
                regen += mod.progMod[2] * prog;
            }
            return new LunarNpcProp(this.ID, armor, attack, regen, dropRate, this.dropTierMin, dropTierMax);
        }
        return this;
    }
}