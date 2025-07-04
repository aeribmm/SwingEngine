package org.aeribmm;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BackgroundPanel(String imagePath) {
        System.out.println("Попытка загрузить изображение: " + imagePath);
        System.out.println("Рабочая директория: " + System.getProperty("user.dir"));

        try {
            // Сначала пробуем загрузить как ресурс
            java.net.URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
                System.out.println("Изображение успешно загружено через ClassLoader!");
                return;
            }

            // Если не получилось, пробуем как обычный файл
            File imageFile = new File(imagePath);
            System.out.println("Файл существует: " + imageFile.exists());
            System.out.println("Абсолютный путь: " + imageFile.getAbsolutePath());

            if (imageFile.exists()) {
                backgroundImage = ImageIO.read(imageFile);
                System.out.println("Изображение успешно загружено как файл!");
            } else {
                System.err.println("ОШИБКА: Файл не найден: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("ОШИБКА при загрузке изображения: " + imagePath);
            e.printStackTrace();
        }
    }

    public BackgroundPanel(BufferedImage image) {
        this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;

            // Включаем антиалиасинг для лучшего качества
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Масштабируем изображение под размер панели
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}