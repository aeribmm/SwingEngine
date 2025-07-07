package org.aeribmm.text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextAnimator {
    private final TextBoxUI textBoxUI;
    private FastForwardController fastForwardController; // Ссылка на контроллер
    private Timer typewriterTimer;
    private String fullText = "";
    private int currentCharIndex = 0;
    private boolean isTyping = false;
    private final int TYPING_DELAY = 15;

    public TextAnimator(TextBoxUI textBoxUI) {
        this.textBoxUI = textBoxUI;
    }

    // ВАЖНО: Метод для установки связи с FastForwardController
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
        // Настройка UI
        if (isCharacterDialog) {
            textBoxUI.showCharacterName(characterName);
        } else {
            textBoxUI.hideCharacterName();
        }

        // Остановка предыдущей анимации
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        // Настройка анимации
        fullText = text;
        currentCharIndex = 0;
        isTyping = true;

        // ИСПРАВЛЕНИЕ: Проверяем быструю перемотку
        if (fastForwardController != null && fastForwardController.isFastForwardMode()) {
            System.out.println("Быстрая перемотка активна - показываем текст мгновенно");
            finishTypingInstantly();

            // Запускаем автопродвижение
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
                // ИСПРАВЛЕНИЕ: Проверяем быструю перемотку во время анимации
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

            // ИСПРАВЛЕНИЕ: Проверяем быструю перемотку после завершения
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

    private String wrapText(String text) {
        // Используем вашу логику адаптивной ширины
        if (textBoxUI.getTextArea().getParent() == null) {
            return "<html><body>" + text + "</body></html>";
        }

        int panelWidth = textBoxUI.getTextArea().getParent().getWidth();
        int textWidth = calculateSafeTextWidth(panelWidth);

        return "<html><body style='width: " + textWidth + "px'>" + text + "</body></html>";
    }

    private int calculateSafeTextWidth(int panelWidth) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int textWidth;
        if (screenSize.width >= 3000) {
            textWidth = Math.min(1400, (int)(panelWidth * 0.45));
        } else if (screenSize.width >= 1800) {
            textWidth = Math.min(1200, (int)(panelWidth * 0.65));
        } else {
            textWidth = (int)(panelWidth * 0.73);
        }

        textWidth = Math.max(400, textWidth);
        textWidth = Math.min(1600, textWidth);
        return textWidth;
    }

    public void cleanup() {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
    }

    // Геттеры
    public boolean isTyping() { return isTyping; }
}