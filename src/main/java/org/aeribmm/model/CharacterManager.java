package org.aeribmm.model;


import org.aeribmm.model.Character;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aeribmm.model.enums.CharacterPosition;
public class CharacterManager {
    private Map<String, Character> characters;           // Все персонажи
    private List<Character> activeCharacters;            // Персонажи на экране

    public CharacterManager() {
        this.characters = new HashMap<>();
        this.activeCharacters = new ArrayList<>();
        loadCharacters();
    }

    // Загружаем спрайты для персонажа
    private void loadCharacterSprites(Character character) {
        // Персонаж сам загружает все свои спрайты из папки
        character.loadSpritesFromFolder("src/main/resources/character");

        // Альтернативно, можно загружать только определенные эмоции:
        // String[] allowedEmotions = {"default", "smiling", "disappointed", "angry", "sad"};
        // character.loadSpritesFromFolderWithFilter("src/main/resources/character", allowedEmotions);
    }

    // Также обновите метод loadCharacters:
    private void loadCharacters() {
        // Создаем Аири
        Character airi = new Character("airi", "Аири");
        loadCharacterSprites(airi);
        characters.put("airi", airi);

        // Добавляем других персонажей
        // Character sakura = new Character("sakura", "Сакура");
        // loadCharacterSprites(sakura);
        // characters.put("sakura", sakura);

        System.out.println("Загружено персонажей: " + characters.size());
    }

    // === МЕТОДЫ УПРАВЛЕНИЯ ПЕРСОНАЖАМИ ===

    // Показать персонажа на экране
    public void showCharacter(String characterId, CharacterPosition position, String emotion) {
        Character character = characters.get(characterId);
        if (character == null) {
            System.err.println("Персонаж не найден: " + characterId);
            return;
        }

        character.setPosition(position);
        if (emotion != null) {
            character.setEmotion(emotion);
        }
        character.show();

        // Добавляем в активные, если еще нет
        if (!activeCharacters.contains(character)) {
            activeCharacters.add(character);
        }

        System.out.println("Показан персонаж: " + character.getName() + " в позиции " + position);
    }

    // Скрыть персонажа
    public void hideCharacter(String characterId) {
        Character character = characters.get(characterId);
        if (character != null) {
            character.hide();
            // Не удаляем сразу из activeCharacters - пусть анимация закончится
        }
    }

    // Изменить эмоцию
    public void changeEmotion(String characterId, String emotion) {
        Character character = characters.get(characterId);
        if (character != null) {
            character.setEmotion(emotion);
            System.out.println(character.getName() + " сменил эмоцию на " + emotion);
        }
    }

    // Очистить экран от всех персонажей
    public void hideAllCharacters() {
        for (Character character : activeCharacters) {
            character.hide();
        }
    }

    // Обновить анимации
    public void update(float deltaTime) {
        activeCharacters.removeIf(character -> !character.isVisible() && !character.isAnimating());

        for (Character character : activeCharacters) {
            character.updateAnimation(deltaTime);
        }
    }
    //     Отрисовать всех персонажей
    public void draw(Graphics2D g2d) {
        // Сортируем по zOrder (кто ближе)
        activeCharacters.sort((a, b) -> Integer.compare(a.getZOrder(), b.getZOrder()));

        for (Character character : activeCharacters) {
            character.draw(g2d);
        }
    }


    // === ГЕТТЕРЫ ===

    public Character getCharacter(String id) {
        return characters.get(id);
    }

    public List<Character> getActiveCharacters() {
        return new ArrayList<>(activeCharacters);
    }
}

