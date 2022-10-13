package rs.lunarshop.items.relics.special;

import rs.lunarshop.items.abstracts.SpecialRelic;

public class DioConsumed extends SpecialRelic {
    public DioConsumed() {
        super(57);
        presetInfo(s -> createInfo(s, stack));
    }
}