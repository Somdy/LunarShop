package rs.lunarshop.interfaces.powers;

public interface RegenModifierPower {
    default int modifyRegen(int origin) {
        return origin;
    }
}