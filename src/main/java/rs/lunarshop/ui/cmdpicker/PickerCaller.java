package rs.lunarshop.ui.cmdpicker;

public interface PickerCaller {
    void onSelectingItem(ItemContainer ic);
    
    void onCancelSelection();
}