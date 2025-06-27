package org.aeribmm;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private MainMenuScene mainMenu;
    private GameScene gameScene;

    public GameWindow() {
        initializeFrame();
        setupScenes();
    }

    private void initializeFrame() {
        setTitle("Visual Novel Engine");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); // Убираем рамку окна для полного погружения
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

    public void showMainMenu() {
        cardLayout.show(mainContainer, "MAIN_MENU");
    }

    public void showGameScene() {
        gameScene.startNewGame();
        cardLayout.show(mainContainer, "GAME_SCENE");
    }
}