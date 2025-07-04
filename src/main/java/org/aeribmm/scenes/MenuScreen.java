package org.aeribmm.scenes;

import javax.swing.*;

public abstract class MenuScreen {
    protected JPanel panel;
    public abstract void createMenu();
    public abstract void hide();
    public abstract void show();
    public JPanel getPanel(){return panel;}
}
