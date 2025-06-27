package org.aeribmm.ui;

import org.aeribmm.ui.GameScene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextBox extends JPanel {
    private final GameScene gameScene;
    private final JTextArea textArea;

    public TextBox(GameScene scene) {
        this.gameScene = scene;

        setOpaque(false);
        setPreferredSize(new Dimension(0, 180));
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        textArea.setFocusable(false); // Убираем фокус с текстовой области

        add(textArea, BorderLayout.CENTER);

        // Добавляем индикатор продолжения
        JLabel continueLabel = new JLabel("▼ Нажмите для продолжения", JLabel.CENTER);
        continueLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        continueLabel.setForeground(new Color(200, 200, 200, 150));
        continueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(continueLabel, BorderLayout.SOUTH);

        // Клик мыши — следующий текст (теперь на всей панели)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameScene.nextText();
            }
        });

        // Добавляем курсор указатель при наведении
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void showCurrentText() {
        textArea.setText(gameScene.getCurrentText());
        textArea.setCaretPosition(0);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 20;
        int padding = 15;

        // Полупрозрачный тёмный фон с градиентом
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(0, 0, 0, 200),
                0, getHeight(), new Color(20, 20, 40, 180)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2, arc, arc);

        // Тонкая рамка
        g2.setColor(new Color(100, 100, 150, 100));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
