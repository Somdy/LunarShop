package rs.lunarshop.interfaces.relics;

public interface ArmorModifierRelic {
    default int modifyArmor(int origin) {
        return origin;
    }
}