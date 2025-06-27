package org.aeribmm.ui;
import org.aeribmm.ui.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuScene extends JPanel {
    private GameWindow parentWindow;

    public MainMenuScene(GameWindow parent) {
        this.parentWindow = parent;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Создаем градиентный фон
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Радиальный градиент от центра
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                float radius = Math.max(getWidth(), getHeight()) / 2f;

                Color[] colors = {new Color(45, 45, 80), new Color(15, 15, 30)};
                float[] fractions = {0f, 1f};

                RadialGradientPaint gradient = new RadialGradientPaint(
                        centerX, centerY, radius, fractions, colors);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Добавляем звездочки
                g2d.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < 50; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    int size = (int) (Math.random() * 3) + 1;
                    g2d.fillOval(x, y, size, size);
                }

                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // Заголовок с тенью
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("VISUAL NOVEL", JLabel.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Тень
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(getText(), x + 3, y + 3);

                // Основной текст с градиентом
                GradientPaint textGradient = new GradientPaint(
                        0, 0, new Color(255, 255, 255),
                        0, getHeight(), new Color(200, 200, 255));
                g2d.setPaint(textGradient);
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 50, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        backgroundPanel.add(titlePanel, BorderLayout.NORTH);
        backgroundPanel.add(createButtonPanel(), BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton startButton = createMenuButton("НОВАЯ ИГРА", new Color(70, 130, 180));
        JButton loadButton = createMenuButton("ЗАГРУЗИТЬ", new Color(100, 149, 237));
        JButton settingsButton = createMenuButton("НАСТРОЙКИ", new Color(123, 104, 238));
        JButton exitButton = createMenuButton("ВЫХОД", new Color(220, 20, 60));

        // Действия кнопок
        startButton.addActionListener(e -> parentWindow.showGameScene());
        loadButton.addActionListener(e -> showNotImplemented("Загрузка"));
        settingsButton.addActionListener(e -> showNotImplemented("Настройки"));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue());

        return buttonPanel;
    }

    private JButton createMenuButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Градиентный фон кнопки
                Color color1 = getModel().isRollover() ? baseColor.brighter() : baseColor;
                Color color2 = color1.darker();

                GradientPaint gradient = new GradientPaint(
                        0, 0, color1,
                        0, getHeight(), color2);
                g2d.setPaint(gradient);

                // Скругленные углы
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Рамка
                g2d.setColor(color1.brighter());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);

                // Свечение при наведении
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 18, 18);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(280, 60));
        button.setMaximumSize(new Dimension(280, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Анимация нажатия
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setLocation(button.getX() + 1, button.getY() + 1);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setLocation(button.getX() - 1, button.getY() - 1);
            }
        });

        return button;
    }

    private void showNotImplemented(String feature) {
        JOptionPane.showMessageDialog(this, feature + " пока не реализовано");
    }
}