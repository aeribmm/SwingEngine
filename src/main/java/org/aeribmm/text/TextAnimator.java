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
     * ✅ ОБНОВЛЕННЫЙ МЕТОД: Адаптивная обработка текста
     */
    private String wrapText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // Получаем оптимальную ширину текста из TextBoxUI
        int textWidth = calculateOptimalTextWidth();

        System.out.println("Адаптивная ширина текста: " + textWidth + "px (" + textBoxUI.getSizeInfo() + ")");

        return "<html><body style='width: " + textWidth + "px; word-wrap: break-word;'>" +
                text + "</body></html>";
    }

    /**
     * ✅ ИСПРАВЛЕННЫЙ МЕТОД: Правильно рассчитывает ширину для переноса строк
     */
    private int calculateOptimalTextWidth() {
        UIScaleManager scaleManager = UIScaleManager.getInstance();
        int screenWidth = scaleManager.getScreenSize().width;

        // Жестко задаем ширину для переноса строк в зависимости от разрешения
        int textWidth;

        if (screenWidth <= 1366) {
             textWidth = 900;
        } else if (screenWidth <= 1920) {
            textWidth = 950; // Для Full HD и около того (ваш случай 1536px)
        } else if (screenWidth <= 2560) {
            textWidth = 1590; // Для 2K мониторов
        } else if (screenWidth <= 3840) {
            textWidth = 2380; // Для 4K мониторов
        } else {
            textWidth = 2200; // Для сверхвысоких разрешений
        }

        System.out.println("Жестко заданная ширина для переноса строк: " + textWidth + "px (экран: " + screenWidth + "px)");

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