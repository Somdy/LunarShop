package rs.lunarshop.localizations;

public class ArtifactLocals {
    public String ID;
    public String NAME;
    public String DESCRIPTION;
    
    public static ArtifactLocals MockingLocals() {
        ArtifactLocals locals = new ArtifactLocals();
        locals.ID = "MISSING";
        locals.NAME = "MISSING_NAME";
        locals.DESCRIPTION = "MISSING_DESCRIPTION";
        return locals;
    }
}