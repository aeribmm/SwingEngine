package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;
import org.aeribmm.parser.TextLoader;
import org.aeribmm.soundManager.AudioInitializer;
import org.aeribmm.soundManager.AudioManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScene extends MenuScreen implements KeyListener {
    private JLabel textArea;
    private JLabel characterNameLabel;
    private JPanel textBox;
    private JPanel nameBox;
    private String currentText = "";
    private boolean showingCharacterName = false;
    private boolean textBoxHidden = true;
    private TextLoader textLoader;
    //Test
    private Timer typewriterTimer;
    private String fullText = "";
    private int currentCharIndex = 0;
    private boolean isTyping = false;
    private final int TYPING_DELAY = 15;

    @Override
    public void createMenu() {
        // Основная панель с фоновым изображением
        panel = new BackgroundPanel("images/airi/bg-airi-play.png"); // Замените на игровой фон
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.addKeyListener(this);
        AudioInitializer.loadPianoSceneAudio();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Левая кнопка мыши
                    handleContinue();
                }
            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Левая кнопка
                    if (!textBoxHidden) { // Продолжаем только если текст виден
                        handleContinue();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Правая кнопка
                    toggleTextBox();
                    panel.repaint(); // Перерисовываем панель
                }
            }
        });


        // Создаем текстовое окно внизу экрана
        createTextBox();
        textLoader = new TextLoader(this);

        // Загружаем текст из файла
        textLoader.loadTextFile("story.txt");
        // Добавляем текстовое окно к основной панели
        panel.add(textBox, BorderLayout.SOUTH);

        // Тестовый текст для демонстрации
//        showText("Добро пожаловать в визуальную новеллу! Нажмите пробел или Enter для продолжения.");
        textLoader.start();
    }
    private void toggleTextBox() {
        textBoxHidden = !textBoxHidden;
        textBox.setVisible(!textBoxHidden);
        System.out.println(textBoxHidden ? "Текстовое окно скрыто" : "Текстовое окно показано");
        panel.repaint();
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
//    public void showText(String text) { working version
//        nameBox.setVisible(false);
//        showingCharacterName = false;
//        currentText = wrapText(text);
//        textArea.setText(currentText);
//        panel.repaint();
//    }

    public void showText(String text) {
        nameBox.setVisible(false);
        showingCharacterName = false;

        // Останавливаем предыдущую анимацию, если она идет
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        // Сохраняем полный текст и сбрасываем индекс
        fullText = text;
        currentCharIndex = 0;
        isTyping = true;

        // Очищаем текстовую область
        textArea.setText("");

        // Создаем таймер для постепенного появления текста
        typewriterTimer = new Timer(TYPING_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentCharIndex <= fullText.length()) {
                    // Получаем подстроку от начала до текущего индекса
                    String displayText = fullText.substring(0, currentCharIndex);

                    // Обновляем текст с HTML-оберткой
                    textArea.setText(wrapText(displayText));

                    currentCharIndex++;
                } else {
                    // Анимация завершена
                    typewriterTimer.stop();
                    isTyping = false;
                }
            }
        });

        // Запускаем анимацию
        typewriterTimer.start();
        panel.repaint();
    }
    public void finishTyping() {
        if (isTyping && typewriterTimer != null) {
            typewriterTimer.stop();
            textArea.setText(wrapText(fullText));
            isTyping = false;
            panel.repaint();
        }
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
        int panelWidth = textArea.getParent().getWidth();
        System.out.println("Ширина текстовой панели: " + panelWidth + "px");
        return "<html><body style='width: 1400'>" + text + "</body></html>";
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
        // Если текст печатается, завершаем анимацию
        if (isTyping) {
            finishTyping();
            return;
        }

        // Иначе переходим к следующей строке
        if (textLoader != null && textLoader.hasNextLine()) {
            textLoader.nextLine();
        } else if (textLoader != null) {
            showText("Конец истории. Нажмите ESC для выхода в меню.");
            VisualNovelMain.getInstance().changeScreen("main");
            AudioManager.getInstance().stopBackgroundMusic();
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
        System.out.println("trying to hide text box");
        textBox.setVisible(false);
    }
}