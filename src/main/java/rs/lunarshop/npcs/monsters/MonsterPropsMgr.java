package rs.lunarshop.npcs.monsters;

import com.megacrit.cardcrawl.monsters.exordium.*;
import rs.lunarshop.subjects.lunarprops.LunarNpcProp;

import java.util.HashMap;
import java.util.Map;

public class MonsterPropsMgr {
    private static final Map<Class<?>, LunarNpcProp> props = new HashMap<>();
    
    public static void Initialize() {
        create(AcidSlime_L.class, 2, 4, 2);
        create(AcidSlime_M.class, 1, 2, 1);
        create(ApologySlime.class, 90, 90, 90);
        create(Cultist.class, 2, 2, 1);
        create(FungiBeast.class, 0, 1, 2);
        create(GremlinFat.class, 0, 1, 0);
        create(GremlinNob.class, 6, 14, 0);
        
    }
    
    static void create(Class<?> clz, int armor, int attack, int regen) {
        LunarNpcProp prop = new LunarNpcProp(clz, armor, attack, regen);
        props.put(clz, prop);
    }
}