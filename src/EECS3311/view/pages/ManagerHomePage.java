package EECS3311.view.pages;

import EECS3311.controller.ManagerHomePageController;
import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Manager;
import EECS3311.view.ListRenderers;
import EECS3311.view.PagePanel;
import EECS3311.view.SwingUtilities;
import EECS3311.view.SwingUtilities.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

public class ManagerHomePage extends PagePanel {
    private final Manager manager;
    private final ManagerHomePageController controller;

    private JLabel pageLabel, nameLabel, emailLabel, adminImage;
    private JButton logoutBtn, editBtn;

    private JList<Store> allStoresJList;
    private DefaultListModel<Store> allStoresListModel;
    private JPanel selectedStorePanel;

    public ManagerHomePage(Manager manager, ManagerHomePageController controller) {
        this.manager = manager;
        this.controller = controller;
    }


    @Override
    protected void initComponent() {
        pageLabel = new JLabel("Manager Panel");
        pageLabel.setFont(new Font("", Font.PLAIN, 22));
        nameLabel = new JLabel(manager.getName());
        nameLabel.setFont(new Font("", Font.BOLD, 15));
        emailLabel = new JLabel(manager.getEmail());
        emailLabel.setFont(new Font("", Font.ITALIC, 15));
        adminImage = EECS3311.view.SwingUtilities.getSwingImage(150, 150, "src/resources/manager.png");
        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(controller::logout);
        editBtn = new JButton("Edit");
        editBtn.addActionListener(controller::editProfile);

        allStoresListModel = new DefaultListModel<>();
        allStoresJList = new JList<>(allStoresListModel);
        allStoresJList.setCellRenderer(new ListRenderers.Manager_Admin_AllStoresRenderer());
        allStoresJList.setBackground(getBackground());
        allStoresJList.addListSelectionListener(controller::selectStore);

        for (Store store : manager.getStores())
            allStoresListModel.addElement(store);

        selectedStorePanel = new JPanel(new BorderLayout());
    }

    @Override
    protected void initLayout() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new EECS3311.view.SwingUtilities.SequencePanel(BoxLayout.Y_AXIS);
        pageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(pageLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(nameLabel);
        infoPanel.add(emailLabel);
        JPanel acts = new JPanel(new FlowLayout());
        acts.add(logoutBtn);
        acts.add(Box.createHorizontalStrut(40));
        acts.add(editBtn);
        infoPanel.add(acts);

        JPanel heading = new JPanel();
        heading.add(adminImage);
        heading.add(infoPanel);
        JPanel panelHeading = new JPanel(new BorderLayout());
        panelHeading.add(heading, BorderLayout.WEST);

        JPanel mainStoreManagementPanel = new JPanel(new BorderLayout());
        JPanel allStoresPanel = new JPanel(new BorderLayout());
        allStoresPanel.add(EECS3311.view.SwingUtilities.getJScrollPage(allStoresJList, 7, 7), BorderLayout.CENTER);
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane1.add(allStoresPanel);
        splitPane1.add(selectedStorePanel);
        splitPane1.setDividerLocation(250);
        mainStoreManagementPanel.add(splitPane1);

        add(panelHeading, BorderLayout.NORTH);
        add(mainStoreManagementPanel, BorderLayout.CENTER);
    }

    public void riseSelectedStorePanel(Store store) {
        selectedStorePanel.removeAll();
        if (store == null) {
            selectedStorePanel.revalidate();
            selectedStorePanel.repaint();
            return;
        }

        //init components
        JLabel nameLabel = new JLabelFont(store.getName(), Font.PLAIN, 38);
        JLabel locationLabel = new JLabelFont("<html><b>Address:</b> <i>" + store.getLocation().getLocation() + "</i></html>", Font.PLAIN, 15);
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("", Font.PLAIN, 25));
        Vector<String> allCategories = Category.getAllCategories();
        allCategories.insertElementAt("All Categories", 0);
        JComboBox<String> categoryCb = new JComboBox<>(allCategories);
        categoryCb.setFont(new Font("", Font.PLAIN, 15));
        categoryCb.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanelList<Item> allItems = new JPanelList<>();
        allItems.setRenderer(new ListRenderers.Manager_Admin_AllItemsRenderer(
                removeItem -> controller.removeItemFromStore(store, removeItem, allItems),
                editItem -> controller.editItemInStore(store, editItem)
        ));
        allItems.addItems(store.getItems());

        JButton editStore = new JButton("edit this store");
        JButton addItem = new JButton("+ Add New Item");
        editStore.addActionListener(e -> controller.editStore(store));
        addItem.addActionListener(e -> controller.newItemForStore(store, allItems));

        //init layouts
        JPanel heading = new JPanel(new BorderLayout());
        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(10, 0, 10, 0));
        searchBox.add(categoryCb, BorderLayout.WEST);
        searchBox.add(new ImageButton("src/resources/search.png", 50, 50, e -> controller.search(store, searchField.getText(), (String) categoryCb.getSelectedItem(), allItems)), BorderLayout.EAST);
        searchBox.add(searchField, BorderLayout.CENTER);
        searchBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel search_info_panel = new SequencePanel(BoxLayout.Y_AXIS);
        search_info_panel.setBorder(new EmptyBorder(0, 15, 0, 0));

        JPanel acts_info_panel = new JPanel(new BorderLayout());
        acts_info_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel actInfo = new SequencePanel(BoxLayout.Y_AXIS);
        actInfo.setBorder(new EmptyBorder(10, 10, 5, 10));
        editStore.setAlignmentX(Component.RIGHT_ALIGNMENT);
        addItem.setAlignmentX(Component.RIGHT_ALIGNMENT);
        actInfo.add(editStore);
        actInfo.add(Box.createVerticalStrut(25));
        actInfo.add(addItem);
        JPanel storeInfo = new SequencePanel(BoxLayout.Y_AXIS);
        storeInfo.add(nameLabel);
        storeInfo.add(locationLabel);
        acts_info_panel.add(storeInfo, BorderLayout.WEST);
        acts_info_panel.add(actInfo, BorderLayout.EAST);

        search_info_panel.add(acts_info_panel);
        search_info_panel.add(searchBox);
        heading.add(search_info_panel, BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout());
        body.add(heading, BorderLayout.NORTH);
        body.add(SwingUtilities.getJScrollPage(allItems, 7, 7), BorderLayout.CENTER);

        selectedStorePanel.add(body, BorderLayout.CENTER);
        selectedStorePanel.revalidate();
        selectedStorePanel.repaint();
    }


    public JList<Store> getAllStoresJList() {
        return allStoresJList;
    }

    public DefaultListModel<Store> getAllStoresListModel() {
        return allStoresListModel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }
}
