package rs.lunarshop.items.equipments;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import rs.lunarshop.data.ItemID;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.vfx.combat.MeteoriteRainEffect;

public class Meteorite extends LunarEquipment {
    public Meteorite() {
        super(ItemID.Meteorite, 6);
        setTargetRequired(false);
    }
    
    @Override
    protected void activate() {
        super.activate();
        addToBot(new VFXAction(new MeteoriteRainEffect(Settings.WIDTH / 2F, cpr().hb.cX, cpr().hb.cY)));
    }
}