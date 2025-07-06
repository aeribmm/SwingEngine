package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;
import org.aeribmm.parser.TextLoader;
import org.aeribmm.soundManager.AudioInitializer;
import org.aeribmm.soundManager.AudioManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GameScene extends MenuScreen implements KeyListener {
    private JLabel textArea;
    private JLabel characterNameLabel;
    private JPanel textBox;
    private JPanel nameBox;
    private String currentText = "";
    private boolean showingCharacterName = false;
    private boolean textBoxHidden = false;
    private TextLoader textLoader;

    // Анимация текста
    private Timer typewriterTimer;
    private String fullText = "";
    private int currentCharIndex = 0;
    private boolean isTyping = false;
    private final int TYPING_DELAY = 15;
    private boolean preventAutoAdvance = false; // Флаг для предотвращения автопродвижения

    // Быстрая перемотка
    private boolean fastForwardMode = false;
    private final int FAST_TYPING_DELAY = 3;
    private final int AUTO_ADVANCE_DELAY = 50;
    private boolean ctrlPressed = false;
    private List<Timer> activeTimers = new ArrayList<>();
    private Timer focusTimer; // Таймер для контроля фокуса

    @Override
    public void createMenu() {
        // Основная панель с фоновым изображением
        panel = new BackgroundPanel("images/airi/bg-airi-play.png");
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.setRequestFocusEnabled(true);
        panel.addKeyListener(this);

        AudioInitializer.loadPianoSceneAudio();

        // Обработчик мыши
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ВСЕГДА восстанавливаем фокус при клике
                panel.requestFocusInWindow();

                if (e.getButton() == MouseEvent.BUTTON1) { // Левая кнопка
                    if (!textBoxHidden) {
                        handleContinue();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Правая кнопка
                    toggleTextBox();
                    panel.repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Дополнительная гарантия фокуса
                panel.requestFocusInWindow();
            }
        });

        // Создаем текстовое окно
        createTextBox();
        textLoader = new TextLoader(this);

        // Загружаем текст из файла
        textLoader.loadTextFile("story.txt");
        panel.add(textBox, BorderLayout.SOUTH);

        textLoader.start();
    }

    private void toggleTextBox() {
        textBoxHidden = !textBoxHidden;
        textBox.setVisible(!textBoxHidden);

        // Если показываем окно и есть сохраненный текст
        if (!textBoxHidden && !fullText.isEmpty()) {
            System.out.println("Восстанавливаем текст при показе окна");

            // ВАЖНО: Просто показываем текст без анимации и автопродвижения
            // Настройка интерфейса
            if (showingCharacterName) {
                String characterName = characterNameLabel.getText();
                characterNameLabel.setText(characterName);
                nameBox.setVisible(true);
            } else {
                nameBox.setVisible(false);
            }

            // Останавливаем любые таймеры
            if (typewriterTimer != null && typewriterTimer.isRunning()) {
                typewriterTimer.stop();
            }

            // Мгновенно показываем сохраненный текст БЕЗ автопродвижения
            textArea.setText(wrapText(fullText));
            currentCharIndex = fullText.length();
            isTyping = false;

            System.out.println("Текст восстановлен без автопродвижения");
        }
        panel.repaint();
    }

    private void createTextBox() {
        // Главная панель для текстового окна
        textBox = new JPanel();
        textBox.setLayout(new BorderLayout());
        textBox.setPreferredSize(new Dimension(0, 180));
        textBox.setOpaque(false);
        textBox.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Панель для имени персонажа
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
        nameBox.setVisible(false);

        characterNameLabel = new JLabel();
        characterNameLabel.setForeground(Color.WHITE);
        characterNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameBox.add(characterNameLabel);

        // Основное текстовое окно
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

        // Индикатор продолжения
        JLabel continueIndicator = new JLabel("▼");
        continueIndicator.setForeground(Color.LIGHT_GRAY);
        continueIndicator.setFont(new Font("Arial", Font.BOLD, 14));
        continueIndicator.setHorizontalAlignment(SwingConstants.RIGHT);
        mainTextPanel.add(continueIndicator, BorderLayout.SOUTH);

        // Собираем текстовое окно
        textBox.add(nameBox, BorderLayout.NORTH);
        textBox.add(mainTextPanel, BorderLayout.CENTER);
    }

    // ============ МЕТОДЫ АНИМАЦИИ ТЕКСТА ============

    private void animateText(String text, boolean isCharacterDialog, String characterName) {
        // Настройка интерфейса
        if (isCharacterDialog) {
            characterNameLabel.setText(characterName);
            nameBox.setVisible(true);
            showingCharacterName = true;
        } else {
            nameBox.setVisible(false);
            showingCharacterName = false;
        }

        // Останавливаем предыдущую анимацию
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        // Настройка анимации
        fullText = text;
        currentCharIndex = 0;
        isTyping = true;

        // Если быстрая перемотка активна, показываем мгновенно
        if (fastForwardMode && !preventAutoAdvance) {
            System.out.println("Быстрая перемотка активна - показываем текст мгновенно");
            finishTypingInstantly();

            // Автоматический переход к следующей строке
            if (ctrlPressed) {
                createAutoAdvanceTimer(50);
            }
            return;
        }

        // Сбрасываем флаг предотвращения автопродвижения
        preventAutoAdvance = false;

        // Обычная анимация
        textArea.setText("");
        System.out.println("Запуск обычной анимации");

        typewriterTimer = new Timer(TYPING_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Проверяем, не включилась ли быстрая перемотка
                if (fastForwardMode && !preventAutoAdvance) {
                    System.out.println("Быстрая перемотка включилась во время анимации");
                    typewriterTimer.stop();
                    finishTypingInstantly();

                    if (ctrlPressed) {
                        createAutoAdvanceTimer(50);
                    }
                    return;
                }

                if (currentCharIndex <= fullText.length()) {
                    String displayText = fullText.substring(0, currentCharIndex);
                    textArea.setText(wrapText(displayText));
                    currentCharIndex++;
                } else {
                    typewriterTimer.stop();
                    isTyping = false;
                    System.out.println("Обычная анимация завершена");
                }
            }
        });

        typewriterTimer.start();
        panel.repaint();
    }

    public void showText(String text) {
        animateText(text, false, "");
    }

    public void showCharacterText(String characterName, String text) {
        animateText(text, true, characterName);
    }

    private void finishTypingInstantly() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        if (!fullText.isEmpty()) {
            textArea.setText(wrapText(fullText));
            currentCharIndex = fullText.length();
            isTyping = false;
            panel.repaint();
            System.out.println("Текст показан мгновенно");
        }
    }

    public void finishTyping() {
        if (isTyping && typewriterTimer != null) {
            typewriterTimer.stop();
            textArea.setText(wrapText(fullText));
            isTyping = false;
            panel.repaint();
            System.out.println("Анимация принудительно завершена");

            // В режиме быстрой перемотки сразу переходим дальше
            if (fastForwardMode && ctrlPressed) {
                createAutoAdvanceTimer(AUTO_ADVANCE_DELAY);
            }
        }
    }

    // ============ МЕТОДЫ БЫСТРОЙ ПЕРЕМОТКИ ============

    private void enableFastForward() {
        System.out.println("=== ВКЛЮЧЕНИЕ БЫСТРОЙ ПЕРЕМОТКИ ===");
        System.out.println("Текущий режим: " + fastForwardMode + ", isTyping: " + isTyping);

        if (!fastForwardMode) {
            fastForwardMode = true;
            System.out.println("✅ Быстрая перемотка ВКЛЮЧЕНА");

            // Останавливаем все таймеры
            stopAllAutoAdvanceTimers();

            // Если текст печатается, завершаем мгновенно
            if (isTyping) {
                System.out.println("Принудительно завершаем анимацию текста");
                finishTypingInstantly();

                // Создаем таймер для перехода к следующей строке
                System.out.println("Создаем таймер автопродвижения");
                createAutoAdvanceTimer(100);
            } else {
                System.out.println("Текст не печатается, сразу переходим к следующей строке");
                // Если текст уже показан, сразу переходим дальше
                createAutoAdvanceTimer(50);
            }
        } else {
            System.out.println("Быстрая перемотка уже включена, пропускаем");
        }
    }

    private void disableFastForward() {
        System.out.println("=== ВЫКЛЮЧЕНИЕ БЫСТРОЙ ПЕРЕМОТКИ ===");

        if (fastForwardMode) {
            fastForwardMode = false;
            System.out.println("✅ Быстрая перемотка ВЫКЛЮЧЕНА");

            // Останавливаем все таймеры автопродвижения
            stopAllAutoAdvanceTimers();

            // ВАЖНО: Восстанавливаем фокус на панели
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    panel.requestFocusInWindow();
                    System.out.println("Фокус восстановлен после выключения быстрой перемотки");
                }
            });

            System.out.println("=== БЫСТРАЯ ПЕРЕМОТКА ПОЛНОСТЬЮ ОТКЛЮЧЕНА ===");
        }
    }

    // ============ УПРАВЛЕНИЕ ТАЙМЕРАМИ ============

    private void createAutoAdvanceTimer(int delay) {
        System.out.println("Создание таймера автопродвижения с задержкой: " + delay + "мс");

        Timer autoAdvanceTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Таймер сработал! ctrlPressed: " + ctrlPressed + ", fastForward: " + fastForwardMode);

                if (ctrlPressed && fastForwardMode) {
                    System.out.println("Выполняем автопродвижение");
                    handleContinue();
                } else {
                    System.out.println("Условия не выполнены, автопродвижение отменено");
                }

                // Удаляем таймер из списка
                activeTimers.remove((Timer) e.getSource());
                System.out.println("Таймер удален из списка, осталось: " + activeTimers.size());
            }
        });
        autoAdvanceTimer.setRepeats(false);

        // Добавляем в список для отслеживания
        activeTimers.add(autoAdvanceTimer);
        System.out.println("Таймер добавлен в список, всего: " + activeTimers.size());
        autoAdvanceTimer.start();
        System.out.println("Таймер запущен");
    }

    private void stopAllAutoAdvanceTimers() {
        System.out.println("Останавливаем все таймеры: " + activeTimers.size());

        for (Timer timer : new ArrayList<>(activeTimers)) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
        }
        activeTimers.clear();
        System.out.println("Все таймеры остановлены");
    }

    // ============ ОБРАБОТКА ВВОДА ============

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode() + " (" + KeyEvent.getKeyText(e.getKeyCode()) + ")");

        if (e.getKeyCode() == KeyEvent.VK_CONTROL && !ctrlPressed) { // ВАЖНО: только если Ctrl еще не был нажат
            System.out.println("CTRL нажат впервые!");
            ctrlPressed = true;
            enableFastForward();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Space/Enter нажат!");
            handleContinue();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESC нажат!");
            // Останавливаем все при выходе
            stopAllAutoAdvanceTimers();
            if (fastForwardMode) {
                disableFastForward();
            }
            VisualNovelMain.getInstance().changeScreen("main");
            returnToMainMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key released: " + e.getKeyCode() + " (" + KeyEvent.getKeyText(e.getKeyCode()) + ")");

        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            System.out.println("CTRL отпущен!");
            ctrlPressed = false;
            disableFastForward();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Не используется
    }

    // ============ ОСНОВНАЯ ЛОГИКА ============

    private void handleContinue() {
        System.out.println("handleContinue: isTyping=" + isTyping + ", fastForward=" + fastForwardMode);

        // ВАЖНО: Всегда восстанавливаем фокус при обработке продолжения
        if (!panel.hasFocus()) {
            panel.requestFocusInWindow();
            System.out.println("Фокус восстановлен в handleContinue");
        }

        // Если текст печатается, завершаем анимацию
        if (isTyping) {
            finishTyping();
            return;
        }

        // Переходим к следующей строке
        if (textLoader != null && textLoader.hasNextLine()) {
            textLoader.nextLine();
        } else if (textLoader != null) {
            showText("Конец истории. Нажмите ESC для выхода в меню.");
            if (!fastForwardMode) {
                VisualNovelMain.getInstance().changeScreen("main");
                AudioManager.getInstance().stopBackgroundMusic();
            }
        }
    }

    // ============ УТИЛИТЫ ============

    public void clearText() {
        textArea.setText("");
        nameBox.setVisible(false);
        showingCharacterName = false;
        panel.repaint();
    }

    private String wrapText(String text) {
        return "<html><body style='width: 1100px'>" + text + "</body></html>";
    }

    private void returnToMainMenu() {
        System.out.println("Возврат в главное меню");
    }

    // Метод для принудительного восстановления фокуса
    public void ensureFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!panel.hasFocus()) {
                    panel.requestFocusInWindow();
                    System.out.println("Фокус принудительно восстановлен");
                }
            }
        });
    }

    // ============ МЕТОДЫ СЦЕНЫ ============

    @Override
    public void show() {
        panel.setVisible(true);

        // Устанавливаем фокус для обработки клавиш
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.requestFocusInWindow();
                System.out.println("Фокус установлен на панель");
            }
        });

        // Запускаем таймер для контроля фокуса
        startFocusTimer();
    }

    @Override
    public void hide() {
        System.out.println("Скрытие GameScene");

        // Останавливаем таймер фокуса
        if (focusTimer != null) {
            focusTimer.stop();
            focusTimer = null;
        }

        // Останавливаем все таймеры при скрытии
        stopAllAutoAdvanceTimers();
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        // Сбрасываем состояние быстрой перемотки
        if (fastForwardMode) {
            fastForwardMode = false;
            ctrlPressed = false;
        }

        textBox.setVisible(false);
    }

    // Запуск таймера контроля фокуса
    private void startFocusTimer() {
        focusTimer = new Timer(1000, new ActionListener() { // Проверяем каждую секунду
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!panel.hasFocus()) {
                    System.out.println("Фокус потерян, восстанавливаем...");
                    panel.requestFocusInWindow();
                }
            }
        });
        focusTimer.start();
    }
}