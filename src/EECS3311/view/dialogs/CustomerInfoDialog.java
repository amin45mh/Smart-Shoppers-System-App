package EECS3311.view.dialogs;

import EECS3311.model.Location;
import EECS3311.model.users.Customer;
import EECS3311.view.InfoDialog;
import EECS3311.view.SwingUtilities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.net.URI;
import java.util.function.Consumer;

public class CustomerInfoDialog extends InfoDialog<Customer> {
    private JLabel pageTitle, response, nameLabel, emailLabel, passLabel, repassLabel, addressLabel, coordinateLabel;
    private JTextField nameTF, emailTF, locationLatTF, locationLngTF, addressTF;
    private JPasswordField passTF, repassTF;
    private TextButton findLatLong;
    private JCheckBox editPasswordCheckBox;

    public CustomerInfoDialog(JFrame parent, Customer onEditCustomer, Consumer<Customer> callBack) {
        super(parent, "Edit Customer", onEditCustomer, callBack);
    }

    @Override
    protected void initComponents() {
        pageTitle = new JLabelFont("Customer Information", Font.BOLD, 25);
        response = new JLabelFont("", Font.ITALIC, 14);
        nameLabel = new JLabel("Name :");
        emailLabel = new JLabel("Email :");
        passLabel = new JLabel("Password :");
        repassLabel = new JLabel("Re-inter Password :");
        addressLabel = new JLabel("Address :");
        coordinateLabel = new JLabel("coordinates :");

        nameTF = new JTextField(15);
        emailTF = new JTextField(15);
        locationLatTF = new JTextField(5);
        locationLngTF = new JTextField(5);
        addressTF = new JTextField(15);
        passTF = new JPasswordField(15);
        repassTF = new JPasswordField(15);

        findLatLong = new TextButton("How to find coordinates?", e -> {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://www.itilog.com/"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        nameTF.setText(object.getName());
        emailTF.setText(object.getEmail());
        addressTF.setText(object.getLocation().getLocation());
        locationLatTF.setText(String.valueOf(object.getLocation().getLat()));
        locationLngTF.setText(String.valueOf(object.getLocation().getLng()));

        editPasswordCheckBox = new JCheckBox("Edit Password");
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
    }

    @Override
    protected void initLayout() {
        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.setBorder(new EmptyBorder(10, 35, 0, 0));
        headingPanel.add(pageTitle);
        headingPanel.add(response);

        JPanel coordinates = new SequencePanel(BoxLayout.X_AXIS);
        coordinates.add(locationLatTF);
        coordinates.add(Box.createHorizontalStrut(5));
        coordinates.add(locationLngTF);
        coordinates.add(Box.createHorizontalStrut(5));
        coordinates.add(findLatLong);

        JPanel jPanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new GridLayout(8, 2, 15, 15));
        infoPanel.add(nameLabel);
        infoPanel.add(nameTF);
        infoPanel.add(emailLabel);
        infoPanel.add(emailTF);
        infoPanel.add(addressLabel);
        infoPanel.add(addressTF);
        infoPanel.add(coordinateLabel);
        infoPanel.add(coordinates);
        infoPanel.add(editPasswordCheckBox);
        infoPanel.add(Box.createHorizontalStrut(5));
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
                pass = String.valueOf(passTF.getPassword()), repass = String.valueOf(repassTF.getPassword()),
                address = addressTF.getText();
        double lat, lng;
        try {
            lat = Double.parseDouble(locationLatTF.getText());
            lng = Double.parseDouble(locationLngTF.getText());
        } catch (NumberFormatException ex) {
            response.setText("Coordinates must be floating point numbers");
            return false;
        }
        if (name.isEmpty() || email.isEmpty() || (editPasswordCheckBox.isSelected() && pass.isEmpty())) {
            response.setText("Please fill all blanks");
            return false;
        }
        if (editPasswordCheckBox.isSelected() && !pass.equals(repass)) {
            response.setText("Password doesn't match it's re-enter");
            return false;
        }


        object.setName(name);
        object.setEmail(email);
        object.setLocation(new Location(address, lat, lng));

        if(editPasswordCheckBox.isSelected())
            object.setPassword(pass);
        return true;
    }
}
