package org.aeribmm.ui;
import org.aeribmm.game.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameScene extends JPanel {
    private GameWindow parentWindow;
    private TextBox textBox;
    private GameData gameData;
    private BufferedImage backgroundImage;

    public GameScene(GameWindow parent) {
        this.parentWindow = parent;
        this.gameData = new GameData();
        loadBackgroundImage();
        setupUI();
    }

    private void loadBackgroundImage() {
        try {
            // Попробуем загрузить из ресурсов или из файла
            String[] possiblePaths = {
                    "src/main/resources/images/bg_road.png",
                    "images/bg_road.png",
                    "bg_road.png",
                    "src/main/java/org/aeribmm/images/bg_road.png"
            };

            boolean loaded = false;
            for (String path : possiblePaths) {
                try {
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        backgroundImage = ImageIO.read(imageFile);
                        System.out.println("Фон успешно загружен из: " + path);
                        loaded = true;
                        break;
                    }
                } catch (Exception e) {
                    // Пропускаем эту попытку
                }
            }

            if (!loaded) {
                System.out.println("Фон не найден, создаем запасной");
                backgroundImage = createDefaultBackground();
            }

        } catch (Exception e) {
            System.out.println("Ошибка загрузки фона: " + e.getMessage());
            backgroundImage = createDefaultBackground();
        }
    }

    private BufferedImage createDefaultBackground() {
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Создаем простой градиент как замену
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(135, 206, 235), // Светло-голубой
                0, 600, new Color(25, 25, 112)  // Темно-синий
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 800, 600);

        // Добавляем простое изображение дороги
        g2d.setColor(new Color(60, 60, 60));
        int[] roadX = {300, 350, 450, 500};
        int[] roadY = {600, 400, 400, 600};
        g2d.fillPolygon(roadX, roadY, 4);

        g2d.dispose();
        return img;
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Кнопка выхода
        JButton exitButton = new JButton("Выход");
        exitButton.setPreferredSize(new Dimension(80, 30));
        exitButton.addActionListener(e -> parentWindow.showMainMenu());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(exitButton);

        // Область для фона и персонажей
        JPanel gameArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Масштабируем изображение под размер панели
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Запасной фон
                    g.setColor(new Color(25, 25, 112));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        gameArea.setLayout(new BorderLayout());
        gameArea.setOpaque(false);
        gameArea.add(topPanel, BorderLayout.NORTH);

        add(gameArea, BorderLayout.CENTER);

        // Текстовое окно
        textBox = new TextBox(this);
        add(textBox, BorderLayout.SOUTH);
    }

    public void startNewGame() {
        gameData.resetToStart();
        textBox.showCurrentText();
    }

    public void nextText() {
        if (gameData.hasNextText()) {
            gameData.nextText();
            textBox.showCurrentText();
        } else {
            JOptionPane.showMessageDialog(this, "История завершена!");
            parentWindow.showMainMenu();
        }
    }

    public String getCurrentText() {
        return gameData.getCurrentText();
    }
}
