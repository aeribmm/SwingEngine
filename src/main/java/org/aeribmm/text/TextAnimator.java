package org.aeribmm.text;

import org.aeribmm.ui.UI;
import org.aeribmm.ui.UIScaleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextAnimator {
    private final TextBoxUI textBoxUI;
    private FastForwardController fastForwardController;
    private Timer typewriterTimer;
    private String fullText = "";
    private int currentCharIndex = 0;
    private boolean isTyping = false;
    private final int TYPING_DELAY = 15;

    public TextAnimator(TextBoxUI textBoxUI) {
        this.textBoxUI = textBoxUI;
    }

    public void setFastForwardController(FastForwardController controller) {
        this.fastForwardController = controller;
    }

    public void showText(String text) {
        animateText(text, false, "");
    }

    public void showCharacterText(String characterName, String text) {
        animateText(text, true, characterName);
    }

    private void animateText(String text, boolean isCharacterDialog, String characterName) {
        if (isCharacterDialog) {
            textBoxUI.showCharacterName(characterName);
        } else {
            textBoxUI.hideCharacterName();
        }

        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        fullText = text;
        currentCharIndex = 0;
        isTyping = true;

        if (fastForwardController != null && fastForwardController.isFastForwardMode()) {
            System.out.println("Быстрая перемотка активна - показываем текст мгновенно");
            finishTypingInstantly();

            if (fastForwardController.isCtrlPressed()) {
                fastForwardController.requestAutoAdvance(50);
            }
            return;
        }

        startAnimation();
    }

    private void startAnimation() {
        textBoxUI.setText("");

        typewriterTimer = new Timer(TYPING_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fastForwardController != null && fastForwardController.isFastForwardMode()) {
                    System.out.println("Быстрая перемотка включилась во время анимации");
                    typewriterTimer.stop();
                    finishTypingInstantly();

                    if (fastForwardController.isCtrlPressed()) {
                        fastForwardController.requestAutoAdvance(50);
                    }
                    return;
                }

                if (currentCharIndex <= fullText.length()) {
                    String displayText = fullText.substring(0, currentCharIndex);
                    textBoxUI.setText(wrapText(displayText));
                    currentCharIndex++;
                } else {
                    typewriterTimer.stop();
                    isTyping = false;
                    System.out.println("Обычная анимация завершена");
                }
            }
        });
        typewriterTimer.start();
    }

    public void finishTyping() {
        if (isTyping && typewriterTimer != null) {
            typewriterTimer.stop();
            textBoxUI.setText(wrapText(fullText));
            isTyping = false;
            System.out.println("Анимация принудительно завершена");

            if (fastForwardController != null &&
                    fastForwardController.isFastForwardMode() &&
                    fastForwardController.isCtrlPressed()) {
                fastForwardController.requestAutoAdvance(50);
            }
        }
    }

    public void finishTypingInstantly() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        if (!fullText.isEmpty()) {
            textBoxUI.setText(wrapText(fullText));
            currentCharIndex = fullText.length();
            isTyping = false;
            System.out.println("Текст показан мгновенно");
        }
    }

    /**
     * ✅ ИСПРАВЛЕННЫЙ МЕТОД: Правильный расчет ширины текста
     */
    private String wrapText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // Получаем текущую ширину текстового окна
        JLabel textArea = textBoxUI.getTextArea();
        if (textArea.getParent() == null) {
            return "<html><body>" + text + "</body></html>";
        }

        // Получаем реальную ширину родительского контейнера
        Container parent = textArea.getParent();
        int containerWidth = parent.getWidth();

        // Если контейнер еще не инициализирован, используем размер экрана
        if (containerWidth <= 0) {
            containerWidth = UIScaleManager.getInstance().getScreenSize().width;
        }

        // Вычисляем безопасную ширину для текста
        int textWidth = calculateOptimalTextWidth(containerWidth);

        System.out.println("Контейнер: " + containerWidth + "px, Текст: " + textWidth + "px");

        return "<html><body style='width: " + textWidth + "px; word-wrap: break-word;'>" +
                text + "</body></html>";
    }

    /**
     * ✅ НОВЫЙ МЕТОД: Оптимальный расчет ширины текста
     */
    private int calculateOptimalTextWidth(int containerWidth) {
        UIScaleManager scaleManager = UIScaleManager.getInstance();
        Dimension screenSize = scaleManager.getScreenSize();

        int totalPadding = UI.LARGE_MARGIN * 4; // 30px слева + 30px справа + отступы панели
        int availableWidth = containerWidth - totalPadding;

        int textWidth;

        // Адаптивный расчет в зависимости от разрешения экрана
        if (screenSize.width >= 3840) { // 4K и выше
            textWidth = Math.min(scaleManager.scaleWidth(1400), (int)(availableWidth * 0.5));
        } else if (screenSize.width >= 2560) { // 2K мониторы
            textWidth = Math.min(scaleManager.scaleWidth(1200), (int)(availableWidth * 0.6));
        } else if (screenSize.width >= 1920) { // Full HD
            textWidth = Math.min(scaleManager.scaleWidth(1000), (int)(availableWidth * 0.7));
        } else { // Меньшие разрешения
            textWidth = (int)(availableWidth * 0.8);
        }

        // Ограничиваем минимальную и максимальную ширину
        textWidth = Math.max(scaleManager.scaleWidth(400), textWidth);
        textWidth = Math.min(scaleManager.scaleWidth(1600), textWidth);

        return textWidth;
    }

    public void cleanup() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
    }

    public boolean isTyping() {
        return isTyping;
    }
}