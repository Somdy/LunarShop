package rs.lunarshop.interfaces.relics;

public interface LuckModifierRelic {
    default float modifyLuck(float origin) {
        return origin;
    }
}