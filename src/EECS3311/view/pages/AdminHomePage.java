package EECS3311.view.pages;

import EECS3311.controller.AdminHomePageController;
import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Admin;
import EECS3311.model.users.Manager;
import EECS3311.view.ListRenderers.*;
import EECS3311.view.PagePanel;
import EECS3311.view.SwingUtilities.*;
import EECS3311.view.SwingUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class AdminHomePage extends PagePanel {
    private final Admin admin;
    private final AdminHomePageController controller;

    private JLabel pageLabel, nameLabel, emailLabel, adminImage;
    private JButton logoutBtn, editBtn, addNewStore, addNewManager;
    private JTabbedPane tabbedPane;

    private JList<Store> allStoresJList;
    private JList<Manager> allManagersJList;
    private DefaultListModel<Store> allStoresListModel;
    private DefaultListModel<Manager> allManagersListModel;
    private JPanel selectedStorePanel, selectedManagerPanel;

    public AdminHomePage(Admin admin, AdminHomePageController controller) {
        this.admin = admin;
        this.controller = controller;
    }

    @Override
    protected void initComponent() {
        pageLabel = new JLabel("Admin Panel");
        pageLabel.setFont(new Font("", Font.PLAIN, 22));
        nameLabel = new JLabel(admin.getName());
        nameLabel.setFont(new Font("", Font.BOLD, 15));
        emailLabel = new JLabel(admin.getEmail());
        emailLabel.setFont(new Font("", Font.ITALIC, 15));
        adminImage = SwingUtilities.getSwingImage(150, 150, "src/resources/admin.png");
        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(controller::logout);
        editBtn = new JButton("Edit");
        editBtn.addActionListener(controller::editProfile);
        tabbedPane = new JTabbedPane();
        addNewStore = new JButton("+ New Store");
        addNewStore.addActionListener(controller::newStore);
        addNewManager = new JButton("+ New Manager");
        addNewManager.addActionListener(controller::newManager);

        allStoresListModel = new DefaultListModel<>();
        allManagersListModel = new DefaultListModel<>();
        allStoresJList = new JList<>(allStoresListModel);
        allManagersJList = new JList<>(allManagersListModel);
        allStoresJList.setCellRenderer(new Manager_Admin_AllStoresRenderer());
        allManagersJList.setCellRenderer(new Admin_AllManagersRenderer());
        allStoresJList.setBackground(getBackground());
        allManagersJList.setBackground(getBackground());
        allStoresJList.addListSelectionListener(controller::selectStore);
        allManagersJList.addListSelectionListener(controller::selectManager);

        for (Store store : controller.getAllStores())
            allStoresListModel.addElement(store);

        for (Manager manager : controller.getAllManagers())
            allManagersListModel.addElement(manager);

        selectedStorePanel = new JPanel(new BorderLayout());
        selectedManagerPanel = new JPanel(new BorderLayout());
    }

    @Override
    protected void initLayout() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new SequencePanel(BoxLayout.Y_AXIS);
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

        //stores_management layout:
        JPanel mainStoreManagementPanel = new JPanel(new BorderLayout());
        JPanel allStoresPanel = new JPanel(new BorderLayout());
        allStoresPanel.add(SwingUtilities.getJScrollPage(allStoresJList, 7, 7), BorderLayout.CENTER);
        JPanel marginNewStoreBtn = new JPanelMargin(new BorderLayout(), 10, 10, 10, 13, true);
        marginNewStoreBtn.add(addNewStore);
        allStoresPanel.add(marginNewStoreBtn, BorderLayout.NORTH);
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane1.add(allStoresPanel);
        splitPane1.add(selectedStorePanel);
        splitPane1.setDividerLocation(250);
        mainStoreManagementPanel.add(splitPane1);

        //managers_management layout:
        JPanel mainManagerManagementPanel = new JPanel(new BorderLayout());
        JPanel allManagersPanel = new JPanel(new BorderLayout());
        allManagersPanel.add(SwingUtilities.getJScrollPage(allManagersJList, 7, 7), BorderLayout.CENTER);
        JPanel marginNewManagerBtn = new JPanelMargin(new BorderLayout(), 10, 10, 10, 13, true);
        marginNewManagerBtn.add(addNewManager);
        allManagersPanel.add(marginNewManagerBtn, BorderLayout.NORTH);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane2.add(allManagersPanel);
        splitPane2.add(selectedManagerPanel);
        splitPane2.setDividerLocation(250);
        mainManagerManagementPanel.add(splitPane2);

        //Tabs layout:
        JPanel mainBody = new JPanel(new BorderLayout());
        tabbedPane.addTab("Mange Stores", null, mainStoreManagementPanel, "Tab 1 tooltip");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("Mange Mangers", null, mainManagerManagementPanel, "Tab 2 tooltip");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.setFont(new Font("", Font.BOLD, 18));
        mainBody.add(tabbedPane);

        add(panelHeading, BorderLayout.NORTH);
        add(mainBody, BorderLayout.CENTER);
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
        allItems.setRenderer(new Manager_Admin_AllItemsRenderer(
                removeItem -> controller.removeItemFromStore(store, removeItem, allItems),
                editItem -> controller.editItemInStore(store, editItem)
        ));
        allItems.addItems(store.getItems());

        JButton removeStore = new JButton("remove this store");
        JButton editStore = new JButton("edit this store");
        JButton addItem = new JButton("+ Add New Item");
        removeStore.addActionListener(e -> controller.removeStoreCompletely(store));
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
        removeStore.setAlignmentX(Component.RIGHT_ALIGNMENT);
        editStore.setAlignmentX(Component.RIGHT_ALIGNMENT);
        addItem.setAlignmentX(Component.RIGHT_ALIGNMENT);
        actInfo.add(removeStore);
        actInfo.add(Box.createVerticalStrut(10));
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


    public void riseSelectedManagerPanel(Manager manager) {
        selectedManagerPanel.removeAll();
        if (manager == null) {
            selectedManagerPanel.revalidate();
            selectedManagerPanel.repaint();
            return;
        }

        JLabel nameLabel = new JLabelFont(manager.getName(), Font.PLAIN, 38);
        JLabel emailLabel = new JLabelFont(manager.getEmail(), Font.ITALIC, 18);
        JLabel underManageLabel = new JLabelFont("Under Control Stores:", Font.BOLD, 22);
        JButton removeManager = new JButton("remove manager");
        JButton editManager = new JButton("edit manager");
        JButton addStore = new JButton("+ Add New Store");

        JPanelList<Store> underManageStores = new JPanelList<>();
        underManageStores.setRenderer(new Admin_UnderManageStoresRenderer(
                removeStore -> controller.removeStoreFromManager(manager, removeStore, underManageStores)
        ));
        underManageStores.addItems(manager.getStores());

        removeManager.addActionListener(e -> controller.removeManagerCompletely(manager));
        editManager.addActionListener(e -> controller.editManager(manager));
        addStore.addActionListener(e -> controller.newStoreForManager(manager, underManageStores));

        JPanel infoPanel = new JPanel(new BorderLayout());
        JPanel p1 = new SequencePanel(BoxLayout.Y_AXIS);
        p1.add(nameLabel);
        p1.add(emailLabel);
        JPanel actInfo = new SequencePanel(BoxLayout.Y_AXIS);
        actInfo.setBorder(new EmptyBorder(5, 0, 0, 5));
        removeManager.setAlignmentX(Component.RIGHT_ALIGNMENT);
        editManager.setAlignmentX(Component.RIGHT_ALIGNMENT);
        actInfo.add(removeManager);
        actInfo.add(Box.createVerticalStrut(10));
        actInfo.add(editManager);
        infoPanel.add(p1, BorderLayout.WEST);
        infoPanel.add(actInfo, BorderLayout.EAST);

        JPanel label_addBtnPanel = new JPanel(new BorderLayout());
        label_addBtnPanel.add(underManageLabel, BorderLayout.WEST);
        label_addBtnPanel.setBorder(new EmptyBorder(20, 5, 0, 0));
        JPanel p0 = new JPanel();
        p0.add(addStore);
        label_addBtnPanel.add(p0, BorderLayout.EAST);

        JPanel headingPanel = new SequencePanel(BoxLayout.Y_AXIS);
        headingPanel.setBorder(new EmptyBorder(5, 15, 5, 5));
        headingPanel.add(infoPanel);
        headingPanel.add(label_addBtnPanel);

        JPanel body = new JPanel(new BorderLayout());
        body.add(headingPanel, BorderLayout.NORTH);
        body.add(SwingUtilities.getJScrollPage(underManageStores, 7, 7), BorderLayout.CENTER);

        selectedManagerPanel.add(body, BorderLayout.CENTER);
        selectedManagerPanel.revalidate();
        selectedManagerPanel.repaint();
    }

    public JList<Store> getAllStoresJList() {
        return allStoresJList;
    }

    public JList<Manager> getAllManagersJList() {
        return allManagersJList;
    }

    public DefaultListModel<Store> getAllStoresListModel() {
        return allStoresListModel;
    }

    public DefaultListModel<Manager> getAllManagersListModel() {
        return allManagersListModel;
    }

    public JPanel getSelectedManagerPanel() {
        return selectedManagerPanel;
    }

    public JPanel getSelectedStorePanel() {
        return selectedStorePanel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

}
