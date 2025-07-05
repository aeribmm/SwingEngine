package org.aeribmm.soundManager;

public class AudioInitializer {

    /**
     * Загружает все аудио файлы игры
     */
    public static void loadGameAudio() {
        AudioManager audioManager = AudioManager.getInstance();

        System.out.println("Загрузка аудио ресурсов...");

        // Загружаем фоновую музыку
        audioManager.loadAudio("piano-main", "audio/music/piano-main.wav");


        System.out.println("Аудио ресурсы загружены!");
    }

    /**
     * Загружает только музыку для сцены с пианино
     */
    public static void loadPianoSceneAudio() {
        AudioManager audioManager = AudioManager.getInstance();

        // Музыка для сцены с пианино
        audioManager.loadAudio("liszt", "audio/liszt-love-dream.wav");


        System.out.println("Аудио для сцены с пианино загружено!");
    }
}