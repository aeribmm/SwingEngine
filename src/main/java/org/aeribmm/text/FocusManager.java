package org.aeribmm.text;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FocusManager {
    private final JPanel panel;
    private Timer focusTimer;

    public FocusManager(JPanel panel) {
        this.panel = panel;
    }

    public void startFocusMonitoring() {
        focusTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.isDisplayable() && panel.isVisible()) {
                    if (!panel.hasFocus() && !panel.isFocusOwner()) {
                        restoreFocus();
                    }
                }
            }
        });
        focusTimer.start();
    }

    public void restoreFocus() {
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    public void ensureFocus() {
        if (!panel.hasFocus()) {
            restoreFocus();
        }
    }

    public void cleanup() {
        if (focusTimer != null) {
            focusTimer.stop();
            focusTimer = null;
        }
    }
}