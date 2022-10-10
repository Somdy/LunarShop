package rs.lunarshop.items.equipments;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import rs.lunarshop.items.abstracts.LunarEquipment;
import rs.lunarshop.vfx.combat.MeteoriteRainEffect;

@AutoAdd.Ignore
public class Meteorite extends LunarEquipment {
    public Meteorite() {
        super(16, 6);
        setTargetRequired(false);
    }
    
    @Override
    protected void use() {
        super.use();
        addToBot(new VFXAction(new MeteoriteRainEffect(Settings.WIDTH / 2F, cpr().hb.cX, cpr().hb.cY)));
    }
}