package rs.lunarshop.interfaces.powers;

public interface AttackModifierPower {
    default int modifyAttack(int origin) {
        return origin;
    }
}