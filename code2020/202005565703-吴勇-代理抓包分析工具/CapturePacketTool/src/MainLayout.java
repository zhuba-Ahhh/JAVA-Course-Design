import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;

public class MainLayout implements LayoutManager2 {

    List<Component> list = new ArrayList<>();
    @Override
    public void addLayoutComponent(String name, Component comp) {
        list.add(comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        list.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }
    final int toolBarHeight = 30;
    final int searchBarHeight = 30;
    final int footBarHeight = 20;
    @Override
    public void layoutContainer(Container parent) {
        int width = parent.getWidth();
        int height = parent.getHeight();
        
        list.get(0).setBounds(0, 0, width, toolBarHeight);
        list.get(1).setBounds(0, toolBarHeight, width, searchBarHeight);
        list.get(2).setBounds(0, toolBarHeight + searchBarHeight, width, height - toolBarHeight - searchBarHeight - footBarHeight);
        list.get(3).setBounds(0, height - 30, width, 30);
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        list.add(comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }
    
}