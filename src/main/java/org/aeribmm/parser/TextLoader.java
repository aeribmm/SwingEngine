package org.aeribmm.parser;

import org.aeribmm.background.BackgroundManager;
import org.aeribmm.soundManager.AudioManager;
import org.aeribmm.scenes.GameScene;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextLoader {
    private List<String> text;
    private int textIndex;
    private GameScene gameScene;
    private boolean started = false; // НОВОЕ ПОЛЕ

    public TextLoader(GameScene gameScene) {
        this.text = new ArrayList<>();
        this.textIndex = 0;
        this.gameScene = gameScene;
        this.started = false; // Инициализируем как не запущенный
    }

    // ... существующие методы ...

    public void start() {
        started = true; // НОВОЕ: отмечаем что запущен
        textIndex = 0;
        if (!text.isEmpty()) {
            showCurrentLine();
        } else {
            gameScene.showText("Файл пуст или не загружен.");
        }
    }

    /**
     * НОВЫЙ МЕТОД: Проверяет, запущен ли загрузчик
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * НОВЫЙ МЕТОД: Сброс состояния (для перезапуска)
     */
    public void reset() {
        started = false;
        textIndex = 0;
    }
    public void loadTextFile(String url){
        text.clear();
        textIndex = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(url), "UTF-8"))){
            String line;
            while((line = reader.readLine()) != null){
                line = line.trim();
                if(!line.isEmpty()){
                    text.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + url);
            text.add("Файл не найден: " + url);
        }catch(IOException ioException){
            System.err.println("IO exception");
        }
    }

    public void showCurrentLine() {
        if (textIndex < text.size()) {
            String currentText = text.get(textIndex);

            // Проверяем, является ли строка командой
            if (isCommand(currentText)) {
                executeCommand(currentText);
                // Автоматически переходим к следующей строке
                if (hasNextLine()) {
                    textIndex++;
                    showCurrentLine();
                }
            }
            // Проверяем, является ли строка диалогом персонажа
            else if (isCharacterDialog(currentText)) {
                String characterName = currentText.trim();
                // Переходим к следующей строке для получения текста диалога
                if (hasNextLine()) {
                    textIndex++;
                    if (textIndex < text.size()) {
                        String dialogText = text.get(textIndex);
                        gameScene.showCharacterText(characterName, dialogText);
                        System.out.println("Диалог " + characterName + ": " + dialogText);
                    }
                }
            }
            else {
                // Обычный текст - показываем игроку
                gameScene.showText(currentText);
                System.out.println("Строка " + (textIndex + 1) + "/" + text.size() + ": " + currentText);
            }
        }
    }
    private boolean isCharacterDialog(String line) {
        return line.trim().endsWith(":") && !line.startsWith("#");
    }

    private boolean isCommand(String line) {
        return line.startsWith("#");
    }

    private void executeCommand(String command) {
        if (command.startsWith("#playMusic(") && command.endsWith(")")) {
            String musicName = extractParameter(command, "#playMusic(", ")");
            AudioManager.getInstance().playBackgroundMusic(musicName);
            System.out.println("Воспроизводится музыка: " + musicName);
        }
        else if (command.equals("#stopMusic")) {
            AudioManager.getInstance().stopBackgroundMusic();
            System.out.println("Музыка остановлена");
        }
        else if (command.startsWith("#playSound(") && command.endsWith(")")) {
            String soundName = extractParameter(command, "#playSound(", ")");
            AudioManager.getInstance().playSoundEffect(soundName);
            System.out.println("Воспроизводится звук: " + soundName);
        }else if (command.startsWith("#changeBackground(") && command.endsWith(")")) {
            String backgroundName = extractParameter(command, "#changeBackground(", ")");
            gameScene.changeBackground(backgroundName);
            System.out.println("Фон изменен на: " + backgroundName);
        }
        else if (command.startsWith("#changeBackgroundAnimated(") && command.endsWith(")")) {
            String backgroundName = extractParameter(command, "#changeBackgroundAnimated(", ")");
            gameScene.changeBackgroundAnimated(backgroundName);
            System.out.println("Фон анимированно изменен на: " + backgroundName);
        }
    }


    private String extractParameter(String line, String prefix, String suffix) {
        int startIndex = line.indexOf(prefix) + prefix.length();
        int endIndex = line.lastIndexOf(suffix);
        if (startIndex < endIndex) {
            String param = line.substring(startIndex, endIndex).trim();
            if (param.startsWith("\"") && param.endsWith("\"")) {
                param = param.substring(1, param.length() - 1);
            }
            return param;
        }
        return "";
    }

    public void nextLine() {
        if (hasNextLine()) {
            textIndex++;
            showCurrentLine();
        } else {
            gameScene.showText("Конец истории. Нажмите ESC для выхода в меню.");
        }
    }

    public boolean hasNextLine() {
        return textIndex < text.size() - 1;
    }
}