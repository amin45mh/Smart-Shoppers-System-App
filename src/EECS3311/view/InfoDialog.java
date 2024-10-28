package EECS3311.view;

import EECS3311.model.users.Customer;

import javax.swing.*;
import java.util.function.Consumer;

public abstract class InfoDialog<T> extends JDialog {
    protected T object;
    protected Consumer<T> callBack;
    protected JButton okBtn, cancelBtn;

    public InfoDialog(JFrame parent, String title, T object, Consumer<T> callBack){
        super(parent, title, true);
        setLocation(parent.getLocation().x+ 100, parent.getLocation().y+100);
        this.object = object;
        this.callBack = callBack;
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            if(check()){
                callBack.accept(object);
                dispose();
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }

    protected abstract boolean check();

    protected abstract void initComponents();

    protected abstract void initLayout();

    public void rise(){
        initComponents();
        initLayout();

        pack();
        setResizable(false);
        setVisible(true);
    }
}
