package rs.lunarshop.interfaces.relics;

public interface AttackModifierRelic {
    default int modifyAttack(int origin) {
        return origin;
    }
}