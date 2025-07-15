package org.aeribmm.ui;

import java.awt.*;

public class UIScaleManager {
    private static UIScaleManager instance;
    private final Dimension screenSize;
    private final double scaleFactorX;
    private final double scaleFactorY;
    private static final int BASE_WIDTH = 1920;
    private static final int BASE_HEIGHT = 1080;

    private UIScaleManager() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        scaleFactorX = (double) screenSize.width / BASE_WIDTH;
        scaleFactorY = (double) screenSize.height / BASE_HEIGHT;

        System.out.println("Разрешение экрана: " + screenSize.width + "x" + screenSize.height);
        System.out.println("Коэффициент масштабирования: " + scaleFactorX + "x" + scaleFactorY);
    }

    public static UIScaleManager getInstance() {
        if (instance == null) {
            instance = new UIScaleManager();
        }
        return instance;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public double getScaleFactorX() {
        return scaleFactorX;
    }

    public double getScaleFactorY() {
        return scaleFactorY;
    }

    public int scaleWidth(int baseWidth) {
        return (int) Math.round(baseWidth * scaleFactorX);
    }

    public int scaleHeight(int baseHeight) {
        return (int) Math.round(baseHeight * scaleFactorY);
    }

    public Dimension scaleDimension(int baseWidth, int baseHeight) {
        return new Dimension(scaleWidth(baseWidth), scaleHeight(baseHeight));
    }

    public int scaleFontSize(int baseFontSize) {
        double fontScale = Math.min(scaleFactorX, scaleFactorY);
        return Math.max(12, (int) Math.round(baseFontSize * fontScale));
    }

    public int scaleMargin(int baseMargin) {
        return (int) Math.round(baseMargin * Math.min(scaleFactorX, scaleFactorY));
    }
}