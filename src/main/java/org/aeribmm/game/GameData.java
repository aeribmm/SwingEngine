package org.aeribmm.game;

import java.util.ArrayList;
import java.util.Arrays;

public class GameData {
    private ArrayList<String> gameText;
    private int currentTextIndex = 0;

    public GameData() {
        initializeTestData();
    }

    private void initializeTestData() {
        gameText = new ArrayList<>(Arrays.asList(
                "Добро пожаловать в визуальную новеллу!",
                "Это Аири, главная героиня нашей истории.",
                "Смотри, как она улыбается! Разве не мило?",
                "Каждый клик мыши будет показывать следующее предложение.",
                "А иногда Аири может погрустить...",
                "Но не волнуйся, она быстро поправится!",
                "Таким образом можно создать интерактивное повествование.",
                "В будущем здесь будут появляться разные персонажи.",
                "Это последнее предложение в тестовой истории."
        ));
    }

    // ... остальные методы без изменений

    public void resetToStart() {
        currentTextIndex = 0;
    }

    public boolean hasNextText() {
        return currentTextIndex < gameText.size() - 1;
    }

    public void nextText() {
        if (hasNextText()) {
            currentTextIndex++;
        }
    }

    public String getCurrentText() {
        if (currentTextIndex < gameText.size()) {
            return gameText.get(currentTextIndex);
        }
        return "";
    }

    public int getCurrentIndex() {
        return currentTextIndex;
    }

    public void loadGameText(ArrayList<String> newText) {
        this.gameText = new ArrayList<>(newText);
        resetToStart();
    }

    public void addTextLine(String text) {
        gameText.add(text);
    }

}