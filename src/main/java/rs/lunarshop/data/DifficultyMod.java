package rs.lunarshop.data;

public class DifficultyMod {
    public int level;
    public int[] statsMod;
    public float dropMod;
    public int[] progMod;
    public int[] playerStats;
    
    public static DifficultyMod MockingMod() {
        DifficultyMod mod = new DifficultyMod();
        mod.level = -1;
        mod.statsMod = new int[]{0, 0, 0};
        mod.dropMod = 1F;
        mod.progMod = new int[]{0, 0, 0};
        mod.playerStats = new int[]{10, 2, 1};
        return mod;
    }
}