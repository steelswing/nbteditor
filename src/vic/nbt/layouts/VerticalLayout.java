/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

package vic.nbt.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * File: VerticalLayout.java
 * Created on 22.09.2022, 6:23:08
 *
 * @author LWJGL2
 */
public class VerticalLayout implements LayoutManager {

    private Dimension size = new Dimension();

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension minimumLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    @Override
    public Dimension preferredLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    @Override
    public void layoutContainer(Container container) {
        Component list[] = container.getComponents();
        int currentY = 5;
        for (int i = 0; i < list.length; i++) {
            Dimension pref = list[i].getPreferredSize();
            list[i].setBounds(5, currentY, pref.width, pref.height);
            currentY += 5;
            currentY += pref.height;
        }
    }

    private Dimension calculateBestSize(Container c) {
        Component[] list = c.getComponents();
        int maxWidth = 0;
        for (int i = 0; i < list.length; i++) {
            int width = list[i].getWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        size.width = maxWidth + 5;
        int height = 0;
        for (int i = 0; i < list.length; i++) {
            height += 5;
            height += list[i].getHeight();
        }
        size.height = height;
        return size;
    }
}
