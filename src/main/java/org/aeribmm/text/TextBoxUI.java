package org.aeribmm.text;

import org.aeribmm.ui.UI;

import javax.swing.*;
import java.awt.*;

public class TextBoxUI {
    private JPanel textBox;
    private JPanel nameBox;
    private JLabel textArea;
    private JLabel characterNameLabel;
    private boolean isHidden = false;

    public void createTextBox() {
        // Главная панель с адаптивной высотой
        textBox = new JPanel();
        textBox.setLayout(new BorderLayout());
        textBox.setPreferredSize(new Dimension(0, UI.TEXT_BOX_HEIGHT));
        textBox.setOpaque(false);

        // ✅ НОВЫЙ КОД: Адаптивные отступы
        textBox.setBorder(BorderFactory.createEmptyBorder(0,
                UI.MEDIUM_MARGIN,
                UI.MEDIUM_MARGIN,
                UI.MEDIUM_MARGIN));

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

                // ✅ НОВЫЙ КОД: Адаптивный радиус скругления
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), UI.MEDIUM_RADIUS, UI.MEDIUM_RADIUS);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        nameBox.setLayout(new FlowLayout(FlowLayout.LEFT, UI.MEDIUM_MARGIN, UI.SMALL_MARGIN));

        // ✅ НОВЫЙ КОД: Адаптивная высота
        nameBox.setPreferredSize(new Dimension(0, UI.NAME_BOX_HEIGHT));
        nameBox.setOpaque(false);
        nameBox.setVisible(false);

        characterNameLabel = new JLabel();
        characterNameLabel.setForeground(Color.WHITE);

        // ✅ НОВЫЙ КОД: Адаптивный шрифт
        characterNameLabel.setFont(new Font("Arial", Font.BOLD, UI.MEDIUM_FONT));
        nameBox.add(characterNameLabel);
    }

    private JPanel createMainTextPanel() {
        JPanel mainTextPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 200));

                // ✅ НОВЫЙ КОД: Адаптивный радиус скругления
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), UI.MEDIUM_RADIUS, UI.MEDIUM_RADIUS);
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        mainTextPanel.setLayout(new BorderLayout());
        mainTextPanel.setOpaque(false);

        // ✅ НОВЫЙ КОД: Адаптивные отступы
        mainTextPanel.setBorder(BorderFactory.createEmptyBorder(
                UI.MEDIUM_MARGIN,
                UI.LARGE_MARGIN,
                UI.MEDIUM_MARGIN,
                UI.LARGE_MARGIN));

        textArea = new JLabel();
        textArea.setForeground(Color.WHITE);

        // ✅ НОВЫЙ КОД: Адаптивный шрифт
        textArea.setFont(new Font("Arial", Font.PLAIN, UI.LARGE_FONT));
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