package rs.lunarshop.interfaces.powers;

public interface ArmorModifierPower {
    default int modifyArmor(int origin) {
        return origin;
    }
}