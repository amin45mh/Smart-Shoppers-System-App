package EECS3311.controller;

import EECS3311.Application;
import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Admin;
import EECS3311.model.users.Manager;
import EECS3311.model.users.UserType;
import EECS3311.view.SwingUtilities;
import EECS3311.view.dialogs.*;
import EECS3311.view.pages.AdminHomePage;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class AdminHomePageController {
    private AdminHomePage adminHomePage;
    private Application application;
    private Admin admin;

    public AdminHomePageController(Application application, Admin admin) {
        this.application = application;
        this.admin = admin;
        this.adminHomePage = new AdminHomePage(admin, this);
        adminHomePage.init();
        adminHomePage.getAllStoresJList().setSelectedIndex(0);
        adminHomePage.getAllManagersJList().setSelectedIndex(0);
    }


    public void logout(ActionEvent e) {
        application.gotoLoginPage();
    }

    public void editProfile(ActionEvent e) {
        new AdminInfoDialog(application, admin, adminEdited -> {
            adminHomePage.getNameLabel().setText(adminEdited.getName());
            adminHomePage.getEmailLabel().setText(adminEdited.getEmail());
        }).rise();
    }


    public void newStore(ActionEvent e) {
        new StoreInfoDialog(application, application.getNewStoreID(), newStore ->{
            getAllStores().add(newStore);
            adminHomePage.getAllStoresListModel().addElement(newStore);
        }).rise();
    }

    public void removeStoreCompletely(Store store) {
        getAllStores().remove(store);
        if (getAllStores().size() == 0)
            adminHomePage.riseSelectedStorePanel(null);

        adminHomePage.getAllStoresListModel().removeElement(store);
        adminHomePage.getAllStoresJList().setSelectedIndex(0);
    }

    public void editStore(Store store) {
        new StoreInfoDialog(application, store, editedStore ->{
            adminHomePage.riseSelectedStorePanel(editedStore);
            adminHomePage.revalidate();
            adminHomePage.repaint();
        }).rise();
    }

    public void newItemForStore(Store store, SwingUtilities.JPanelList<Item> UI_ItemsList) {
        new ItemInfoDialog(application, application.getNewItemID(), newItem->{
            store.addItem(newItem);
            UI_ItemsList.addItem(newItem);
            adminHomePage.revalidate();
            adminHomePage.repaint();
        }).rise();
    }

    public void editItemInStore(Store store, Item item) {
        new ItemInfoDialog(application, item, newItem->{
            adminHomePage.riseSelectedStorePanel(store);
        }).rise();
    }

    public void removeItemFromStore(Store store, Item item, SwingUtilities.JPanelList<Item> UI_itemsList) {
        store.getItems().remove(item);
        UI_itemsList.clearAndReset();
        UI_itemsList.addItems(store.getItems());
        adminHomePage.revalidate();
        adminHomePage.repaint();
    }


    public void newManager(ActionEvent e) {
        new ManagerInfoDialog(application, application.getNewUserID(UserType.Manager), newManager -> {
            getAllManagers().add(newManager);
            adminHomePage.getAllManagersListModel().addElement(newManager);
        }).rise();
    }

    public void removeManagerCompletely(Manager manager) {
        getAllManagers().remove(manager);
        if (getAllManagers().size() == 0)
            adminHomePage.riseSelectedManagerPanel(null);

        adminHomePage.getAllManagersListModel().removeElement(manager);
        adminHomePage.getAllManagersJList().setSelectedIndex(0);
    }

    public void editManager(Manager manager) {
        new ManagerInfoDialog(application, manager, editedManager -> {
            adminHomePage.riseSelectedManagerPanel(editedManager);
            adminHomePage.revalidate();
            adminHomePage.repaint();
        }).rise();
    }

    public void newStoreForManager(Manager manager, SwingUtilities.JPanelList<Store> UI_StoresList) {
        SelectStoreDialog.rise(application, getAllStores(), store -> {
            if(store != null){
                if (!manager.getStores().contains(store)) {
                    manager.addStore(store);
                    UI_StoresList.addItem(store);
                }
            }
        });
    }

    public void removeStoreFromManager(Manager manager, Store removeStore, SwingUtilities.JPanelList<Store> UI_StoresList) {
        manager.getStores().remove(removeStore);
        UI_StoresList.clearAndReset();
        UI_StoresList.addItems(manager.getStores());
        adminHomePage.revalidate();
        adminHomePage.repaint();
    }


    public void selectStore(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = adminHomePage.getAllStoresJList().getSelectedIndex();
            if (index >= 0)
                adminHomePage.riseSelectedStorePanel(getAllStores().get(index));
        }
    }

    public void selectManager(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = adminHomePage.getAllManagersJList().getSelectedIndex();
            if (index >= 0)
                adminHomePage.riseSelectedManagerPanel(getAllManagers().get(index));
        }
    }

    public void search(Store store, String txt, String category, SwingUtilities.JPanelList<Item> UI_ItemsList) {
        UI_ItemsList.clearAndReset();
        for (Item item : store.getItems()) {
            if (category.equals("All Categories") || category.equals(item.getCategory().name())) {
                if (txt.isEmpty() || item.getName().contains(txt))
                    UI_ItemsList.addItem(item);
            }
        }
        adminHomePage.revalidate();
        adminHomePage.repaint();
    }


    public AdminHomePage getAdminHomePage() {
        return adminHomePage;
    }

    public Vector<Store> getAllStores() {
        return application.getAllStores();
    }

    public Vector<Manager> getAllManagers() {
        return application.getAllManagers();
    }
}
