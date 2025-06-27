package org.aeribmm;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import static javax.swing.SwingConstants.CENTER;

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
            // Загружаем изображение из папки images
            backgroundImage = ImageIO.read(new File("src/main/java/org/aeribmm/images/bg_road.png"));
            System.out.println("Фон успешно загружен!");
        } catch (Exception e) {
            System.out.println("Не удалось загрузить фон 'bg_road.png': " + e.getMessage());

            // Попробуем альтернативные пути
            try {
                backgroundImage = ImageIO.read(new File("images/bg_road.png"));
            } catch (Exception e2) {
                try {
                    backgroundImage = ImageIO.read(new File("bg_road.png"));
                } catch (Exception e3) {
                    System.out.println("Все попытки загрузки фона неудачны, создаем запасной фон");
//                    backgroundImage = createDefaultBackground();
                }
            }
        }
    }

//    private BufferedImage createDefaultBackground() {
//        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = img.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        // Создаем простой градиент как замену
//        GradientPaint gradient = new GradientPaint(
//                0, 0, new Color(135, 206, 235), // Светло-голубой
//                0, 600, new Color(25, 25, 112)  // Темно-синий
//        );
//        g2d.setPaint(gradient);
//        g2d.fillRect(0, 0, 800, 600);
//        g2d.dispose();
//
//        return img;
//    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JButton exit = new JButton("exit");
        exit.addActionListener(a -> System.exit(0));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Область для фона и персонажей с вашим фоном
        JPanel gameArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {

                    System.out.println("something goes wrong");
                }
            }
        };
        gameArea.setLayout(new BorderLayout());
        gameArea.setOpaque(false);

        // Добавляем кнопку поверх игровой области
        gameArea.add(topPanel, BorderLayout.NORTH);
        add(exit,CENTER);
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