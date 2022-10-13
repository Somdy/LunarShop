package rs.lunarshop.interfaces;

public interface ArmorModifierInterface {
    default int modifyArmor(int origin) {
        return origin;
    }
}