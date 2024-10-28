package EECS3311.view.pages;

import EECS3311.controller.LoginPageController;
import EECS3311.view.PagePanel;
import EECS3311.view.SwingUtilities.SequencePanel;
import EECS3311.view.SwingUtilities.TextButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPage extends PagePanel {
    private LoginPageController controller;
    private JLabel pageLabel, roleLabel, emailLabel, passLabel, response;
    private JTextField emailTF;
    private JPasswordField passTF;
    private JComboBox roleCB;
    private JButton loginButton;
    protected TextButton signupButton;

    public LoginPage(LoginPageController controller) {
        this.controller = controller;
    }

    @Override
    protected void initComponent() {
        pageLabel = new JLabel("Login");
        pageLabel.setFont(new Font("", Font.BOLD, 35));

        response = new JLabel("");
        response.setFont(new Font("", Font.PLAIN, 18));

        roleLabel = new JLabel("Role :");
        emailLabel = new JLabel("Email :");
        passLabel = new JLabel("Password :");
        emailTF = new JTextField(10);
        passTF = new JPasswordField(10);
        roleCB = new JComboBox<>(new String[]{"Customer", "Manager", "Administrator"});

        loginButton = new JButton("Login");
        signupButton = new TextButton("create new account", controller::signup);

        loginButton.addActionListener(controller::login);
    }

    @Override
    protected void initLayout() {
        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setBorder(new EmptyBorder(10, 35, 0, 0));
        headingPanel.add(pageLabel);
        headingPanel.add(response);

        JPanel jPanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        infoPanel.add(roleLabel);
        infoPanel.add(roleCB);
        infoPanel.add(emailLabel);
        infoPanel.add(emailTF);
        infoPanel.add(passLabel);
        infoPanel.add(passTF);
        jPanel.add(infoPanel);

        JPanel buttonsPane = new JPanel();
        buttonsPane.add(loginButton);
        buttonsPane.add(Box.createHorizontalStrut(20));
        buttonsPane.add(signupButton);

        JPanel main = new JPanel(new GridLayout(3, 1));
        main.add(headingPanel);
        main.add(jPanel);
        main.add(buttonsPane);

        setLayout(new BorderLayout());
        add(main, BorderLayout.CENTER);
    }

    public void setResponseText(String txt) {
        response.setText(txt);
    }

    public String[] getValues() {
        return new String[]{
                (String) roleCB.getSelectedItem(),
                emailTF.getText(),
                String.valueOf(passTF.getPassword())
        };
    }
}
