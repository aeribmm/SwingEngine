package org.aeribmm.model;

import org.aeribmm.model.enums.animation.AnimationType;
import org.aeribmm.model.enums.position.Position;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Character {
    private String id;
    private String name;
    private String nameInGame;

    //=== ВИЗУАЛЬНАЯ ИНФОРМАЦИЯ ===

    private HashMap<String, BufferedImage> sprites;
    private String currentEmotion;
    private boolean isVisible;

    // === ПОЗИЦИОНИРОВАНИЕ ===
    private Position screenPosition;
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
        this.screenPosition = Position.CENTER;
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
    public boolean isAnimating(){
        return isAnimating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//todo make field which represents relationships with main character

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

    public void setPosition(Position position) {
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
        this.screenPosition = Position.CUSTOM;
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
        AffineTransform oldTransform = g2d.getTransform();
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
                    g2d.translate(offsetX, 0);
                    break;
            }
        }

        // Устанавливаем прозрачность
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(composite);

        // Рисуем спрайт с масштабированием
        int width = (int) (sprite.getWidth() * scale);
        int height = (int) (sprite.getHeight() * scale);
        g2d.drawImage(sprite, coordinates.x, coordinates.y, width, height, null);

        // Восстанавливаем состояние
        g2d.setTransform(oldTransform);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
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

    public Position getScreenPosition() {
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