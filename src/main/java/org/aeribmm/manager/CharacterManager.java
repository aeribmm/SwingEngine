package org.aeribmm.manager;


import org.aeribmm.model.Character;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aeribmm.model.enums.position.Position;
public class CharacterManager {
    private Map<String, Character> characters;           // Все персонажи
    private List<Character> activeCharacters;            // Персонажи на экране

    public CharacterManager() {
        this.characters = new HashMap<>();
        this.activeCharacters = new ArrayList<>();
        loadCharacters();
    }

    // Загружаем всех персонажей
    private void loadCharacters() {
        // Создаем Аири
        Character airi = new Character("airi", "Аири");
        loadCharacterSprites(airi);
        characters.put("airi", airi);

        // Можно добавить других персонажей
        // Character sakura = new Character("sakura", "Сакура");
        // loadCharacterSprites(sakura);
        // characters.put("sakura", sakura);
    }

    // Загружаем спрайты для персонажа
    private void loadCharacterSprites(Character character) {
        String basePath = "src/main/resources/characters/" + character.getName() + "/";

        // Список файлов для загрузки
        String[] spriteFiles = {
                "default", "smiling", "disappointed"
        };

        for (String spriteName : spriteFiles) {
            String fileName = spriteName + ".png";
            String fullPath = basePath + fileName;

            try {
                File imageFile = new File(fullPath);
                if (imageFile.exists()) {
                    BufferedImage sprite = ImageIO.read(imageFile);
                    character.addSprite(spriteName.toUpperCase(), sprite);
                    System.out.println("Загружен спрайт: " + fileName);
                } else {
                    System.out.println("Спрайт не найден: " + fullPath);
                }
            } catch (Exception e) {
                System.err.println("Ошибка загрузки спрайта " + fileName + ": " + e.getMessage());
            }
        }
    }

    // === МЕТОДЫ УПРАВЛЕНИЯ ПЕРСОНАЖАМИ ===

    // Показать персонажа на экране
    public void showCharacter(String characterId, Position position, String emotion) {
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

    // === ТЕСТОВЫЕ МЕТОДЫ ===

    public void testShowAiri() {
        showCharacter("airi", Position.CENTER, "default");
    }

    public void testChangeAiriEmotion() {
        changeEmotion("airi", "smiling");
    }
}
