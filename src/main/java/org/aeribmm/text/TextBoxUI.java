package org.aeribmm.text;

import javax.swing.*;
import java.awt.*;

public class TextBoxUI {
    private JPanel textBox;
    private JPanel nameBox;
    private JLabel textArea;
    private JLabel characterNameLabel;
    private boolean isHidden = false;

    public void createTextBox() {
        // Главная панель
        textBox = new JPanel();
        textBox.setLayout(new BorderLayout());
        textBox.setPreferredSize(new Dimension(0, 180));
        textBox.setOpaque(false);
        textBox.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        createNameBox();
        createMainTextPanel();

        textBox.add(nameBox, BorderLayout.NORTH);
        textBox.add(createMainTextPanel(), BorderLayout.CENTER);
    }

    private void createNameBox() {
        nameBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        nameBox.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        nameBox.setPreferredSize(new Dimension(0, 40));
        nameBox.setOpaque(false);
        nameBox.setVisible(false);

        characterNameLabel = new JLabel();
        characterNameLabel.setForeground(Color.WHITE);
        characterNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameBox.add(characterNameLabel);
    }

    private JPanel createMainTextPanel() {
        JPanel mainTextPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        mainTextPanel.setLayout(new BorderLayout());
        mainTextPanel.setOpaque(false);
        mainTextPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        textArea = new JLabel();
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));
        textArea.setVerticalAlignment(SwingConstants.TOP);

        mainTextPanel.add(textArea, BorderLayout.CENTER);
        return mainTextPanel;
    }

    public void toggleVisibility() {
        isHidden = !isHidden;
        textBox.setVisible(!isHidden);
    }

    public void showCharacterName(String name) {
        characterNameLabel.setText(name);
        nameBox.setVisible(true);
    }

    public void hideCharacterName() {
        nameBox.setVisible(false);
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    // Геттеры
    public JPanel getTextBox() { return textBox; }
    public JLabel getTextArea() { return textArea; }
    public boolean isHidden() { return isHidden; }
}