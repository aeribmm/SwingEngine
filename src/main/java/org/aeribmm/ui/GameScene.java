package org.aeribmm.ui;

import org.aeribmm.manager.CharacterManager;
import org.aeribmm.model.enums.position.Position;
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
    private CharacterManager characterManager;    // ДОБАВИЛИ
    private BufferedImage backgroundImage;

    // Для анимации
    private Timer animationTimer;
    private long lastFrameTime;

    public GameScene(GameWindow parent) {
        this.parentWindow = parent;
        this.gameData = new GameData();
        this.characterManager = new CharacterManager();  // СОЗДАЕМ МЕНЕДЖЕР
        loadBackgroundImage();
        setupUI();
        setupAnimationTimer();
    }

    // Настройка таймера анимации
    private void setupAnimationTimer() {
        lastFrameTime = System.nanoTime();
        animationTimer = new Timer(16, e -> {  // ~60 FPS
            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f;
            lastFrameTime = currentTime;

            characterManager.update(deltaTime);
            repaint();
        });
        animationTimer.start();
    }

    private void loadBackgroundImage() {
        try {
            String[] possiblePaths = {
                    "src/main/resources/images/background/bg_road.png",
                    "images/background/bg_road.png",
                    "bg_road.png"
            };

            boolean loaded = false;
            for (String path : possiblePaths) {
                try {
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        backgroundImage = ImageIO.read(imageFile);
                        System.out.println("Фон загружен: " + path);
                        loaded = true;
                        break;
                    }
                } catch (Exception e) {
                    // Пропускаем
                }
            }

            if (!loaded) {
                backgroundImage = createDefaultBackground();
            }

        } catch (Exception e) {
            backgroundImage = createDefaultBackground();
        }
    }

    private BufferedImage createDefaultBackground() {
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(135, 206, 235),
                0, 600, new Color(25, 25, 112)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 800, 600);

        g2d.setColor(new Color(60, 60, 60));
        int[] roadX = {300, 350, 450, 500};
        int[] roadY = {600, 400, 400, 600};
        g2d.fillPolygon(roadX, roadY, 4);

        g2d.dispose();
        return img;
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));


        JButton exitButton = new JButton("Выход");
        exitButton.setPreferredSize(new Dimension(80, 30));
        exitButton.addActionListener(e -> parentWindow.showMainMenu());

        // ДОБАВЛЯЕМ КНОПКИ ДЛЯ ТЕСТИРОВАНИЯ ПЕРСОНАЖА
        JButton testShowButton = new JButton("Показать Аири");
        testShowButton.addActionListener(e -> characterManager.testShowAiri());

        JButton testEmotionButton = new JButton("Сменить эмоцию");
        testEmotionButton.addActionListener(e -> characterManager.testChangeAiriEmotion());

        JButton testHideButton = new JButton("Скрыть");
        testHideButton.addActionListener(e -> characterManager.hideCharacter("airi"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(exitButton);
        topPanel.add(testShowButton);      // ДОБАВИЛИ
        topPanel.add(testEmotionButton);   // ДОБАВИЛИ
        topPanel.add(testHideButton);      // ДОБАВИЛИ

        JPanel gameArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Рисуем фон
                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2d.setColor(new Color(25, 25, 112));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

                // РИСУЕМ ПЕРСОНАЖЕЙ
                characterManager.draw(g2d);

                g2d.dispose();
            }
        };
        gameArea.setLayout(new BorderLayout());
        gameArea.setOpaque(false);
        gameArea.add(topPanel, BorderLayout.NORTH);

        add(gameArea, BorderLayout.CENTER);

        textBox = new TextBox(this);
        add(textBox, BorderLayout.SOUTH);
    }

    public void startNewGame() {
        characterManager.hideAllCharacters();  // Очищаем экран
        gameData.resetToStart();
        textBox.showCurrentText();

        // АВТОМАТИЧЕСКИ ПОКАЗЫВАЕМ АИРИ В НАЧАЛЕ ИГРЫ
        characterManager.showCharacter("airi", Position.CENTER, "default");
    }

    public void nextText() {
        if (gameData.hasNextText()) {
            textBox.clearText();
            gameData.nextText();
            textBox.showCurrentText();

            // ПРИМЕР: меняем эмоцию в зависимости от текста
            int textIndex = gameData.getCurrentIndex();
            if (textIndex == 2) {
                characterManager.changeEmotion("airi", "smiling");
            } else if (textIndex == 4) {
                characterManager.changeEmotion("airi", "disappointed");
            }

        } else {
            JOptionPane.showMessageDialog(this, "История завершена!");
            parentWindow.showMainMenu();
        }
    }

    public String getCurrentText() {
        return gameData.getCurrentText();
    }

    // Получить менеджер персонажей (для будущих нужд)
    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}
