package org.aeribmm;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class VisualNovelEngine extends JFrame {
    private JPanel currentPanel;
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Игровые данные
    private ArrayList<String> gameText;
    private int currentTextIndex = 0;
    private JTextArea textArea;

    public VisualNovelEngine() {
        initializeFrame();
        setupScenes();
        setupTestData();
    }

    private void initializeFrame() {
        setTitle("Visual Novel Engine");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer);
    }

    private void setupScenes() {
        // Главное меню
        JPanel mainMenu = createMainMenu();
        mainContainer.add(mainMenu, "MAIN_MENU");

        // Игровая сцена
        JPanel gameScene = createGameScene();
        mainContainer.add(gameScene, "GAME_SCENE");

        // Показываем главное меню
        cardLayout.show(mainContainer, "MAIN_MENU");
    }

    private JPanel createMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(30, 30, 50));

        // Заголовок
        JLabel title = new JLabel("Visual Novel", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        panel.add(title, BorderLayout.NORTH);

        // Кнопки меню
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(30, 30, 50));

        JButton startButton = createMenuButton("Новая игра");
        JButton loadButton = createMenuButton("Загрузить");
        JButton settingsButton = createMenuButton("Настройки");
        JButton exitButton = createMenuButton("Выход");

        // Действия кнопок
        startButton.addActionListener(e -> {
            currentTextIndex = 0;
            updateGameText();
            cardLayout.show(mainContainer, "GAME_SCENE");
        });

        loadButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Функция загрузки пока не реализована"));

        settingsButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Настройки пока не реализованы"));

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(70, 70, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        // Эффект наведения
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 100, 150));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 70, 100));
            }
        });

        return button;
    }

    private JPanel createGameScene() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);

        // Область для фона и персонажей (пока пустая)
        JPanel gameArea = new JPanel();
        gameArea.setBackground(new Color(20, 40, 60));
        gameArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(gameArea, BorderLayout.CENTER);

        // Текстовое окно внизу
        JPanel textPanel = createTextPanel();
        panel.add(textPanel, BorderLayout.SOUTH);

        // Кнопка возврата в меню
        JButton backButton = new JButton("Меню");
        backButton.addActionListener(e -> cardLayout.show(mainContainer, "MAIN_MENU"));
        backButton.setPreferredSize(new Dimension(80, 30));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createTextPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(800, 150));
        panel.setBackground(new Color(0, 0, 0, 180));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Текстовая область
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);

        // Клик по тексту для продолжения
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nextText();
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Индикатор продолжения
        JLabel continueLabel = new JLabel("▼ Кликните для продолжения", JLabel.RIGHT);
        continueLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        continueLabel.setForeground(Color.LIGHT_GRAY);
        panel.add(continueLabel, BorderLayout.SOUTH);

        return panel;
    }

    private void setupTestData() {
        // Тестовые тексты
        gameText = new ArrayList<>(Arrays.asList(
                "Добро пожаловать в визуальную новеллу!",
                "Это первое предложение истории. Здесь будет развиваться сюжет.",
                "Каждый клик мыши будет показывать следующее предложение.",
                "Старый текст исчезает, а новый появляется на его месте.",
                "Таким образом можно создать интерактивное повествование.",
                "В будущем здесь будут появляться персонажи и меняться фоны.",
                "А пока что мы тестируем базовую функциональность текста.",
                "Это последнее предложение в тестовой истории."
        ));
    }

    private void nextText() {
        currentTextIndex++;
        if (currentTextIndex >= gameText.size()) {
            // Если текст закончился, возвращаемся в меню
            JOptionPane.showMessageDialog(this, "История завершена!");
            cardLayout.show(mainContainer, "MAIN_MENU");
            return;
        }
        updateGameText();
    }

    private void updateGameText() {
        if (currentTextIndex < gameText.size()) {
            // Полностью очищаем текстовую область
            textArea.selectAll();
            textArea.replaceSelection("");

            // Устанавливаем новый текст
            textArea.setText(gameText.get(currentTextIndex));

            // Принудительно очищаем буферы и обновляем
            textArea.setCaretPosition(0);
            textArea.revalidate();
            textArea.repaint();

            // Запускаем сборщик мусора для очистки памяти
            System.gc();
        }
    }

    // Метод для загрузки текста из внешнего источника
    public void loadGameText(ArrayList<String> newText) {
        this.gameText = newText;
        this.currentTextIndex = 0;
        updateGameText();
    }

    // Метод для добавления одного предложения
    public void addTextLine(String text) {
        gameText.add(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new VisualNovelEngine().setVisible(true);
        });
    }
}