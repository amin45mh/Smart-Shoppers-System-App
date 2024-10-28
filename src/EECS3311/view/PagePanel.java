package EECS3311.view;

import javax.swing.*;
import java.awt.*;

public abstract class PagePanel extends JPanel {
    public void init(){
        initComponent();
        initLayout();
    }

    protected abstract void initComponent() ;

    protected abstract void initLayout();

    public void addAll(Component...components){
        for(Component com : components){
            add(com);
        }
    }
}
