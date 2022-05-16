package rs.lunarshop.interfaces.relics;

public interface RegenModifierRelic {
    default int modifyRegen(int origin) {
        return origin;
    }
}