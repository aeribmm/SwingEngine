package org.aeribmm.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextBox extends JPanel {
    private final GameScene gameScene;
    private final JTextArea textArea;
    private JLabel nameLabel;
    private final JPanel namePanel;

    public TextBox(GameScene scene) {
        this.gameScene = scene;

        setOpaque(false);
        setPreferredSize(new Dimension(0, 160));
        setLayout(new BorderLayout());

        // Создаем панель с именем персонажа
        namePanel = createNamePanel();

        // Создаем основную текстовую область
        textArea = createTextArea();

        // Добавляем компоненты
        add(namePanel, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);

        // Обработчик кликов
        setupClickHandler();
    }

    private JPanel createNamePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Полностью прозрачная панель имени
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
        panel.setPreferredSize(new Dimension(0, 35));

        nameLabel = new JLabel("Аири") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // ВАРИАНТ 1: Полупрозрачный фон для имени (оставляем видимым)
                g2d.setColor(new Color(0, 0, 0, 150)); // Слегка темнее для читаемости
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // ВАРИАНТ 2: Полностью убрать фон имени (раскомментируйте если нужно)
                // (не рисуем ничего = прозрачный)

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(new Color(255, 255, 255, 255)); // Белый текст
        nameLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        nameLabel.setOpaque(false);

        panel.add(nameLabel);
        return panel;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                // Прозрачная текстовая область
                super.paintComponent(g);
            }
        };

        area.setFont(new Font("SansSerif", Font.PLAIN, 16));
        area.setForeground(new Color(255, 255, 255, 255)); // Белый текст
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setBorder(BorderFactory.createEmptyBorder(15, 30, 20, 30));

        return area;
    }

    private void setupClickHandler() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameScene.nextText();
            }
        });

        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameScene.nextText();
            }
        });
    }

    public void showCurrentText() {
        textArea.setText(gameScene.getCurrentText());
        textArea.setCaretPosition(0);
        repaint();
    }

    public void setCharacterName(String name) {
        nameLabel.setText(name);
        namePanel.setVisible(name != null && !name.isEmpty());
    }

    public void setNameColor(Color color) {
        nameLabel.setForeground(color);
    }
    public String clearText(){
        return "";
    }

    @Override
    protected void paintComponent(Graphics g) {
        // УБРАЛИ ОТЛАДОЧНЫЕ СООБЩЕНИЯ!

        // ВАРИАНТ A: Полностью прозрачное текстовое окно
//        super.paintComponent(g); // Не рисуем вообще ничего


        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int nameHeight = namePanel.getPreferredSize().height;
        int textY = nameHeight;
        int textHeight = getHeight() - nameHeight;

        // Полупрозрачный черный фон
        g2d.setColor(new Color(0, 0, 0, 128));
        g2d.dispose();
        super.paintComponent(g);


        // Только тонкая линия, без фона
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(0, nameHeight, getWidth(), nameHeight);

        g2d.dispose();
        super.paintComponent(g);

    }
}