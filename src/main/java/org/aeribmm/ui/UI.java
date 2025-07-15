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

    // Добавьте эти константы:
    public static int BORDER_WIDTH = scale.scaleMargin(2);

    static {
        if (is4KOrHigher()) {
            BORDER_WIDTH = scale.scaleMargin(3);
        }
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
        return String.format("Screen: %dx%d, Scale: %.2fx%.2f",
                scale.getScreenSize().width, scale.getScreenSize().height,
                scale.getScaleFactorX(), scale.getScaleFactorY());
    }
}