package org.aeribmm.text;

import org.aeribmm.ui.UI;
import org.aeribmm.ui.UIScaleManager;

import javax.swing.*;
import java.awt.*;

public class TextBoxUI {
    private JPanel textBox;
    private JPanel nameBox;
    private JLabel textArea;
    private JLabel characterNameLabel;
    private boolean isHidden = false;

    // Адаптивные настройки
    private final UIScaleManager scaleManager = UIScaleManager.getInstance();

    public void createTextBox() {
        // Главная панель с адаптивной высотой
        textBox = new JPanel();
        textBox.setLayout(new BorderLayout());
        textBox.setPreferredSize(new Dimension(0, UI.TEXT_BOX_HEIGHT));
        textBox.setOpaque(false);

        // ✅ НОВЫЙ КОД: Адаптивные отступы в зависимости от разрешения
        Insets adaptiveInsets = calculateAdaptiveInsets();
        textBox.setBorder(BorderFactory.createEmptyBorder(
                adaptiveInsets.top,
                adaptiveInsets.left,
                adaptiveInsets.bottom,
                adaptiveInsets.right));

        createNameBox();
        createMainTextPanel();

        textBox.add(nameBox, BorderLayout.NORTH);
        textBox.add(createMainTextPanel(), BorderLayout.CENTER);
    }

    /**
     * ✅ ИСПРАВЛЕННЫЙ МЕТОД: Возвращаем нормальные отступы для текстового окна
     */
    private Insets calculateAdaptiveInsets() {
        Dimension screenSize = scaleManager.getScreenSize();
        int screenWidth = screenSize.width;

        int topMargin = 0;
        int bottomMargin = UI.MEDIUM_MARGIN;
        int sideMargin;

        if (screenWidth <= 1366) {
            // Маленькие экраны - небольшие отступы
            sideMargin = UI.MEDIUM_MARGIN;
            System.out.println("Маленький экран: " + screenWidth + "px - минимальные отступы: " + sideMargin + "px");
        } else if (screenWidth <= 1920) {
            // Full HD - умеренные отступы (10% с каждой стороны)
            sideMargin = (int)(screenWidth * 0.08); // 8% с каждой стороны
            System.out.println("Full HD: " + screenWidth + "px - умеренные отступы: " + sideMargin + "px");
        } else if (screenWidth <= 2560) {
            // 2K мониторы - средние отступы
            sideMargin = (int)(screenWidth * 0.12); // 12% с каждой стороны
            System.out.println("2K монитор: " + screenWidth + "px - средние отступы: " + sideMargin + "px");
        } else if (screenWidth <= 3840) {
            // 4K мониторы - большие отступы
            sideMargin = (int)(screenWidth * 0.15); // 15% с каждой стороны
            System.out.println("4K монитор: " + screenWidth + "px - большие отступы: " + sideMargin + "px");
        } else {
            // Сверхвысокие разрешения - очень большие отступы
            sideMargin = (int)(screenWidth * 0.18); // 18% с каждой стороны
            System.out.println("Сверхвысокое разрешение: " + screenWidth + "px - очень большие отступы: " + sideMargin + "px");
        }

        // Ограничиваем минимальные и максимальные отступы
        sideMargin = Math.max(UI.MEDIUM_MARGIN, sideMargin);
        sideMargin = Math.min(screenWidth / 3, sideMargin);

        return new Insets(topMargin, sideMargin, bottomMargin, sideMargin);
    }

    /**
     * ✅ НОВЫЙ МЕТОД: Получает ширину текстового контента
     */
    public int getTextContentWidth() {
        Insets insets = calculateAdaptiveInsets();
        int screenWidth = scaleManager.getScreenSize().width;
        return screenWidth - insets.left - insets.right - (UI.LARGE_MARGIN * 2); // Вычитаем внутренние отступы
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
        characterNameLabel.setFont(new Font("Dialog", Font.BOLD, UI.MEDIUM_FONT));
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
        textArea.setFont(new Font("Dialog", Font.PLAIN, UI.LARGE_FONT));
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

    /**
     * ✅ НОВЫЙ МЕТОД: Получает информацию о текущих размерах
     */
    public String getSizeInfo() {
        Insets insets = calculateAdaptiveInsets();
        int contentWidth = getTextContentWidth();
        int screenWidth = scaleManager.getScreenSize().width;
        double widthPercentage = (double)contentWidth / screenWidth * 100;

        return String.format("Экран: %dpx, Контент: %dpx (%.1f%%), Отступы: %dpx",
                screenWidth, contentWidth, widthPercentage, insets.left);
    }

    // Геттеры
    public JPanel getTextBox() { return textBox; }
    public JLabel getTextArea() { return textArea; }
    public boolean isHidden() { return isHidden; }
}