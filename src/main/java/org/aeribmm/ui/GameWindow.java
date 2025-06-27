package org.aeribmm.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame implements KeyListener {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private MainMenuScene mainMenu;
    private GameScene gameScene;

    public GameWindow() {
        initializeFrame();
        setupScenes();
        setupKeyListener();
    }

    private void initializeFrame() {
        setTitle("Visual Novel Engine");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer);
    }

    private void setupScenes() {
        mainMenu = new MainMenuScene(this);
        gameScene = new GameScene(this);

        mainContainer.add(mainMenu, "MAIN_MENU");
        mainContainer.add(gameScene, "GAME_SCENE");

        showMainMenu();
    }

    private void setupKeyListener() {
        // Добавляем обработку клавиш для всего окна
        addKeyListener(this);
        setFocusable(true);
    }

    public void showMainMenu() {
        cardLayout.show(mainContainer, "MAIN_MENU");
        requestFocus(); // Возвращаем фокус окну
    }

    public void showGameScene() {
        gameScene.startNewGame();
        cardLayout.show(mainContainer, "GAME_SCENE");
        requestFocus(); // Возвращаем фокус окну
    }

    // Обработка клавиш
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                if (mainContainer.getComponent(0) == gameScene) {
                    showMainMenu();
                } else {
                    System.exit(0);
                }
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                // Если мы в игре, переходим к следующему тексту
                Component currentComponent = null;
                for (Component comp : mainContainer.getComponents()) {
                    if (comp.isVisible()) {
                        currentComponent = comp;
                        break;
                    }
                }
                if (currentComponent == gameScene) {
                    gameScene.nextText();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}