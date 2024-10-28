package EECS3311.view.pages;

import EECS3311.controller.CustomerHomePageController;
import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Customer;
import EECS3311.view.ListRenderers;
import EECS3311.view.PagePanel;
import EECS3311.view.SwingUtilities;
import EECS3311.view.ListRenderers.*;
import EECS3311.view.SwingUtilities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;


public class CustomerHomePage extends PagePanel {
    private final Customer customer;
    private final CustomerHomePageController controller;

    private JLabel nameLabel, emailLabel, prefStoresLabel, customerImage;
    private JButton logoutBtn, editBtn;
    private JList<Store> prefStoresList;
    private DefaultListModel<Store> prefStoresListModel;
    private JPanel homePanel;

    public CustomerHomePage(Customer customer, CustomerHomePageController controller) {
        this.customer = customer;
        this.controller = controller;
    }

    @Override
    protected void initComponent() {
        nameLabel = new JLabel(customer.getName());
        nameLabel.setFont(new Font("", Font.BOLD, 15));
        emailLabel = new JLabel(customer.getEmail());
        emailLabel.setFont(new Font("", Font.ITALIC, 15));

        prefStoresLabel = new JLabel("Preferred Stores");
        prefStoresLabel.setFont(new Font("", Font.BOLD, 25));
        prefStoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        customerImage = SwingUtilities.getSwingImage(220, 220, "src/resources/customer.png");
        logoutBtn = new JButton("Logout");
        editBtn = new JButton("Edit");
        logoutBtn.addActionListener(controller::logout);
        editBtn.addActionListener(controller::editProfile);

        prefStoresListModel = new DefaultListModel<>();
        for (Store s : customer.getPreferredStores())
            prefStoresListModel.addElement(s);
        prefStoresList = new JList<>(prefStoresListModel);
        prefStoresList.setCellRenderer(new ListRenderers.PreferredStoresRenderer(customer));
        prefStoresList.addListSelectionListener(controller::selectStore);
        prefStoresList.setBackground(getBackground());

        homePanel = new JPanel(new BorderLayout());
    }

    @Override
    protected void initLayout() {
        JPanel leftPanel = new SequencePanel(BoxLayout.Y_AXIS);
        customerImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(customerImage);
        leftPanel.add(nameLabel);
        leftPanel.add(emailLabel);
        JPanel acts = new JPanel(new FlowLayout());
        acts.setMaximumSize(new Dimension(10000, 100));
        acts.setBorder(new EmptyBorder(10, 10, 10, 10));
        acts.add(logoutBtn);
        acts.add(Box.createHorizontalStrut(40));
        acts.add(editBtn);

        leftPanel.add(acts);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(prefStoresLabel);
        leftPanel.add(new JScrollPane(prefStoresList));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(leftPanel);
        splitPane.add(homePanel);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    public void riseStoresPanel(Vector<Store> stores) {
        homePanel.removeAll();

        JPanelList<Store> storesJList = new JPanelList<>(new CustomerAllStoresRenderer(controller::addPreferredStore, customer));
        storesJList.addItems(stores);

        homePanel.add(SwingUtilities.getJScrollPage(storesJList, 7, 7), BorderLayout.CENTER);

        JLabel header = new JLabel(" Available Stores");
        header.setFont(new Font("", Font.BOLD, 60));
        homePanel.add(header, BorderLayout.NORTH);

        homePanel.revalidate();
        homePanel.repaint();
    }

    public void riseSelectedStorePanel(Store store) {
        homePanel.removeAll();

        //init components
        JLabel nameLabel = new JLabelFont(store.getName(), Font.PLAIN, 38);
        JLabel locationLabel = new JLabelFont("<html><b>Address:</b> <i>" + store.getLocation().getLocation() + "</i></html>", Font.PLAIN, 15);
        JLabel distanceLabel = new JLabelFont("<html><b>Distance:</b> <i>" + customer.getLocation().distance(store.getLocation()) + "</i></html>", Font.PLAIN, 15);
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("", Font.PLAIN, 25));

        Vector<String> allCategories = Category.getAllCategories();
        allCategories.insertElementAt("All Categories", 0);
        JComboBox<String> categoryCb = new JComboBox<>(allCategories);
        categoryCb.setFont(new Font("", Font.PLAIN, 15));
        categoryCb.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel shoppingListLabel = new JLabelFont("Your Shopping List:", Font.BOLD, 20);
        TextButton removeStore = new TextButton("remove this store", e -> controller.removePreferredStore(store));

        JPanelList<Item> shoppingList = new JPanelList<>();
        shoppingList.setRenderer(new CustomerShoppingListRenderer(item -> controller.removeItemFromShoppingList(store, item, shoppingList)));
        shoppingList.addItems(customer.getShoppingLists().get(store));

        JPanelList<Item> allItems = new JPanelList<>();
        allItems.setRenderer(new CustomerStoreAllItemsRenderer(item -> controller.addItemToShoppingList(store, item, shoppingList)));
        allItems.addItems(store.getItems());

        JButton getPathBtn = new JButton("Get The Path");
        getPathBtn.addActionListener(e -> controller.getPathInStore(store));

        //init layouts
        JPanel heading = new JPanel(new BorderLayout());
        heading.add(new ImageButton("src/resources/back.png", 50, 50, controller::gotoAllStores), BorderLayout.WEST);
        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(10, 0, 10, 0));
        searchBox.add(categoryCb, BorderLayout.WEST);
        searchBox.add(new ImageButton("src/resources/search.png", 50, 50, e -> controller.search(store, searchField.getText(), (String)categoryCb.getSelectedItem(), allItems)), BorderLayout.EAST);
        searchBox.add(searchField, BorderLayout.CENTER);
        JPanel search_info_panel = new SequencePanel(BoxLayout.Y_AXIS);
        search_info_panel.setBorder(new EmptyBorder(0, 15, 0, 0));
        searchBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel storeInfo = new SequencePanel(BoxLayout.Y_AXIS);
        storeInfo.add(nameLabel);
        storeInfo.add(locationLabel);
        storeInfo.add(distanceLabel);
        search_info_panel.add(storeInfo);
        search_info_panel.add(searchBox);
        heading.add(search_info_panel, BorderLayout.CENTER);

        JPanelMargin shoppingListPanel = new JPanelMargin(new BorderLayout(), 10, 10, 10, 10, true);

        SequencePanel label_pathBtn = new SequencePanel(BoxLayout.Y_AXIS);
        label_pathBtn.addAll(shoppingListLabel,
                Box.createVerticalStrut(10),
                getPathBtn,
                Box.createVerticalStrut(20));

        shoppingListPanel.add(label_pathBtn, BorderLayout.NORTH);
        shoppingListPanel.add(removeStore, BorderLayout.SOUTH);
        shoppingListPanel.add(shoppingList, BorderLayout.CENTER);
        JPanelMargin shoppingListPanelPadding = new JPanelMargin(new BorderLayout(), 10, 10, 10, 10, true);
        shoppingListPanelPadding.setBorder(BorderFactory.createRaisedBevelBorder());
        shoppingListPanelPadding.add(SwingUtilities.getJScrollPage(shoppingListPanel, 7, 7), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout());
        body.add(heading, BorderLayout.NORTH);
        body.add(SwingUtilities.getJScrollPage(allItems, 7,7), BorderLayout.CENTER);

        homePanel.add(body, BorderLayout.CENTER);
        homePanel.add(shoppingListPanelPadding, BorderLayout.EAST);
        homePanel.revalidate();
        homePanel.repaint();
    }

    public JList<Store> getPrefStoresList() {
        return prefStoresList;
    }

    public DefaultListModel<Store> getPrefStoresListModel() {
        return prefStoresListModel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }
}
