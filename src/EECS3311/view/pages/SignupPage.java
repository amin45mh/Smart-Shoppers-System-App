package EECS3311.view.pages;

import EECS3311.controller.SignupPageController;
import EECS3311.view.PagePanel;
import EECS3311.view.SwingUtilities.SequencePanel;
import EECS3311.view.SwingUtilities.TextButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SignupPage extends PagePanel {
    private SignupPageController controller;
    private JLabel pageLabel, nameLabel, emailLabel, passLabel, repassLabel, response;
    private JTextField emailTF, nameTF;
    private JPasswordField passTF, repassTF;
    private JButton signupButton;
    private TextButton loginButton;

    public SignupPage(SignupPageController controller){
        this.controller = controller;
    }

    @Override
    protected void initComponent() {
        pageLabel = new JLabel("Signup Customer");
        pageLabel.setFont(new Font("", Font.BOLD, 35));

        response = new JLabel("");
        response.setFont(new Font("", Font.PLAIN, 18));

        nameLabel = new JLabel("Name :");
        emailLabel = new JLabel("Email :");
        passLabel = new JLabel("Password :");
        repassLabel = new JLabel("Re-enter:");
        emailTF = new JTextField(10);
        nameTF = new JTextField(10);
        passTF = new JPasswordField(10);
        repassTF = new JPasswordField(10);

        signupButton = new JButton("Signup");
        loginButton = new TextButton("I have an account", controller::login);

        signupButton.addActionListener(controller::signup);
    }

    @Override
    protected void initLayout() {
        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setBorder(new EmptyBorder(10, 35, 0, 0));
        headingPanel.add(pageLabel);
        headingPanel.add(response);

        JPanel jPanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        infoPanel.add(nameLabel);
        infoPanel.add(nameTF);
        infoPanel.add(emailLabel);
        infoPanel.add(emailTF);
        infoPanel.add(passLabel);
        infoPanel.add(passTF);
        infoPanel.add(repassLabel);
        infoPanel.add(repassTF);
        jPanel.add(infoPanel);

        JPanel buttonsPane = new JPanel();
        buttonsPane.add(signupButton);
        buttonsPane.add(Box.createHorizontalStrut(20));
        buttonsPane.add(loginButton);

        JPanel main = new JPanel(new GridLayout(3, 1));
        main.add(headingPanel);
        main.add(jPanel);
        main.add(buttonsPane);

        setLayout(new BorderLayout());
        add(main, BorderLayout.CENTER);
    }

    public void setResponseText(String txt){
        response.setText(txt);
    }

    public String[] getValues() {
        return new String[]{
                emailTF.getText(),
                nameTF.getText(),
                String.valueOf(passTF.getPassword()),
                String.valueOf(repassTF.getPassword())
        };
    }
}
