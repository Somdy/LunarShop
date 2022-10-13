package rs.lunarshop.utils;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.abstracts.lunarprops.LunarNpcProp;

import java.util.HashMap;
import java.util.Map;

public class NpcHelper {
    private static final Map<String, LunarNpcProp> NPC_PROP_MAP = new HashMap<>();
    
    public static void AddNpcProp(@NotNull LunarNpcProp prop) {
        String ID = prop.ID;
        if (NPC_PROP_MAP.containsKey(ID)) 
            LunarMod.LogInfo("Replacing npc [" + ID + "] properties");
        NPC_PROP_MAP.put(ID, prop);
    }
    
    @Nullable
    public static LunarNpcProp GetProp(String ID) {
        return NPC_PROP_MAP.get(ID);
    }
    
    public static void AddMonsters(AbstractMonster... monsters) {
        if (preCheckMap() && preCheckRoom() && monsters != null) {
            for (AbstractMonster monster : monsters) {
                int index = 0;
                for (AbstractMonster mo : currRoom().monsters.monsters) {
                    if (!mo.isDeadOrEscaped() && mo.drawX < monster.drawX)
                        index++;
                }
                currRoom().monsters.addMonster(index, monster);
            }
        }
    }
    
    public static void MakeMonsters(AbstractMonster... monsters) {
        if (preCheckMap() && preCheckRoom() && monsters != null) {
            currRoom().monsters = new MonsterGroup(monsters);
        }
    }
    
    public static boolean InCombat() {
        return preCheckMap() && preCheckRoom() && currRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }
    
    static boolean preCheckMap() {
        return AbstractDungeon.getCurrMapNode() != null;
    }
    
    static boolean preCheckRoom() {
        return currRoom() != null;
    }
    
    static AbstractRoom currRoom() {
        return AbstractDungeon.getCurrRoom();
    }
}