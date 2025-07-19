package org.aeribmm.ui;

import java.awt.*;

public class UI {

    private static final UIScaleManager scale = UIScaleManager.getInstance();

    // Размеры кнопок
    public static final Dimension MENU_BUTTON_SIZE = scale.scaleDimension(200, 50);
    public static final Dimension SMALL_BUTTON_SIZE = scale.scaleDimension(120, 35);
    public static final Dimension LARGE_BUTTON_SIZE = scale.scaleDimension(300, 60);

    // Размеры панелей
    public static final int TEXT_BOX_HEIGHT = scale.scaleHeight(180);
    public static final int NAME_BOX_HEIGHT = scale.scaleHeight(40);

    // Отступы
    public static final int SMALL_MARGIN = scale.scaleMargin(10);
    public static final int MEDIUM_MARGIN = scale.scaleMargin(20);
    public static final int LARGE_MARGIN = scale.scaleMargin(30);

    // Размеры шрифтов
    public static final int SMALL_FONT = scale.scaleFontSize(14);
    public static final int MEDIUM_FONT = scale.scaleFontSize(16);
    public static final int LARGE_FONT = scale.scaleFontSize(18);
    public static final int TITLE_FONT = scale.scaleFontSize(24);

    // Радиусы скругления
    public static final int SMALL_RADIUS = scale.scaleMargin(10);
    public static final int MEDIUM_RADIUS = scale.scaleMargin(15);
    public static final int LARGE_RADIUS = scale.scaleMargin(20);

    // Границы
    public static int BORDER_WIDTH = scale.scaleMargin(2);

    static {
        if (is4KOrHigher()) {
            BORDER_WIDTH = scale.scaleMargin(3);
        }
    }

    /**
     * ✅ НОВЫЙ МЕТОД: Определяет тип экрана
     */
    public static ScreenType getScreenType() {
        int width = scale.getScreenSize().width;

        if (width <= 1366) {
            return ScreenType.SMALL;
        } else if (width <= 1920) {
            return ScreenType.HD;
        } else if (width <= 2560) {
            return ScreenType.QHD;
        } else if (width <= 3840) {
            return ScreenType.UHD_4K;
        } else {
            return ScreenType.ULTRA_WIDE;
        }
    }

    /**
     * ✅ ИСПРАВЛЕННЫЙ МЕТОД: Нормальные проценты ширины для текстового окна
     */
    public static double getTextBoxWidthPercentage() {
        switch (getScreenType()) {
            case SMALL:
                return 0.95; // 95% ширины для маленьких экранов
            case HD:
                return 0.84; // 84% ширины для Full HD
            case QHD:
                return 0.76; // 76% ширины для 2K
            case UHD_4K:
                return 0.70; // 70% ширины для 4K
            case ULTRA_WIDE:
                return 0.64; // 64% ширины для сверхшироких
            default:
                return 0.84;
        }
    }

    /**
     * ✅ НОВЫЙ МЕТОД: Рассчитывает боковые отступы для текстового окна
     */
    public static int getTextBoxSideMargin() {
        int screenWidth = scale.getScreenSize().width;
        double widthPercentage = getTextBoxWidthPercentage();
        double marginPercentage = (1.0 - widthPercentage) / 2.0; // Делим поровну на левый и правый отступ

        int margin = (int)(screenWidth * marginPercentage);

        // Ограничиваем минимальными и максимальными значениями
        margin = Math.max(MEDIUM_MARGIN, margin);
        margin = Math.min(screenWidth / 4, margin);

        return margin;
    }

    /**
     * Проверяет, является ли экран высокого разрешения
     */
    public boolean isHighDPI() {
        return scale.getScreenSize().width >= 2560 || scale.getScreenSize().height >= 1440;
    }

    /**
     * Проверяет, является ли экран 4K или выше
     */
    public static boolean is4KOrHigher() {
        return scale.getScreenSize().width >= 3840 || scale.getScreenSize().height >= 2160;
    }

    /**
     * Возвращает информацию о масштабировании
     */
    public String getScaleInfo() {
        return String.format("Screen: %dx%d (%s), Scale: %.2fx%.2f, TextBox: %.0f%%",
                scale.getScreenSize().width, scale.getScreenSize().height,
                getScreenType(), scale.getScaleFactorX(), scale.getScaleFactorY(),
                getTextBoxWidthPercentage() * 100);
    }

    /**
     * ✅ НОВЫЙ ENUM: Типы экранов
     */
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
}