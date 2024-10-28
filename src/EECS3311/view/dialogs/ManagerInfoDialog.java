package EECS3311.view.dialogs;

import EECS3311.model.users.Manager;
import EECS3311.view.InfoDialog;
import EECS3311.view.SwingUtilities.JLabelFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;

public class ManagerInfoDialog extends InfoDialog<Manager> {
    private JLabel pageTitle, response, nameLabel, emailLabel, passLabel, repassLabel;
    private JTextField nameTF, emailTF;
    private JPasswordField passTF, repassTF;
    private JCheckBox editPasswordCheckBox;
    private boolean isEditingDialog;

    public ManagerInfoDialog(JFrame parent, Manager onEditManager, Consumer<Manager> callBack) {
        super(parent, "Edit Manager", onEditManager, callBack);
        isEditingDialog = true;
    }

    public ManagerInfoDialog(JFrame parent, int newManagerId, Consumer<Manager> callBack) {
        super(parent, "Create New Manager", new Manager(newManagerId, "","", ""), callBack);
        isEditingDialog = false;
    }

    @Override
    protected void initComponents() {
        pageTitle = new JLabelFont("Manager Information", Font.BOLD, 25);
        response = new JLabelFont("", Font.ITALIC, 14);
        nameLabel = new JLabel("Name :");
        emailLabel = new JLabel("Email :");
        passLabel = new JLabel("Password :");
        repassLabel = new JLabel("Re-inter Password :");

        nameTF = new JTextField(15);
        emailTF = new JTextField(15);
        passTF = new JPasswordField(15);
        repassTF = new JPasswordField(15);

        nameTF.setText(object.getName());
        emailTF.setText(object.getEmail());

        editPasswordCheckBox = new JCheckBox("Edit Password");
        if(isEditingDialog) {
            editPasswordCheckBox.addItemListener(c ->{
                if(c.getStateChange() == ItemEvent.SELECTED){
                    passTF.setVisible(true);
                    repassTF.setVisible(true);
                    passLabel.setVisible(true);
                    repassLabel.setVisible(true);
                }else{
                    passTF.setVisible(false);
                    repassTF.setVisible(false);
                    passLabel.setVisible(false);
                    repassLabel.setVisible(false);
                }
            });
            editPasswordCheckBox.doClick();
            editPasswordCheckBox.setSelected(false);
        }else{
            editPasswordCheckBox.setSelected(true);
        }
    }

    @Override
    protected void initLayout() {
        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setBorder(new EmptyBorder(10, 35, 0, 0));
        headingPanel.add(pageTitle);
        headingPanel.add(response);

        JPanel jPanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        infoPanel.add(nameLabel);
        infoPanel.add(nameTF);
        infoPanel.add(emailLabel);
        infoPanel.add(emailTF);
        if(isEditingDialog) {
            infoPanel.add(editPasswordCheckBox);
            infoPanel.add(Box.createHorizontalStrut(5));
        }
        infoPanel.add(passLabel);
        infoPanel.add(passTF);
        infoPanel.add(repassLabel);
        infoPanel.add(repassTF);
        jPanel.add(infoPanel);

        JPanel buttonsPane = new JPanel();
        buttonsPane.add(cancelBtn);
        buttonsPane.add(Box.createHorizontalStrut(20));
        buttonsPane.add(okBtn);

        JPanel main = new JPanel(new BorderLayout());
        main.add(headingPanel, BorderLayout.NORTH);
        main.add(jPanel, BorderLayout.CENTER);
        main.add(buttonsPane, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(main, BorderLayout.CENTER);
    }

    @Override
    protected boolean check() {
        String name = nameTF.getText(), email = emailTF.getText(),
                pass = String.valueOf(passTF.getPassword()), repass = String.valueOf(repassTF.getPassword());

        if (name.isEmpty() || email.isEmpty() ||  (editPasswordCheckBox.isSelected() && pass.isEmpty())) {
            response.setText("Please fill all blanks");
            return false;
        }
        if (editPasswordCheckBox.isSelected() && !pass.equals(repass)) {
            response.setText("Password doesn't match it's re-enter");
            return false;
        }

        object.setName(name);
        object.setEmail(email);

        if(editPasswordCheckBox.isSelected())
            object.setPassword(pass);
        return true;
    }
}
