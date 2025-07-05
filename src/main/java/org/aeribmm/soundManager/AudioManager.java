package org.aeribmm.soundManager;


import javax.sound.sampled.*;

public class AudioManager {
    private static AudioManager instance;
    private AudioLoader audioLoader;

    private Clip currentBackgroundMusic;
    private Clip currentSoundEffect;
    private String currentMusicName = "";

    private float musicVolume = 0.7f;
    private float soundVolume = 1.0f;
    private boolean musicMuted = false;
    private boolean soundMuted = false;

    private AudioManager() {
        audioLoader = new AudioLoader();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Загружает аудио файл
     */
    public boolean loadAudio(String name, String filePath) {
        return audioLoader.loadAudio(name, filePath);
    }

    /**
     * Воспроизводит фоновую музыку
     */
    public void playBackgroundMusic(String name) {
        playBackgroundMusic(name, true);
    }

    /**
     * Воспроизводит фоновую музыку с опцией зацикливания
     */
    public void playBackgroundMusic(String name, boolean loop) {
        // Останавливаем текущую музыку
        stopBackgroundMusic();

        if (!audioLoader.isLoaded(name)) {
            System.err.println("Музыка не загружена: " + name);
            return;
        }

        currentBackgroundMusic = audioLoader.getClip(name);
        currentMusicName = name;

        if (currentBackgroundMusic != null) {
            // Устанавливаем громкость
            setClipVolume(currentBackgroundMusic, musicVolume);

            // Перематываем в начало
            currentBackgroundMusic.setFramePosition(0);

            // Запускаем воспроизведение
            if (loop) {
                currentBackgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                currentBackgroundMusic.start();
            }

            System.out.println("Воспроизводится музыка: " + name);
        }
    }

    /**
     * Останавливает фоновую музыку
     */
    public void stopBackgroundMusic() {
        if (currentBackgroundMusic != null && currentBackgroundMusic.isRunning()) {
            currentBackgroundMusic.stop();
            currentBackgroundMusic.setFramePosition(0);
            System.out.println("Музыка остановлена: " + currentMusicName);
        }
        currentMusicName = "";
    }

    /**
     * Приостанавливает/возобновляет фоновую музыку
     */
    public void pauseBackgroundMusic() {
        if (currentBackgroundMusic != null && currentBackgroundMusic.isRunning()) {
            currentBackgroundMusic.stop();
            System.out.println("Музыка приостановлена");
        }
    }

    public void resumeBackgroundMusic() {
        if (currentBackgroundMusic != null && !currentBackgroundMusic.isRunning()) {
            currentBackgroundMusic.start();
            System.out.println("Музыка возобновлена");
        }
    }

    /**
     * Воспроизводит звуковой эффект
     */
    public void playSoundEffect(String name) {
        if (!audioLoader.isLoaded(name)) {
            System.err.println("Звук не загружен: " + name);
            return;
        }

        Clip soundClip = audioLoader.getClip(name);
        if (soundClip != null) {
            // Останавливаем и перематываем в начало
            soundClip.stop();
            soundClip.setFramePosition(0);

            // Устанавливаем громкость
            setClipVolume(soundClip, soundVolume);

            // Воспроизводим один раз
            soundClip.start();

            System.out.println("Воспроизводится звук: " + name);
        }
    }

    /**
     * Устанавливает громкость клипа
     */
    private void setClipVolume(Clip clip, float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Конвертируем громкость (0.0-1.0) в децибелы
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            dB = Math.max(dB, gainControl.getMinimum());
            dB = Math.min(dB, gainControl.getMaximum());

            gainControl.setValue(dB);
        }
    }

    /**
     * Устанавливает громкость музыки (0.0 - 1.0)
     */
    public void setMusicVolume(float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (currentBackgroundMusic != null) {
            setClipVolume(currentBackgroundMusic, musicMuted ? 0.0f : musicVolume);
        }
    }

    /**
     * Устанавливает громкость звуков (0.0 - 1.0)
     */
    public void setSoundVolume(float volume) {
        soundVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    /**
     * Включает/выключает музыку
     */
    public void muteMusicMusic(boolean mute) {
        musicMuted = mute;
        if (currentBackgroundMusic != null) {
            setClipVolume(currentBackgroundMusic, mute ? 0.0f : musicVolume);
        }
    }

    /**
     * Включает/выключает звуки
     */
    public void muteSounds(boolean mute) {
        soundMuted = mute;
    }

    /**
     * Проверяет, играет ли музыка
     */
    public boolean isMusicPlaying() {
        return currentBackgroundMusic != null && currentBackgroundMusic.isRunning();
    }

    /**
     * Получает имя текущей музыки
     */
    public String getCurrentMusicName() {
        return currentMusicName;
    }

    /**
     * Освобождает все ресурсы
     */
    public void cleanup() {
        stopBackgroundMusic();
        audioLoader.unloadAll();
        System.out.println("AudioManager очищен");
    }
}