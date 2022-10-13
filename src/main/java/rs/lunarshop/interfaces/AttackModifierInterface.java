package rs.lunarshop.interfaces;

public interface AttackModifierInterface {
    default int modifyAttack(int origin) {
        return origin;
    }
}