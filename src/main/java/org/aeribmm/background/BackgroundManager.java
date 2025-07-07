package org.aeribmm.background;

import org.aeribmm.BackgroundPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BackgroundManager {
    private HashMap<String, BufferedImage> backgroundImages;
    private BackgroundPanel currentBackgroundPanel;
    private String currentBackgroundName = "";

    public BackgroundManager() {
        backgroundImages = new HashMap<>();
        loadDefaultBackgrounds();
    }

    /**
     * Загружает стандартные фоны при инициализации
     */
    private void loadDefaultBackgrounds() {
        // Загружаем основные фоны
        loadBackground("corridor", "backgrounds/bg-corridor.png");
        loadBackground("ceremony", "backgrounds/bg-ceremony.png");
//        loadBackground("library", "backgrounds/bg-library.png");
        loadBackground("music-room", "backgrounds/bg-music-room.png");
        loadBackground("road", "backgrounds/bg-road.png");
        loadBackground("airi-playing", "backgrounds/bg-airi-play.png");

        System.out.println("Загружено фонов: " + backgroundImages.size());
    }

    /**
     * Загружает фон в память
     */
    public boolean loadBackground(String name, String imagePath) {
        try {
            BufferedImage image = null;

            // Пробуем загрузить как ресурс
            java.net.URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                image = ImageIO.read(imageUrl);
                System.out.println("Фон загружен из ресурсов: " + name);
            } else {
                // Пробуем загрузить как файл
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    image = ImageIO.read(imageFile);
                    System.out.println("Фон загружен из файла: " + name);
                }
            }

            if (image != null) {
                backgroundImages.put(name, image);
                return true;
            } else {
                System.err.println("Не удалось загрузить фон: " + imagePath);
                return false;
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки фона " + name + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Устанавливает ссылку на текущую панель фона
     */
    public void setCurrentBackgroundPanel(BackgroundPanel panel) {
        this.currentBackgroundPanel = panel;
    }

    /**
     * Меняет фон
     */
    public boolean changeBackground(String backgroundName) {
        return changeBackground(backgroundName, false);
    }

    /**
     * Меняет фон с возможностью анимации
     */
    public boolean changeBackground(String backgroundName, boolean animate) {
        if (currentBackgroundPanel == null) {
            System.err.println("BackgroundPanel не установлен!");
            return false;
        }

        BufferedImage newBackground = backgroundImages.get(backgroundName);
        if (newBackground == null) {
            System.err.println("Фон не найден: " + backgroundName);
            return false;
        }
        if (animate) {
            animateBackgroundChange(newBackground, backgroundName);
        } else {
            setBackgroundImmediately(newBackground, backgroundName);
        }

        return true;
    }

    /**
     * Мгновенно меняет фон
     */
    private void setBackgroundImmediately(BufferedImage newBackground, String backgroundName) {
        currentBackgroundPanel.setBackgroundImage(newBackground);
        currentBackgroundName = backgroundName;
        currentBackgroundPanel.repaint();
        System.out.println("Фон изменен на: " + backgroundName);
    }

    /**
     * Анимированная смена фона (fade effect)
     */
    private void animateBackgroundChange(BufferedImage newBackground, String backgroundName) {
        System.out.println("Запуск анимации смены фона: " + backgroundName);

        // Сохраняем старый фон
        BufferedImage oldBackground = currentBackgroundPanel.getBackgroundImage();

        // Параметры анимации
        Timer fadeTimer = new Timer(16, null); // ~60 FPS (16ms)
        final float[] alpha = {1.0f}; // Прозрачность перехода (1.0 = старый фон, 0.0 = новый фон)
        final float fadeSpeed = 0.02f; // Скорость затухания (меньше = медленнее)

        ActionListener fadeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] -= fadeSpeed;

                if (alpha[0] <= 0) {
                    // Анимация завершена
                    fadeTimer.stop();
                    setBackgroundImmediately(newBackground, backgroundName);
                    System.out.println("Анимация смены фона завершена");
                } else {
                    // Создаем смешанное изображение для плавного перехода
                    BufferedImage blendedImage = createBlendedImage(oldBackground, newBackground, alpha[0]);
                    currentBackgroundPanel.setBackgroundImage(blendedImage);
                    currentBackgroundPanel.repaint();
                }
            }
        };

        fadeTimer.addActionListener(fadeAction);
        fadeTimer.start();
    }

    /**
     * Создает смешанное изображение для плавного перехода
     * @param oldImage Старое изображение
     * @param newImage Новое изображение
     * @param alpha Коэффициент смешивания (1.0 = только старое, 0.0 = только новое)
     * @return Смешанное изображение
     */
    private BufferedImage createBlendedImage(BufferedImage oldImage, BufferedImage newImage, float alpha) {
        if (oldImage == null) return newImage;
        if (newImage == null) return oldImage;

        // Получаем размеры панели
        int width = currentBackgroundPanel.getWidth();
        int height = currentBackgroundPanel.getHeight();

        // Если размеры панели еще не определены, используем размер изображений
        if (width <= 0 || height <= 0) {
            width = Math.max(oldImage.getWidth(), newImage.getWidth());
            height = Math.max(oldImage.getHeight(), newImage.getHeight());
        }

        // Создаем результирующее изображение
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();

        // Включаем антиалиасинг для лучшего качества
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем новое изображение (фон)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.drawImage(newImage, 0, 0, width, height, null);

        // Накладываем старое изображение с прозрачностью
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(oldImage, 0, 0, width, height, null);

        g2d.dispose();
        return result;
    }

    /**
     * Получить имя текущего фона
     */
    public String getCurrentBackgroundName() {
        return currentBackgroundName;
    }

    /**
     * Проверить, загружен ли фон
     */
    public boolean isBackgroundLoaded(String name) {
        return backgroundImages.containsKey(name);
    }

    /**
     * Получить список всех загруженных фонов
     */
    public String[] getLoadedBackgrounds() {
        return backgroundImages.keySet().toArray(new String[0]);
    }
}