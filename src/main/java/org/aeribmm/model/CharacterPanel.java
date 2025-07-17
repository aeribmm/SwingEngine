package org.aeribmm.model;

import org.aeribmm.model.CharacterManager;
import javax.swing.*;
import java.awt.*;

/**
 * Панель для отображения персонажей в игре
 */
public class CharacterPanel extends JPanel {
    private CharacterManager characterManager;
    private Timer animationTimer;

    public CharacterPanel() {
        setOpaque(false); // Прозрачная панель
        setLayout(null);  // Абсолютное позиционирование

        // Создаем таймер для анимации (60 FPS)
        animationTimer = new Timer(16, e -> {
            if (characterManager != null) {
                characterManager.update(0.016f); // ~16ms = 0.016 секунд
                repaint();
            }
        });
        animationTimer.start();
    }

    public void setCharacterManager(CharacterManager manager) {
        this.characterManager = manager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (characterManager != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Включаем антиалиасинг для красивого отображения
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Отрисовываем всех персонажей
            characterManager.draw(g2d);

            g2d.dispose();
        }
    }

    public void cleanup() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}