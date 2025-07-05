package org.aeribmm.soundManager;

import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AudioLoader {
    private Map<String, Clip> loadedClips;
    private Map<String, AudioInputStream> audioStreams;

    public AudioLoader() {
        this.loadedClips = new HashMap<>();
        this.audioStreams = new HashMap<>();
    }
    public boolean loadAudio(String name, String filePath) {
        System.out.println("Попытка загрузки: " + name + " из " + filePath);

        // Проверяем поддерживаемые форматы
        AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes();
        System.out.println("Поддерживаемые форматы:");
        for (AudioFileFormat.Type type : types) {
            System.out.println("- " + type.toString());
        }
        try {
            // Загружаем из ресурсов
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(filePath);
            if (audioSrc == null) {
                // Пробуем загрузить как обычный файл
                audioSrc = new FileInputStream(filePath);
            }

            // Создаем буферизованный поток
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Получаем формат аудио
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Проверяем, поддерживается ли формат
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Формат аудио не поддерживается: " + filePath);
                return false;
            }

            // Создаем клип
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);

            // Сохраняем клип
            loadedClips.put(name, clip);
            audioStreams.put(name, audioStream);

            System.out.println("Аудио загружено: " + name + " (" + filePath + ")");
            return true;

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Неподдерживаемый формат аудио: " + filePath);
            return false;
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + filePath + " - " + e.getMessage());
            return false;
        } catch (LineUnavailableException e) {
            System.err.println("Аудио линия недоступна: " + e.getMessage());
            return false;
        }
    }

    /**
     * Получает загруженный клип по имени
     */
    public Clip getClip(String name) {
        return loadedClips.get(name);
    }

    /**
     * Проверяет, загружен ли клип
     */
    public boolean isLoaded(String name) {
        return loadedClips.containsKey(name);
    }

    /**
     * Получает информацию о загруженном аудио
     */
    public String getAudioInfo(String name) {
        Clip clip = loadedClips.get(name);
        if (clip != null) {
            long lengthInSeconds = clip.getMicrosecondLength() / 1_000_000;
            return name + " - Длительность: " + lengthInSeconds + " сек";
        }
        return "Аудио не найдено: " + name;
    }

    /**
     * Освобождает ресурсы конкретного клипа
     */
    public void unloadAudio(String name) {
        Clip clip = loadedClips.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            loadedClips.remove(name);
        }

        AudioInputStream stream = audioStreams.get(name);
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                System.err.println("Ошибка закрытия аудио потока: " + e.getMessage());
            }
            audioStreams.remove(name);
        }

        System.out.println("Аудио выгружено: " + name);
    }

    /**
     * Освобождает все загруженные ресурсы
     */
    public void unloadAll() {
        for (String name : loadedClips.keySet()) {
            unloadAudio(name);
        }
        System.out.println("Все аудио файлы выгружены");
    }

    /**
     * Получает список всех загруженных аудио
     */
    public String[] getLoadedAudioNames() {
        return loadedClips.keySet().toArray(new String[0]);
    }
}