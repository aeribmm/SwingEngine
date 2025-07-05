package org.aeribmm.parser;

import org.aeribmm.scenes.GameScene;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<String> text;
    private int textIndex;
    private GameScene gameScene;

    public Parser(GameScene gameScene) {
        this.text = new ArrayList<>();
        this.textIndex = 0;
        this.gameScene = gameScene;
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
            gameScene.showText(currentText);
            System.out.println("Строка " + (textIndex + 1) + "/" + text.size() + ": " + currentText);
        }
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
    public void start() {
        textIndex = 0;
        if (!text.isEmpty()) {
            showCurrentLine();
        } else {
            gameScene.showText("Файл пуст или не загружен.");
        }
    }
}
