package EECS3311.view.dialogs;

import EECS3311.model.Location;
import EECS3311.model.Store;
import EECS3311.view.InfoDialog;
import EECS3311.view.SwingUtilities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URI;
import java.util.function.Consumer;

public class StoreInfoDialog extends InfoDialog<Store> {
    private JLabel pageTitle, response, nameLabel, addressLabel, coordinateLabel;
    private JTextField nameTF, locationLatTF, locationLngTF, addressTF;
    private TextButton findLatLong;

    public StoreInfoDialog(JFrame parent, Store onEditStore, Consumer<Store> callBack) {
        super(parent, "Edit Store", onEditStore, callBack);
    }

    public StoreInfoDialog(JFrame parent, int newStoreId, Consumer<Store> callBack) {
        super(parent, "Create New Store", new Store("", new Location("", 0,0), newStoreId), callBack);
    }

    @Override
    protected void initComponents() {
        pageTitle = new JLabelFont("Store Information", Font.BOLD, 25);
        response = new JLabelFont("", Font.ITALIC, 14);
        nameLabel = new JLabel("Name :");
        addressLabel = new JLabel("Address :");
        coordinateLabel = new JLabel("coordinates :");

        nameTF = new JTextField(15);
        locationLatTF = new JTextField(5);
        locationLngTF = new JTextField(5);
        addressTF = new JTextField(15);

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
        addressTF.setText(object.getLocation().getLocation());
        locationLatTF.setText(String.valueOf(object.getLocation().getLat()));
        locationLngTF.setText(String.valueOf(object.getLocation().getLng()));
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
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        infoPanel.add(nameLabel);
        infoPanel.add(nameTF);
        infoPanel.add(addressLabel);
        infoPanel.add(addressTF);
        infoPanel.add(coordinateLabel);
        infoPanel.add(coordinates);
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
        String name = nameTF.getText(), address = addressTF.getText();
        double lat, lng;
        try {
            lat = Double.parseDouble(locationLatTF.getText());
            lng = Double.parseDouble(locationLngTF.getText());
        } catch (NumberFormatException ex) {
            response.setText("Coordinates must be floating point numbers");
            return false;
        }
        if (name.isEmpty() || address.isEmpty()) {
            response.setText("Please fill all blanks");
            return false;
        }

        object.setName(name);
        object.setLocation(new Location(address, lat, lng));
        return true;
    }
}
