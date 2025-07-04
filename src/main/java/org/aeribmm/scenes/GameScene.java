package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScene extends MenuScreen implements KeyListener {
    private JLabel textArea;
    private JLabel characterNameLabel;
    private JPanel textBox;
    private JPanel nameBox;
    private String currentText = "";
    private boolean showingCharacterName = false;

    @Override
    public void createMenu() {
        // Основная панель с фоновым изображением
        panel = new BackgroundPanel("backgrounds/bg-music-room.png"); // Замените на игровой фон
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.addKeyListener(this);

        // Создаем текстовое окно внизу экрана
        createTextBox();

        // Добавляем текстовое окно к основной панели
        panel.add(textBox, BorderLayout.SOUTH);

        // Тестовый текст для демонстрации
        showText("Добро пожаловать в визуальную новеллу! Нажмите пробел или Enter для продолжения.");
    }

    private void createTextBox() {
        // Главная панель для текстового окна с отступами по краям
        textBox = new JPanel();
        textBox.setLayout(new BorderLayout());
        textBox.setPreferredSize(new Dimension(0, 180));
        textBox.setOpaque(false);
        textBox.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Отступы: верх, лево, низ, право

        // Панель для имени персонажа с закругленными углами через Border
        nameBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        nameBox.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        nameBox.setPreferredSize(new Dimension(0, 40));
        nameBox.setOpaque(false);
        nameBox.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        nameBox.setVisible(false); // По умолчанию скрыто

        characterNameLabel = new JLabel();
        characterNameLabel.setForeground(Color.WHITE);
        characterNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameBox.add(characterNameLabel);

        // Основное текстовое окно с закругленными углами
        JPanel mainTextPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        mainTextPanel.setLayout(new BorderLayout());
        mainTextPanel.setOpaque(false);
        mainTextPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainTextPanel.setPreferredSize(new Dimension(0, 140));

        // Текстовая область
        textArea = new JLabel();
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));
        textArea.setVerticalAlignment(SwingConstants.TOP);
        textArea.setHorizontalAlignment(SwingConstants.LEFT);

        mainTextPanel.add(textArea, BorderLayout.CENTER);

        // Индикатор продолжения (стрелочка)
        JLabel continueIndicator = new JLabel("▼");
        continueIndicator.setForeground(Color.LIGHT_GRAY);
        continueIndicator.setFont(new Font("Arial", Font.BOLD, 14));
        continueIndicator.setHorizontalAlignment(SwingConstants.RIGHT);
        mainTextPanel.add(continueIndicator, BorderLayout.SOUTH);

        // Собираем текстовое окно
        textBox.add(nameBox, BorderLayout.NORTH);
        textBox.add(mainTextPanel, BorderLayout.CENTER);
    }

    // Показать текст без имени персонажа (авторский текст)
    public void showText(String text) {
        nameBox.setVisible(false);
        showingCharacterName = false;
        currentText = wrapText(text);
        textArea.setText(currentText);
        panel.repaint();
    }

    // Показать текст с именем персонажа (диалог)
    public void showCharacterText(String characterName, String text) {
        characterNameLabel.setText(characterName);
        nameBox.setVisible(true);
        showingCharacterName = true;
        currentText = wrapText(text);
        textArea.setText(currentText);
        panel.repaint();
    }

    // Очистить текстовое окно
    public void clearText() {
        textArea.setText("");
        nameBox.setVisible(false);
        showingCharacterName = false;
        panel.repaint();
    }

    // Автоматический перенос текста для HTML
    private String wrapText(String text) {
        return "<html><body style='width: 700px'>" + text + "</body></html>";
    }

    // Обработка нажатий клавиш для продолжения
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Здесь будет логика перехода к следующему тексту
            handleContinue();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            VisualNovelMain.getInstance().changeScreen("main");
            returnToMainMenu();
        }
    }

    private void handleContinue() {
        // Примеры текстов для демонстрации
        if (currentText.contains("Добро пожаловать")) {
            showCharacterText("Аня", "Привет! Меня зовут Аня. Как дела?");
        } else if (currentText.contains("Привет! Меня зовут Аня")) {
            showCharacterText("Максим", "О, привет Аня! У меня все отлично, спасибо!");
        } else if (currentText.contains("У меня все отлично")) {
            showText("Максим улыбнулся. Было видно, что он рад встрече.");
        } else {
            showText("Это демонстрация текстового движка. Нажмите ESC для выхода в меню.");
        }
    }

    private void returnToMainMenu() {
        // Здесь будет логика возврата в главное меню
        System.out.println("Возврат в главное меню");
        // Можно добавить вызов менеджера сцен для переключения
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Не используется
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Не используется
    }

    @Override
    public void show() {
        panel.setVisible(true);
        panel.requestFocus(); // Важно для обработки клавиш
    }

    @Override
    public void hide() {
        panel.setVisible(false);
    }
}