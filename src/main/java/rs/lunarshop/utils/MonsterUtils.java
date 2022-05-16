package rs.lunarshop.utils;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MonsterUtils {
    
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