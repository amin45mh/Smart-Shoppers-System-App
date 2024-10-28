package EECS3311.view.dialogs;

import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Location;
import EECS3311.view.InfoDialog;
import EECS3311.view.SwingUtilities.JLabelFont;
import EECS3311.view.SwingUtilities.SequencePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class ItemInfoDialog extends InfoDialog<Item> {
    private JLabel pageTitle, response, nameLabel, descriptionLabel, sizeLabel, priceLabel, availabilityLabel, categoryLabel, addressLabel, coordinateLabel;
    private JTextField nameTF, descriptionTF, sizeTF, priceTF, locationLatTF, locationLngTF, addressTF;
    private JComboBox<String> availabilityCB, categoryCB;


    public ItemInfoDialog(JFrame parent, Item onEditItem, Consumer<Item> callBack) {
        super(parent, "Edit Item", onEditItem, callBack);
    }

    public ItemInfoDialog(JFrame parent, int newItemID, Consumer<Item> callBack) {
        super(parent, "Create New Item", new Item(newItemID, "", "", " kg", 0, false, new Location("", 0, 0), Category.Drinks), callBack);
    }

    @Override
    protected void initComponents() {
        pageTitle = new JLabelFont("Item information", Font.BOLD, 25);
        response = new JLabelFont("", Font.ITALIC, 14);
        nameLabel = new JLabel("Name :");
        descriptionLabel = new JLabel("Description :");
        sizeLabel = new JLabel("Size :");
        priceLabel = new JLabel("Price($) :");
        availabilityLabel = new JLabel("Availability :");
        categoryLabel = new JLabel("Category :");
        addressLabel = new JLabel("Address :");
        coordinateLabel = new JLabel("coordinates :");

        nameTF = new JTextField(15);
        descriptionTF = new JTextField(15);
        sizeTF = new JTextField(15);
        priceTF = new JTextField(15);
        locationLatTF = new JTextField(5);
        locationLngTF = new JTextField(5);
        addressTF = new JTextField(15);

        categoryCB = new JComboBox<>(Category.getAllCategories());
        availabilityCB = new JComboBox<>(new String[]{"Is Available", "Unavailable"});

        nameTF.setText(object.getName());
        descriptionTF.setText(object.getDescription());
        addressTF.setText(object.getLocationInStore().getLocation());
        sizeTF.setText(object.getSize());
        priceTF.setText(String.valueOf(object.getPrice()));
        locationLatTF.setText(String.valueOf(object.getLocationInStore().getLat()));
        locationLngTF.setText(String.valueOf(object.getLocationInStore().getLng()));

        availabilityCB.setSelectedIndex(object.isAvailable() ? 0 : 1);
        categoryCB.setSelectedItem(object.getCategory().name());
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
        coordinates.add(Box.createHorizontalStrut(25));

        JPanel jPanel = new JPanel(new FlowLayout());
        JPanel infoPanel = new JPanel(new GridLayout(8, 2, 15, 15));
        infoPanel.add(nameLabel);
        infoPanel.add(nameTF);
        infoPanel.add(descriptionLabel);
        infoPanel.add(descriptionTF);
        infoPanel.add(sizeLabel);
        infoPanel.add(sizeTF);
        infoPanel.add(priceLabel);
        infoPanel.add(priceTF);
        infoPanel.add(availabilityLabel);
        infoPanel.add(availabilityCB);
        infoPanel.add(categoryLabel);
        infoPanel.add(categoryCB);
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
        String name = nameTF.getText(), desc = descriptionTF.getText(),
                size = sizeTF.getText(), address = addressTF.getText();
        double lat, lng, price;
        try {
            lat = Double.parseDouble(locationLatTF.getText());
            lng = Double.parseDouble(locationLngTF.getText());
        } catch (NumberFormatException ex) {
            response.setText("Coordinates must be valid floating point numbers");
            return false;
        }

        try {
            price = Double.parseDouble(priceTF.getText());
        } catch (NumberFormatException ex) {
            response.setText("Price must be valid floating point number");
            return false;
        }

        if (name.isEmpty() || desc.isEmpty() || size.isEmpty() || address.isEmpty()) {
            response.setText("Please fill all blanks");
            return false;
        }


        object.setName(name);
        object.setDescription(desc);
        object.setSize(size);
        object.setPrice(price);
        object.setCategory(Category.valueOf((String) categoryCB.getSelectedItem()));
        object.setAvailability(availabilityCB.getSelectedIndex() == 0);
        object.setLocationInStore(new Location(address, lat, lng));
        return true;
    }
}
