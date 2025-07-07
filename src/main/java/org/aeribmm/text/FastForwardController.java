package org.aeribmm.text;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FastForwardController {
    private final TextAnimator textAnimator;
    private boolean fastForwardMode = false;
    private boolean ctrlPressed = false;
    private List<Timer> activeTimers = new ArrayList<>();
    private AdvanceListener advanceListener;

    public FastForwardController(TextAnimator textAnimator) {
        this.textAnimator = textAnimator;
    }

    public void enableFastForward() {
        System.out.println("=== ВКЛЮЧЕНИЕ БЫСТРОЙ ПЕРЕМОТКИ ===");

        if (!fastForwardMode) {
            fastForwardMode = true;
            ctrlPressed = true;
            System.out.println("✅ Быстрая перемотка ВКЛЮЧЕНА");

            stopAllTimers();

            if (textAnimator.isTyping()) {
                System.out.println("Принудительно завершаем анимацию текста");
                textAnimator.finishTypingInstantly();
                requestAutoAdvance(100);
            } else {
                System.out.println("Текст не печатается, сразу переходим к следующей строке");
                requestAutoAdvance(50);
            }
        }
    }

    public void disableFastForward() {
        System.out.println("=== ВЫКЛЮЧЕНИЕ БЫСТРОЙ ПЕРЕМОТКИ ===");

        if (fastForwardMode) {
            fastForwardMode = false;
            ctrlPressed = false;
            System.out.println("✅ Быстрая перемотка ВЫКЛЮЧЕНА");
            stopAllTimers();
        }
    }

    // НОВЫЙ МЕТОД: Запрос автопродвижения (может вызываться из TextAnimator)
    public void requestAutoAdvance(int delay) {
        System.out.println("Создание таймера автопродвижения с задержкой: " + delay + "мс");

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Таймер сработал! ctrlPressed: " + ctrlPressed + ", fastForward: " + fastForwardMode);

                if (ctrlPressed && fastForwardMode && advanceListener != null) {
                    System.out.println("Выполняем автопродвижение");
                    advanceListener.onAdvanceNeeded();
                } else {
                    System.out.println("Условия не выполнены, автопродвижение отменено");
                }

                Timer currentTimer = (Timer) e.getSource();
                activeTimers.remove(currentTimer);
                System.out.println("Таймер удален из списка, осталось: " + activeTimers.size());
            }
        });
        timer.setRepeats(false);
        activeTimers.add(timer);
        timer.start();
        System.out.println("Таймер запущен, всего: " + activeTimers.size());
    }

    private void stopAllTimers() {
        System.out.println("Останавливаем все таймеры: " + activeTimers.size());

        List<Timer> timersToStop = new ArrayList<>(activeTimers);
        for (Timer timer : timersToStop) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
        }
        activeTimers.clear();
        System.out.println("Все таймеры остановлены, список очищен. Осталось: " + activeTimers.size());
    }

    public void cleanup() {
        stopAllTimers();
    }

    // Геттеры и сеттеры
    public boolean isFastForwardMode() { return fastForwardMode; }
    public boolean isCtrlPressed() { return ctrlPressed; }

    public void setAdvanceListener(AdvanceListener listener) {
        this.advanceListener = listener;
    }

    // Интерфейс для уведомления о необходимости продвижения
    public interface AdvanceListener {
        void onAdvanceNeeded();
    }
}
