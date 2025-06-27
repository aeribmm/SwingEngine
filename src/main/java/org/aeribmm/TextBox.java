package org.aeribmm;

import javax.swing.*;
import java.awt.*;

public class TextBox extends JPanel {
    private final GameScene gameScene;
    private final JTextArea textArea;

    public TextBox(GameScene scene) {
        this.gameScene = scene;

        setOpaque(false); // Не рисуем фон JPanel-ом
        setPreferredSize(new Dimension(0, 180)); // Высота панели

        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        textArea.setForeground(Color.WHITE);
        textArea.setOpaque(false); // Делает саму текстовую область прозрачной
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        add(textArea, BorderLayout.CENTER);

        // Клик мыши — следующий текст
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                gameScene.nextText();
            }
        });
    }

    public void showCurrentText() {
        textArea.setText(gameScene.getCurrentText());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 30;
        int padding = 15;

        // Полупрозрачный чёрный фон
        g2.setColor(new Color(0, 0, 0, 180)); // Прозрачность
        g2.fillRoundRect(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
