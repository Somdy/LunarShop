package rs.lunarshop.subjects.lunarprops;

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
}