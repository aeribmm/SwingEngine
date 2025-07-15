package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;
import org.aeribmm.ui.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenuScreen extends MenuScreen {
    private JButton startButton;
    private JButton loadButton;
    private JButton settingsButton;
    private JButton exitButton;
    private VisualNovelMain mainApp;

    public MainMenuScreen() {
    }

    public void setMainApp(VisualNovelMain mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void createMenu() {
        panel = new BackgroundPanel("backgrounds/bg-main-menu.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Добавляем адаптивный отступ сверху
        panel.add(Box.createVerticalGlue());

        // Создаем кнопки с новыми константами
        startButton = createMenuButton("Новая игра");
        loadButton = createMenuButton("Загрузить");
        settingsButton = createMenuButton("Настройки");
        exitButton = createMenuButton("Выход");

        // Добавляем кнопки с адаптивными отступами
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(UI.MEDIUM_MARGIN));
        panel.add(loadButton);
        panel.add(Box.createVerticalStrut(UI.MEDIUM_MARGIN));
        panel.add(settingsButton);
        panel.add(Box.createVerticalStrut(UI.MEDIUM_MARGIN));
        panel.add(exitButton);

        // Добавляем отступ снизу
        panel.add(Box.createVerticalGlue());

        setupEventHandlers();
    }

    public JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ НОВЫЙ КОД: Используем адаптивные размеры
        button.setPreferredSize(UI.MENU_BUTTON_SIZE);
        button.setMaximumSize(UI.MENU_BUTTON_SIZE);
        button.setMinimumSize(UI.MENU_BUTTON_SIZE);

        // Стилизация с адаптивными значениями
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, UI.BORDER_WIDTH));

        // ✅ НОВЫЙ КОД: Адаптивный шрифт
        button.setFont(new Font("Arial", Font.BOLD, UI.MEDIUM_FONT));

        // Эффект при наведении (без изменений)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.DARK_GRAY);
            }
        });

        return button;
    }

    public void setupEventHandlers() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualNovelMain.getInstance().changeScreen("game");

                // ДОБАВИТЬ ЭТУ СТРОКУ:
                // Получаем GameScene и запускаем историю
                GameScene gameScene = (GameScene) VisualNovelMain.getInstance().getMenus().get("game");
                if (gameScene != null) {
                    gameScene.startStory();
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Логика загрузки игры
                System.out.println("Загрузка игры");
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Логика открытия настроек
                System.out.println("Открытие настроек");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Выход из игры
                System.exit(0);
            }
        });
    }

    @Override
    public void show() {
        panel.setVisible(true);
    }

    @Override
    public void hide() {
        panel.setVisible(false);
    }
}

