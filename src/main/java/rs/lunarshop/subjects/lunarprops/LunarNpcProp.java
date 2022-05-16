package rs.lunarshop.subjects.lunarprops;

public class LunarNpcProp {
    public final Class<?> targetClz;
    private int armor;
    private int attack;
    private int regen;
    
    public LunarNpcProp(Class<?> targetClz, int armor, int attack, int regen) {
        this.targetClz = targetClz;
        this.armor = armor;
        this.attack = attack;
        this.regen = regen;
    }
    
    public Class<?> getTargetClz() {
        return targetClz;
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
}