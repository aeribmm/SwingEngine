package org.aeribmm.ui;

public enum ScreenType {
    SMALL("Маленький"),        // До 1366px
    HD("Full HD"),             // 1367-1920px
    QHD("2K/QHD"),            // 1921-2560px
    UHD_4K("4K/UHD"),         // 2561-3840px
    ULTRA_WIDE("Сверхширокий"); // 3841px+

    private final String description;

    ScreenType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}