package rs.lunarshop.data;

import com.megacrit.cardcrawl.helpers.Hitbox;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.NpcTier;
import rs.lunarshop.subjects.lunarprops.LunarNpcData;

public final class NpcID {
    // Enemies
    public static final LunarNpcData LunarSnecko = make("Snecko_E", NpcTier.BOSS, 10000, 310F, 305F);
    
    // Friendly
    public static final LunarNpcData MerchSnecko = make("Snecko_F", NpcTier.MERCHANT, 50000, 310F, 305F);
    
    private static LunarNpcData make(String ID, NpcTier tier, int min, int max, float w, float h) {
        return new LunarNpcData(LunarMod.Prefix(ID), tier, new Hitbox(w, h), min, max);
    }
    
    private static LunarNpcData make(String ID, NpcTier tier, int hp, float w, float h) {
        return new LunarNpcData(LunarMod.Prefix(ID), tier, new Hitbox(w, h), hp, hp);
    }
}