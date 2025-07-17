package org.aeribmm.model;

import org.aeribmm.model.enums.AnimationType;
import org.aeribmm.model.enums.CharacterPosition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Character {
    private String id;
    private String name;
    private String nameInGame;

    //=== ВИЗУАЛЬНАЯ ИНФОРМАЦИЯ ===

    private HashMap<String, BufferedImage> sprites;
    private String currentEmotion;
    private boolean isVisible;

    // === ПОЗИЦИОНИРОВАНИЕ ===
    private CharacterPosition screenPosition;
    private Point coordinates;
    private float scale;
    private int zOrder;

    // === АНИМАЦИЯ ===

    private boolean isAnimating;
    private AnimationType currentAnimation;
    private float animationProgress;

    // === ДИАЛОГИ ===

    private Color nameColor;

    //game data
    private int relationshipLevel;

    public Character(String id, String nameInGame) {
        this.id = id;
        this.name = id;
        this.nameInGame = nameInGame;

        // Инициализация
        this.sprites = new HashMap<>();
        this.currentEmotion = "default";
        this.isVisible = false;
        this.screenPosition = CharacterPosition.CENTER.CENTER;
        this.coordinates = new Point(0, 0);
        this.scale = 0.8f;
        this.zOrder = 0;

        // Анимация
        this.isAnimating = false;
        this.animationProgress = 0.0f;

        // Диалоги
        this.nameColor = Color.WHITE;

        // Игровые данные
        this.relationshipLevel = 0;

    }

    // === МЕТОДЫ ЗАГРУЗКИ СПРАЙТОВ ===


    public void loadSpritesFromFolder(String basePath) {
        // Создаем путь к папке персонажа
        String characterFolderPath = basePath + "/" + this.id + "/";
        File characterFolder = new File(characterFolderPath);

        // Проверяем, существует ли папка
        if (!characterFolder.exists() || !characterFolder.isDirectory()) {
            System.err.println("Папка персонажа не найдена: " + characterFolderPath);
            return;
        }

        // Получаем все файлы изображений из папки
        File[] files = characterFolder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg");
        });

        if (files == null || files.length == 0) {
            System.out.println("В папке " + characterFolderPath + " не найдено изображений");
            return;
        }

        // Загружаем каждый файл как спрайт
        for (File file : files) {
            try {
                BufferedImage sprite = ImageIO.read(file);

                // Получаем имя эмоции из имени файла (без расширения)
                String fileName = file.getName();
                String emotionName = fileName.substring(0, fileName.lastIndexOf('.'));

                // Добавляем спрайт в коллекцию
                addSprite(emotionName, sprite);

                System.out.println("Загружен спрайт: " + fileName + " как эмоция '" + emotionName + "'");

            } catch (IOException e) {
                System.err.println("Ошибка загрузки спрайта " + file.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("Загружено " + sprites.size() + " спрайтов для персонажа " + name);

        // Устанавливаем эмоцию по умолчанию, если она есть
        if (sprites.containsKey("default")) {
            this.currentEmotion = "default";
        } else if (!sprites.isEmpty()) {
            // Если нет default, берем первую доступную эмоцию
            this.currentEmotion = sprites.keySet().iterator().next();
        }
    }

    /**
     * Перегруженный метод с стандартным путем
     */
    public void loadSpritesFromFolder() {
        loadSpritesFromFolder("src/main/resources/character");
    }

    public void loadSpritesFromFolderWithFilter(String basePath, String[] allowedEmotions) {
        String characterFolderPath = basePath + "/" + this.id + "/";
        File characterFolder = new File(characterFolderPath);

        if (!characterFolder.exists() || !characterFolder.isDirectory()) {
            System.err.println("Папка персонажа не найдена: " + characterFolderPath);
            return;
        }

        // Создаем множество разрешенных эмоций для быстрого поиска
        Set<String> allowedSet = new HashSet<>();
        for (String emotion : allowedEmotions) {
            allowedSet.add(emotion);
        }

        File[] files = characterFolder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            if (!(lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg"))) {
                return false;
            }

            // Проверяем, входит ли имя файла в список разрешенных
            String emotionName = name.substring(0, name.lastIndexOf('.'));
            return allowedSet.contains(emotionName);
        });

        if (files == null || files.length == 0) {
            System.out.println("В папке " + characterFolderPath + " не найдено подходящих изображений");
            return;
        }

        for (File file : files) {
            try {
                BufferedImage sprite = ImageIO.read(file);
                String fileName = file.getName();
                String emotionName = fileName.substring(0, fileName.lastIndexOf('.'));

                addSprite(emotionName, sprite);
                System.out.println("Загружен спрайт: " + fileName + " как эмоция '" + emotionName + "'");

            } catch (IOException e) {
                System.err.println("Ошибка загрузки спрайта " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Получает список всех доступных эмоций
     * @return множество имен эмоций
     */
    public Set<String> getAvailableEmotions() {
        return new HashSet<>(sprites.keySet());
    }

    /**
     * Проверяет, загружены ли спрайты
     * @return true, если есть хотя бы один спрайт
     */
    public boolean hasSprites() {
        return !sprites.isEmpty();
    }

    /**
     * Получает количество загруженных спрайтов
     * @return количество спрайтов
     */
    public int getSpriteCount() {
        return sprites.size();
    }

    // === ОСТАЛЬНЫЕ МЕТОДЫ ===

    public boolean isAnimating(){
        return isAnimating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addSprite(String emotion, BufferedImage sprite) {
        sprites.put(emotion, sprite);
    }

    public BufferedImage getCurrentSprite() {
        return sprites.get(currentEmotion);
    }

    public void setEmotion(String emotion) {
        if (sprites.containsKey(emotion)) {
            this.currentEmotion = emotion;
        }else if (sprites.containsKey("default")){
            this.currentEmotion = "default";
        }else{
            System.out.println("emotions not found");
        }
    }

    public boolean hasEmotion(String emotion) {
        return sprites.containsKey(emotion);
    }

    // === МЕТОДЫ ПОЗИЦИОНИРОВАНИЯ ===

    public void setPosition(CharacterPosition position) {
        this.screenPosition = position;
        int gameAreaHeight = 600 - 180;
        int characterY = gameAreaHeight - 400;
        switch (position) {
            case LEFT:
                this.coordinates = new Point(150, characterY);
                break;
            case CENTER:
                this.coordinates = new Point(400, characterY);
                break;
            case RIGHT:
                this.coordinates = new Point(650, characterY);
                break;
        }
    }

    public void setCoordinates(int x, int y) {
        this.coordinates = new Point(x, y);
        this.screenPosition = CharacterPosition.CUSTOM;
    }

    // === МЕТОДЫ АНИМАЦИИ ===

    public void startAnimation(AnimationType type) {
        this.currentAnimation = type;
        this.isAnimating = true;
        this.animationProgress = 0.0f;
    }

    public void updateAnimation(float deltaTime) {
        if (isAnimating) {
            animationProgress += deltaTime;
            if (animationProgress >= 1.0f) {
                animationProgress = 1.0f;
                isAnimating = false;
            }
        }
    }

    // === МЕТОДЫ ОТОБРАЖЕНИЯ ===

    public void show() {
        this.isVisible = true;
        startAnimation(AnimationType.FADE_IN);
    }

    public void hide() {
        startAnimation(AnimationType.FADE_OUT);
        // isVisible будет установлен в false после завершения анимации
    }

    public void draw(Graphics2D g2d) {
        if (!isVisible && !isAnimating) return;

        BufferedImage sprite = getCurrentSprite();
        if (sprite == null) return;

        // Сохраняем текущее состояние Graphics2D
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        g2dCopy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2dCopy.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Применяем анимацию
        float alpha = 1.0f;
        if (isAnimating) {
            switch (currentAnimation) {
                case FADE_IN:
                    alpha = animationProgress;
                    break;
                case FADE_OUT:
                    alpha = 1.0f - animationProgress;
                    if (animationProgress >= 1.0f) {
                        isVisible = false;
                    }
                    break;
                case SLIDE_IN:
                    // Скольжение слева
                    int offsetX = (int) ((1.0f - animationProgress) * -200);
                    g2dCopy.translate(offsetX, 0);
                    break;
            }
        }

        // Ограничиваем alpha
        alpha = Math.max(0.0f, Math.min(1.0f, alpha));

        // Устанавливаем прозрачность
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2dCopy.setComposite(composite);

        // Рисуем спрайт с масштабированием
        int width = (int) (sprite.getWidth() * scale);
        int height = (int) (sprite.getHeight() * scale);

        int drawX = Math.max(-width, Math.min(1200, coordinates.x));
        int drawY = Math.max(-height, Math.min(800, coordinates.y));

        g2dCopy.drawImage(sprite, drawX, drawY, width, height, null);

        // Освобождаем ресурсы
        g2dCopy.dispose();
    }

    // === ИГРОВЫЕ МЕТОДЫ ===

    public void increaseRelationship(int amount) {
        this.relationshipLevel = Math.min(100, relationshipLevel + amount);
    }

    // === ГЕТТЕРЫ И СЕТТЕРЫ ===

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameInGame() {
        return nameInGame;
    }

    public void setNameInGame(String displayName) {
        this.nameInGame = displayName;
    }

    public String getCurrentEmotion() {
        return currentEmotion;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public CharacterPosition getScreenPosition() {
        return screenPosition;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public float getScale() {
        return scale;
    }
    public int getZOrder() { return zOrder; }
    public void setZOrder(int zOrder) { this.zOrder = zOrder; }

    public void setScale(float scale) {
        this.scale = Math.max(0.1f, Math.min(3.0f, scale));
    }

    public Color getNameColor() {
        return nameColor;
    }

    public void setNameColor(Color nameColor) {
        this.nameColor = nameColor;
    }

    public int getRelationshipLevel() {
        return relationshipLevel;
    }

    public void setRelationshipLevel(int level) {
        this.relationshipLevel = Math.max(0, Math.min(100, level));
    }

    public static Character createDefault(String id, String name) {
        Character character = new Character(id, name);
        character.setNameColor(new Color(100, 149, 237)); // Корнфлауэр синий
        return character;
    }

    @Override
    public String toString() {
        return String.format("Character{name in system ='%s', name in game='%s', emotion='%s', visible=%s}",
                name, nameInGame, currentEmotion, isVisible);
    }
}