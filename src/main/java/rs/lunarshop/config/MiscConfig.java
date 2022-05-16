package rs.lunarshop.config;

import basemod.abstracts.CustomSavable;
import basemod.abstracts.CustomSavableRaw;
import com.google.gson.JsonElement;
import rs.lunarshop.core.LunarMod;
import rs.lunarshop.items.relics.LunarPass;
import rs.lunarshop.utils.LunarUtils;

public class MiscConfig implements LunarUtils, CustomSavableRaw {
    
    @Override
    public JsonElement onSaveRaw() {
        log("Saving lunar misc things");
        SaveObject so = new SaveObject();
        so.lunarPass = LunarMod.HasPass(LunarPass.LUNAR);
        so.voidPass = LunarMod.HasPass(LunarPass.VOID);
        so.tabooPass = LunarMod.HasPass(LunarPass.TABOO);
        return CustomSavable.saveFileGson.toJsonTree(so);
    }
    
    @Override
    public void onLoadRaw(JsonElement element) {
        if (element != null) {
            log("Loading lunar misc things");
            SaveObject so = CustomSavable.saveFileGson.fromJson(element, SaveObject.class);
            if (so.lunarPass)
                LunarMod.ActivatePass(LunarPass.LUNAR);
            if (so.voidPass)
                LunarMod.ActivatePass(LunarPass.VOID);
            if (so.tabooPass)
                LunarMod.ActivatePass(LunarPass.TABOO);
        }
    }
}