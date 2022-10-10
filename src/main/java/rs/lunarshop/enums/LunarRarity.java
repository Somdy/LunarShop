package rs.lunarshop.enums;

import com.badlogic.gdx.math.MathUtils;

import static rs.lunarshop.patches.relic.RenderRelicRarityPatches.TEXT;

public enum LunarRarity {
    COMMON(1, 3, 1F, 0F, 0F),
    UNCOMMON(2, 5, 0.98F, 0F, 0F),
    RARE(3, 9, 0.86F, 1F, 0.5F),
    LEGEND(4, 15, 0.82F, 1.5F, 1.25F),
    MYTHIC(5, 20, 0.76F, 2.25F, 1.55F),
    UNREAL(6, 999, 999F, 999F, 999F);
    
    private static final int GOLD_RATE = 60;
    private static final int BLOOD_RATE = 5;
    private final int tier;
    // 基于月亮币的价格
    private final int price;
    // 修正系数
    private final float coefficient;
    // 金币补差价
    private final float gold_value;
    // 血量补差价
    private final float blood_value;
    
    LunarRarity(int tier, int price, float coefficient, float gold_value, float blood_value) {
        this.tier = tier;
        this.price = price;
        this.coefficient = coefficient;
        this.gold_value = gold_value;
        this.blood_value = blood_value;
    }
    
    public boolean above(LunarRarity rarity) {
        return price > rarity.price;
    }
    
    public String locals() {
        return TEXT[tier];
    }
    
    public int tier() {
        return tier;
    }
    
    public int coins() {
        return price;
    }
    
    public int golds() {
        return MathUtils.ceil((GOLD_RATE + gold_value) * coefficient);
    }
    
    public int pureGolds() {
        return MathUtils.ceil(GOLD_RATE * coefficient);
    }
    
    public int bloods() {
        return MathUtils.ceil((BLOOD_RATE + blood_value) * coefficient);
    }
    
    public int pureBloods() {
        return MathUtils.ceil(BLOOD_RATE * coefficient);
    }
    
    @Override
    public String toString() {
        return "LunarRarity{" +
                "locals='" + TEXT[tier] + '\'' +
                ", price=" + price +
                ", coefficient=" + coefficient +
                ", gold_value=" + gold_value +
                ", blood_value=" + blood_value +
                '}';
    }
}