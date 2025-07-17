package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;
import org.aeribmm.background.BackgroundManager;
import org.aeribmm.parser.TextLoader;
import org.aeribmm.soundManager.AudioInitializer;
import org.aeribmm.soundManager.AudioManager;
import org.aeribmm.text.*;
import org.aeribmm.text.FocusManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScene extends MenuScreen implements KeyListener, AdvanceListener {
    // Компоненты
    private TextBoxUI textBoxUI;
    private TextAnimator textAnimator;
    private FastForwardController fastForward;
    private FocusManager focusManager;
    private TextLoader textLoader;
    private BackgroundManager backgroundManager;

    // ✅ ДОБАВЛЕНО: Сохраняем ссылку на BackgroundPanel
    private BackgroundPanel backgroundPanel;

    @Override
    public void createMenu() {
        // ✅ ИСПРАВЛЕНИЕ: Создаем BackgroundPanel и сохраняем ссылку
        backgroundPanel = new BackgroundPanel("backgrounds/bg-corridor.png");
        panel = backgroundPanel;
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.addKeyListener(this);

        System.out.println("=== ИНИЦИАЛИЗАЦИЯ GAMESCENE ===");

        AudioInitializer.loadPianoSceneAudio();

        // ✅ КРИТИЧЕСКОЕ ИСПРАВЛЕНИЕ: Инициализируем BackgroundManager!
        backgroundManager = new BackgroundManager();
        backgroundManager.setCurrentBackgroundPanel(backgroundPanel);
        System.out.println("✅ BackgroundManager инициализирован и связан с панелью");

        // Инициализируем компоненты
        initializeComponents();
        setupEventHandlers();

        // Создаем TextLoader, но НЕ запускаем его
        textLoader = new TextLoader(this);
        textLoader.loadTextFile("story.txt");

        System.out.println("=== GAMESCENE ГОТОВ К РАБОТЕ ===");
    }

    public void changeBackground(String backgroundName) {
        System.out.println("=== СМЕНА ФОНА ===");
        System.out.println("Запрос смены фона на: " + backgroundName);
        System.out.println("BackgroundManager: " + (backgroundManager != null ? "ОК" : "NULL"));

        if (backgroundManager != null) {
            // Показываем доступные фоны для отладки
            String[] available = backgroundManager.getLoadedBackgrounds();
            System.out.println("Доступные фоны: " + String.join(", ", available));

            boolean success = backgroundManager.changeBackground(backgroundName);
            System.out.println("Результат смены фона: " + (success ? "УСПЕХ" : "ОШИБКА"));

            if (!success) {
                System.err.println("❌ Не удалось сменить фон на: " + backgroundName);
            } else {
                System.out.println("✅ Фон успешно изменен на: " + backgroundName);
            }
        } else {
            System.err.println("❌ КРИТИЧЕСКАЯ ОШИБКА: BackgroundManager = null!");
        }
    }

    /**
     * Для анимированной смены фона
     */
    public void changeBackgroundAnimated(String backgroundName) {
        System.out.println("=== АНИМИРОВАННАЯ СМЕНА ФОНА ===");
        System.out.println("Запрос анимированной смены фона на: " + backgroundName);

        if (backgroundManager != null) {
            boolean success = backgroundManager.changeBackground(backgroundName, true);
            System.out.println("Результат анимированной смены: " + (success ? "УСПЕХ" : "ОШИБКА"));

            if (!success) {
                System.err.println("❌ Не удалось анимированно сменить фон на: " + backgroundName);
            } else {
                System.out.println("✅ Фон анимированно изменен на: " + backgroundName);
            }
        } else {
            System.err.println("❌ КРИТИЧЕСКАЯ ОШИБКА: BackgroundManager = null!");
        }
    }

    /**
     * Получить BackgroundManager (для парсера)
     */
    public BackgroundManager getBackgroundManager() {
        return backgroundManager;
    }

    private void initializeComponents() {
        System.out.println("Инициализация UI компонентов...");

        // Создаем компоненты
        textBoxUI = new TextBoxUI();
        textAnimator = new TextAnimator(textBoxUI);
        fastForward = new FastForwardController(textAnimator);
        focusManager = new FocusManager(panel);

        // ВАЖНО: Устанавливаем связи между компонентами
        fastForward.setAdvanceListener(this); // GameScene обрабатывает автопродвижение
        textAnimator.setFastForwardController(fastForward); // TextAnimator знает о быстрой перемотке

        // Настраиваем UI
        textBoxUI.createTextBox();
        panel.add(textBoxUI.getTextBox(), BorderLayout.SOUTH);

        System.out.println("✅ UI компоненты инициализированы");
    }

    private void setupEventHandlers() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                focusManager.restoreFocus();

                if (e.getButton() == MouseEvent.BUTTON1) {
                    handleContinue();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    textBoxUI.toggleVisibility();
                }
            }
        });
    }

    // ============ ОБРАБОТКА ВВОДА ============
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                if (!fastForward.isCtrlPressed()) {
                    fastForward.enableFastForward();
                    // ✅ НОВОЕ: Уведомляем BackgroundManager о быстрой перемотке
                    if (backgroundManager != null) {
                        backgroundManager.setFastForwardMode(true);
                    }
                }
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                handleContinue();
                break;
            case KeyEvent.VK_ESCAPE:
                exitToMenu();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            fastForward.disableFastForward();
            // ✅ НОВОЕ: Уведомляем BackgroundManager о выключении быстрой перемотки
            if (backgroundManager != null) {
                backgroundManager.setFastForwardMode(false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // ============ РЕАЛИЗАЦИЯ AdvanceListener ============
    @Override
    public void onAdvanceNeeded() {
        System.out.println("Автопродвижение запрошено");
        handleContinue();
    }

    // ============ ОСНОВНАЯ ЛОГИКА ============
    public void showText(String text) {
        textAnimator.showText(text);
    }

    public void showCharacterText(String characterName, String text) {
        textAnimator.showCharacterText(characterName, text);
    }

    private void handleContinue() {
        System.out.println("handleContinue: isTyping=" + textAnimator.isTyping() + ", fastForward=" + fastForward.isFastForwardMode());

        focusManager.ensureFocus();

        if (textAnimator.isTyping()) {
            textAnimator.finishTyping();
            return;
        }

        // Обычная логика продолжения
        if (textLoader != null && textLoader.hasNextLine()) {
            textLoader.nextLine();
        } else {
            showText("Конец истории. Нажмите ESC для выхода в меню.");
        }
    }

    public void startStory() {
        if (textLoader != null) {
            System.out.println("=== ЗАПУСК ИСТОРИИ ===");
            textLoader.start();
        } else {
            System.err.println("❌ TextLoader = null!");
        }
    }

    private void exitToMenu() {
        cleanup();
        VisualNovelMain.getInstance().changeScreen("main");
        AudioManager.getInstance().stopBackgroundMusic();
    }

    private void cleanup() {
        fastForward.cleanup();
        focusManager.cleanup();
        textAnimator.cleanup();
    }

    // ============ МЕТОДЫ СЦЕНЫ ============
    @Override
    public void show() {
        panel.setVisible(true);
        focusManager.startFocusMonitoring();
    }

    @Override
    public void hide() {
        cleanup();
        panel.setVisible(false);
    }
}