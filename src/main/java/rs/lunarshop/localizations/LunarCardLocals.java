package rs.lunarshop.localizations;

import org.jetbrains.annotations.NotNull;

public class LunarCardLocals {
    public String ID;
    public String NAME;
    public String DESCRIPTION;
    public String[] UPGRADE_DESC;
    public String[] MSG;
    
    @NotNull
    public static LunarCardLocals MockingLocals() {
        return new LunarCardLocals(){{
            ID = "MISSING";
            NAME = "MISSING_NAME";
            DESCRIPTION = "MISSING_DESCRIPTION";
            UPGRADE_DESC = new String[]{"MISSING_DESCRIPTION"};
        }};
    }
}