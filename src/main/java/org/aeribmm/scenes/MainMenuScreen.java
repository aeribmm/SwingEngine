package org.aeribmm.scenes;

import org.aeribmm.BackgroundPanel;
import org.aeribmm.VisualNovelMain;

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


        // Добавляем отступ сверху
        panel.add(Box.createVerticalGlue());

        // Создаем кнопки
        startButton = createMenuButton("Новая игра");
        loadButton = createMenuButton("Загрузить");
        settingsButton = createMenuButton("Настройки");
        exitButton = createMenuButton("Выход");

        panel.add(startButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loadButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(settingsButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(exitButton);

        // Добавляем отступ снизу
        panel.add(Box.createVerticalGlue());

        // Добавляем обработчики событий
        setupEventHandlers();
    }

    public JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));

        // Стилизация кнопки
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        button.setFont(new Font("Arial", Font.BOLD, 16));

        // Эффект при наведении
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

