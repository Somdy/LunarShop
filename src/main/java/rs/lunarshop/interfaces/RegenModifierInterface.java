package rs.lunarshop.interfaces;

public interface RegenModifierInterface {
    default int modifyRegen(int origin) {
        return origin;
    }
}