package org.aeribmm;

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
                "Это первое предложение истории. Здесь будет развиваться сюжет.",
                "Каждый клик мыши будет показывать следующее предложение.",
                "Старый текст исчезает, а новый появляется на его месте.",
                "Таким образом можно создать интерактивное повествование.",
                "В будущем здесь будут появляться персонажи и меняться фоны.",
                "А пока что мы тестируем базовую функциональность текста.",
                "Это последнее предложение в тестовой истории."
        ));
    }

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

    // Методы для загрузки текста из файла
    public void loadGameText(ArrayList<String> newText) {
        this.gameText = newText;
        resetToStart();
    }

    public void addTextLine(String text) {
        gameText.add(text);
    }
}