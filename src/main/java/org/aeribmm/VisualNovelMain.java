package org.aeribmm;

import org.aeribmm.scenes.GameScene;
import org.aeribmm.scenes.MainMenuScreen;
import org.aeribmm.scenes.MenuScreen;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VisualNovelMain {
    private JFrame frame;
    private Map<String, MenuScreen> menus;
    private MenuScreen currentMenu;
    private static VisualNovelMain instance;


    public static VisualNovelMain getInstance() {
        return instance;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Map<String, MenuScreen> getMenus() {
        return menus;
    }

    public void setMenus(Map<String, MenuScreen> menus) {
        this.menus = menus;
    }

    public MenuScreen getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(MenuScreen currentMenu) {
        this.currentMenu = currentMenu;
    }

    public VisualNovelMain() {
        instance = this;
        initializeApplication();
        createMenus();
        showMainMenu();
    }

    private void initializeApplication() {
        // Создаем главное окно
        frame = new JFrame("Visual Novel Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        // Запрещаем изменение размера
        frame.setResizable(false);

        // Устанавливаем полноэкранный режим
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Альтернативный способ - установить размер экрана вручную
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // frame.setSize(screenSize);

        // Центрируем окно (на случай если не сработает максимизация)
        frame.setLocationRelativeTo(null); // Центрируем окно

        // Инициализируем HashMap для меню
        menus = new HashMap<>();
    }

    private void createMenus() {
        // Создаем все меню и помещаем их в HashMap
        MainMenuScreen mainMenu = new MainMenuScreen();
        mainMenu.createMenu();
        menus.put("main", mainMenu);
        GameScene gameScreen = new GameScene();
        gameScreen.createMenu();
        menus.put("game",gameScreen);
    }

    private void showMainMenu() {
        showMenu("main");
    }

    public void showMenu(String menuName) {
        MenuScreen menu = menus.get(menuName);
        if (menu != null) {
            // Скрываем текущее меню
            if (currentMenu != null) {
                currentMenu.hide();
                frame.remove(currentMenu.getPanel());
            }

            // Показываем новое меню
            currentMenu = menu;
            frame.add(currentMenu.getPanel());
            currentMenu.show();

            // Обновляем окно
            frame.revalidate();
            frame.repaint();
        }
    }
    public void changeScreen(String name) {
        if (name == null) return;

        MenuScreen newMenu = menus.get(name);
        if (newMenu == null) {
            System.err.println("Меню не найдено: " + name);
            return;
        }

        // Проверяем, не то ли это же меню
        if (currentMenu == newMenu) {
            return;
        }

        // Скрываем и удаляем старое меню
        if (currentMenu != null) {
            currentMenu.hide();
            frame.remove(currentMenu.getPanel());
        }

        // Показываем новое меню
        currentMenu = newMenu;
        frame.add(currentMenu.getPanel());
        currentMenu.show();

        // Обновляем GUI
        frame.revalidate();
        frame.repaint();

        currentMenu.getPanel().requestFocus();
    }
}