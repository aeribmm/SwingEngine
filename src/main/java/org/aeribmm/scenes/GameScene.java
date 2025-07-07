package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;
import org.aeribmm.parser.TextLoader;
import org.aeribmm.soundManager.AudioInitializer;
import org.aeribmm.soundManager.AudioManager;
import org.aeribmm.text.TextAnimator;
import org.aeribmm.text.TextBoxUI;
import org.aeribmm.text.FastForwardController;
import org.aeribmm.text.FocusManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScene extends MenuScreen implements KeyListener, FastForwardController.AdvanceListener {
    // Компоненты
    private TextBoxUI textBoxUI;
    private TextAnimator textAnimator;
    private FastForwardController fastForward;
    private FocusManager focusManager;
    private TextLoader textLoader;

    @Override
    public void createMenu() {
        // Основная панель
        panel = new BackgroundPanel("images/airi/bg-airi-play.png");
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);
        panel.addKeyListener(this);

        AudioInitializer.loadPianoSceneAudio();

        // Инициализируем компоненты
        initializeComponents();
        setupEventHandlers();

        // Загружаем контент
        textLoader = new TextLoader(this);
        textLoader.loadTextFile("story.txt");
        textLoader.start();
    }

    private void initializeComponents() {
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

        System.out.println("Key pressed: " + e.getKeyCode() + " (" + KeyEvent.getKeyText(e.getKeyCode()) + ")");

        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                if (!fastForward.isCtrlPressed()) {
                    System.out.println("CTRL нажат впервые!");
                    fastForward.enableFastForward();
                }
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                System.out.println("Space/Enter нажат!");
                handleContinue();
                break;
            case KeyEvent.VK_ESCAPE:
                System.out.println("ESC нажат!");
                exitToMenu();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key released: " + e.getKeyCode() + " (" + KeyEvent.getKeyText(e.getKeyCode()) + ")");

        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            System.out.println("CTRL отпущен!");
            fastForward.disableFastForward();
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

        if (textLoader != null && textLoader.hasNextLine()) {
            textLoader.nextLine();
        } else {
            showText("Конец истории. Нажмите ESC для выхода в меню.");
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
