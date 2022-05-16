package rs.lunarshop.subjects.lunarprops;

import com.megacrit.cardcrawl.helpers.Hitbox;
import rs.lunarshop.enums.NpcTier;

public class LunarNpcData {
    public final String ID;
    public final NpcTier tier;
    private final Hitbox hb;
    private final int[] hp;
    
    private LunarNpcData(String ID, NpcTier tier, Hitbox hb, int[] hp) {
        this.ID = ID;
        this.tier = tier;
        this.hb = hb;
        this.hp = hp;
    }
    
    public LunarNpcData(String ID, NpcTier tier, Hitbox hb, int minHp, int maxHp) {
        this(ID, tier, hb, new int[] {minHp, maxHp});
    }
    
    public float height() {
        return hb.height;
    }
    
    public float width() {
        return hb.width;
    }
    
    public int minHp() {
        return hp[0];
    }
    
    public int maxHp() {
        return hp[1];
    }
}