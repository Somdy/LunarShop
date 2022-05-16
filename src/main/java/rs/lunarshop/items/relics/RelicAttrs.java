package rs.lunarshop.items.relics;

import javafx.util.Pair;
import rs.lunarshop.items.relics.lunar.HealingRack;
import rs.lunarshop.subjects.lunarprops.LunarItemID;
import org.jetbrains.annotations.NotNull;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.enums.LunarRarity;

import java.util.HashMap;

import static rs.lunarshop.data.ItemID.*;
import static com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;

public final class RelicAttrs {
    private static final HashMap<String, Pair<LandingSound, LunarRarity>> attrs = new HashMap<>();
    
    public static void Initialize() {
        create(Fealty, LandingSound.MAGICAL, LunarRarity.COMMON);
        create(Crown, LandingSound.CLINK, LunarRarity.COMMON);
        create(Corpsebloom, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(LessBossHp, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(DrownedGesture, LandingSound.SOLID, LunarRarity.COMMON);
        create(PowerRachis, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(Purity, LandingSound.HEAVY, LunarRarity.LEGEND);
        create(SharpedGlass, LandingSound.CLINK, LunarRarity.LEGEND);
        create(Beetle, LandingSound.FLAT, LunarRarity.RARE);
        create(Clover, LandingSound.MAGICAL, LunarRarity.RARE);
        create(Gouge, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(JumpHigher, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(Horn, LandingSound.CLINK, LunarRarity.UNCOMMON);
        create(ReduceStrength, LandingSound.HEAVY, LunarRarity.COMMON);
        create(Helfire, LandingSound.CLINK, LunarRarity.COMMON);
        create(FakeWine, LandingSound.CLINK, LunarRarity.RARE);
        create(InstantKiller, LandingSound.HEAVY, LunarRarity.UNCOMMON);
        create(DeathMark, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(LeechingSeed, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(Planula, LandingSound.SOLID, LunarRarity.RARE);
        create(Vase, LandingSound.SOLID, LunarRarity.UNCOMMON);
        create(RoseBuckler, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(Shattering, LandingSound.HEAVY, LunarRarity.RARE);
        create(ChargedDrill, LandingSound.CLINK, LunarRarity.RARE);
        create(BossBullet, LandingSound.CLINK, LunarRarity.COMMON);
        create(Microbots, LandingSound.CLINK, LunarRarity.UNCOMMON);
        create(Daisy, LandingSound.MAGICAL, LunarRarity.COMMON);
        create(Scythe, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(GhorTome, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(CrowdFunder, LandingSound.HEAVY, LunarRarity.COMMON);
        create(Infusion, LandingSound.CLINK, LunarRarity.UNCOMMON);
        create(BleederDagger, LandingSound.CLINK, LunarRarity.COMMON);
        create(Chronobauble, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(TougherTimes, LandingSound.MAGICAL, LunarRarity.COMMON);
        create(Crowbar, LandingSound.FLAT, LunarRarity.COMMON);
        create(RitualDagger, LandingSound.CLINK, LunarRarity.RARE);
        create(ArmorPlate, LandingSound.HEAVY, LunarRarity.COMMON);
        create(TheGlasses, LandingSound.CLINK, LunarRarity.COMMON);
        create(Medkit, LandingSound.FLAT, LunarRarity.COMMON);
        create(Aegis, LandingSound.HEAVY, LunarRarity.RARE);
        create(Rejuvenation, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(Spleen, LandingSound.CLINK, LunarRarity.RARE);
        create(Knurl, LandingSound.HEAVY, LunarRarity.RARE);
        create(AlienHead, LandingSound.MAGICAL, LunarRarity.RARE);
        create(Crystal, LandingSound.SOLID, LunarRarity.COMMON);
        create(Syringe, LandingSound.CLINK, LunarRarity.COMMON);
        create(HopooFeather, LandingSound.MAGICAL, LunarRarity.UNCOMMON);
        create(DioFriend, LandingSound.FLAT, LunarRarity.LEGEND);
        create(JadeElephant, LandingSound.SOLID, LunarRarity.UNCOMMON);
        create(Opus, LandingSound.FLAT, LunarRarity.UNCOMMON);
        create(Slug, LandingSound.MAGICAL, LunarRarity.COMMON);
        create(RustyKey, LandingSound.CLINK, LunarRarity.COMMON);
        create(Predatory, LandingSound.SOLID, LunarRarity.UNCOMMON);
        
        create(Pearl, LandingSound.MAGICAL, LunarRarity.UNREAL);
        create(PerfectPearl, LandingSound.MAGICAL, LunarRarity.UNREAL);
        
        create(DioConsumed, LandingSound.FLAT, LunarRarity.UNREAL);
        create(WineAffliction, LandingSound.CLINK, LunarRarity.UNREAL);
    }
    
    public static LandingSound GetSFX(String internalID) {
        if (attrs.containsKey(internalID))
            return attrs.get(internalID).getKey();
        LunarMod.LogInfo("Missing " + internalID + "'s sound");
        return LandingSound.FLAT;
    }
    
    public static LunarRarity GetRarity(String internalID) {
        if (attrs.containsKey(internalID))
            return attrs.get(internalID).getValue();
        LunarMod.LogInfo("Missing " + internalID + "'s rarity");
        return LunarRarity.UNREAL;
    }
    
    private static void create(@NotNull LunarItemID itemID, LandingSound sound, LunarRarity rarity) {
        attrs.put(itemID.internalID, new Pair<>(sound, rarity));
    }
}