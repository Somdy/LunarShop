package rs.lunarshop.items.relics.lunar;

import rs.lunarshop.data.ItemID;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import rs.lunarshop.enums.LunarRarity;
import rs.lunarshop.items.abstracts.LunarRelic;
import rs.lunarshop.map.GougedRoomNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;

public class Gouge extends LunarRelic {
    private boolean againstElite;
    private float difficultyScale;
    private Map<String, Integer> saves = new HashMap<>();
    
    public Gouge() {
        super(ItemID.Gouge);
        setStackable(false);
        againstElite = false;
        difficultyScale = 1F;
    }
    
    @Override
    public void atBattleStart() {
        super.atBattleStart();
        againstElite = currRoom() instanceof MonsterRoomElite;
        if (againstElite) {
            beginPulse();
            calcDifficulty();
            spawnEnemy();
        }
    }
    
    private void spawnEnemy() {
        String key = getMonsterCombo();
        MonsterGroup mongrp = MonsterHelper.getEncounter(key);
        for (AbstractMonster m : mongrp.monsters) {
            m.drawX -= scaleX(m.hb.width);
            m.drawY += m.hb.height;
            m.hb.update();
            log("Repositioning " + m.name);
            addToTop(new SpawnMonsterAction(m, false));
            addToBot(new IncreaseMaxHpAction(m, difficultyScale / 1.5F, true));
            if (difficultyScale >= 2.5) {
                addToBot(new ApplyPowerAction(m, m, new PlatedArmorPower(m, 4)));
                addToBot(new GainBlockAction(m, m, 16));
            }
            if (difficultyScale >= 3) {
                addToBot(new ApplyPowerAction(m, m, new IntangiblePlayerPower(m, 1)));
                addToBot(new ApplyPowerAction(m, m, new RegenerateMonsterPower(m, 12)));
            }
            m.usePreBattleAction();
        }
    }
    
    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (againstElite && m.type == AbstractMonster.EnemyType.ELITE) {
            currRoom().addGoldToRewards(goldReward());
            currRoom().addCardToRewards();
            addPotionReward();
            addRelicReward();
            addLunarItemReward();
            againstElite = rollStaticLuck(0.05F * difficultyScale);
        }
    }
    
    private void calcDifficulty() {
        if (currFloor() <= 9) {
            difficultyScale = 0F;
            return;
        }
        difficultyScale = 1F;
        if (currFloor() <= 30) {
            difficultyScale += 1.75 * ((currFloor() - 9F) / (currFloor() - 5));
        } else {
            difficultyScale += 0.15 * currFloor();
        }
        if (difficultyScale > 10) difficultyScale = 10;
    }
    
    private int goldReward() {
        float baseGold = 5 + currFloor();
        if (baseGold < 10) baseGold = 10;
        int golds = MathUtils.ceil(baseGold * difficultyScale);
        if (golds > LunarRarity.RARE.golds()) golds = LunarRarity.RARE.golds();
        return golds;
    }
    
    private void addPotionReward() {
        float potionChance = 0.01F * difficultyScale + 0.05F * AbstractRoom.blizzardPotionMod;
        if (rollStaticLuck(potionChance)) {
            log("lucky to spawn a potion reward");
            currRoom().addPotionToRewards(returnRandomPotion());
        } else {
            AbstractRoom.blizzardPotionMod += potionChance * 10;
        }
    }
    
    private void addRelicReward() {
        RelicTier tier = RelicTier.COMMON;
        if (difficultyScale > 3)
            tier = rollCloverLuck(0.5F) ? RelicTier.RARE : RelicTier.UNCOMMON;
        log("lucky to spawn a relic: " + tier);
        currRoom().addRelicToRewards(tier);
    }
    
    private void addLunarItemReward() {
        float chance = 0.01F * difficultyScale;
        if (currFloor() < 35) chance /= 2F;
        if (rollCloverLuck(chance)) {
            // TODO: Add lunar rs.lunarshop.items
        }
    }
    
    @Override
    protected void activate() {
        //createTreasureEntry(getCurrMapNode(), false);
    }
    
    private void createTreasureEntry(MapRoomNode where, boolean onLoad) {
        List<MapRoomNode> validNodes = new ArrayList<>();
        for (MapRoomNode node : map.get(Math.min(where.y + 1, map.size() - 1))) {
            if (node.getRoom() == null)
                validNodes.add(node);
        }
        if (validNodes.isEmpty())
            validNodes.addAll(map.get(Math.min(where.y + 1, map.size() - 1)));
        MapRoomNode entry = validNodes.get(mapRng.random(validNodes.size() - 1));
        MapRoomNode treasure = new GougedRoomNode(entry.x, entry.y);
        map.get(treasure.y).set(treasure.x, treasure);
        boolean nextToBoss = treasure.y == map.size() - 1;
        treasure.addParent(where);
        MapEdge edge = new MapEdge(where.x, where.y, 0F, 0F, treasure.x, treasure.y,
                0F, 0F, false);
        where.addEdge(edge);
        if (nextToBoss) {
            edge = new MapEdge(treasure.x, treasure.y, 0F, 0F, 3, treasure.y + 1, 
                    0F, 0F, true);
            treasure.addEdge(edge);
        } else {
            validNodes.clear();
            for (MapRoomNode node : map.get(treasure.y + 1)) {
                if (node.getRoom() != null)
                    validNodes.add(node);
            }
            MapRoomNode nextNode = validNodes.get(mapRng.random(validNodes.size() - 1));
            validNodes.remove(nextNode);
            edge = new MapEdge(treasure.x, treasure.y, 0F, 0F, nextNode.x, nextNode.y,
                    0F, 0F, false);
            treasure.addEdge(edge);
            nextNode.addParent(treasure);
            treasure.room = new MonsterRoom();
        }
        String location = where.x + ", " + where.y;
        if (!onLoad) {
            saves.put(location, saves.getOrDefault(location, 0) + 1);
        } else {
            dungeonMapScreen.updateImage();
        }
        log("Locating treasure {}" + treasure.toString());
    }
    
    private String getMonsterCombo() {
        List<String> monsters = new ArrayList<>(monsterList);
        monsters.add(MonsterHelper.LOOTER_ENC);
        monsters.add(MonsterHelper.SMALL_SLIMES_ENC);
        monsters.add(MonsterHelper.CULTIST_ENC);
        if (currAct() >= 2) {
            monsters.remove(MonsterHelper.SMALL_SLIMES_ENC);
            monsters.add(MonsterHelper.THREE_BYRDS_ENC);
            monsters.add(MonsterHelper.SHELL_PARASITE_ENC);
            monsters.add(MonsterHelper.SPHERE_GUARDIAN_ENC);
        }
        if (currAct() >= 3) {
            monsters.remove(MonsterHelper.CULTIST_ENC);
            monsters.remove(MonsterHelper.THREE_BYRDS_ENC);
            monsters.add(MonsterHelper.TWO_ORB_WALKER_ENC);
            monsters.add(MonsterHelper.SENTRY_SPHERE_ENC);
            monsters.add(MonsterHelper.WRITHING_MASS_ENC);
        }
        if (difficultyScale >= 2) {
            monsters.remove(MonsterHelper.LOOTER_ENC);
            monsters.remove(MonsterHelper.THREE_BYRDS_ENC);
            monsters.add(MonsterHelper.THREE_CULTISTS_ENC);
            monsters.add(MonsterHelper.TANK_HEALER_ENC);
            monsters.add(MonsterHelper.PARASITE_AND_FUNGUS);
        }
        if (difficultyScale >= 2.7) {
            monsters.remove(MonsterHelper.SPHERE_GUARDIAN_ENC);
            monsters.remove(MonsterHelper.WRITHING_MASS_ENC);
            monsters.add(MonsterHelper.CULTIST_CHOSEN_ENC);
            monsters.add(MonsterHelper.CHOSEN_FLOCK_ENC);
            monsters.add(MonsterHelper.SNECKO_WITH_MYSTICS);
        }
        return getRandom(monsters, monsterRng()).get();
    }
}