package EECS3311.controller;

import EECS3311.Application;
import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Manager;
import EECS3311.view.SwingUtilities;
import EECS3311.view.dialogs.ItemInfoDialog;
import EECS3311.view.dialogs.ManagerInfoDialog;
import EECS3311.view.dialogs.StoreInfoDialog;
import EECS3311.view.pages.ManagerHomePage;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class ManagerHomePageController {
    private ManagerHomePage managerHomePage;
    private Application application;
    private Manager manager;

    public ManagerHomePageController(Application application, Manager manager) {
        this.application = application;
        this.manager = manager;
        managerHomePage = new ManagerHomePage(manager, this);
        managerHomePage.init();
        managerHomePage.getAllStoresJList().setSelectedIndex(0);
    }


    public void logout(ActionEvent e) {
        application.gotoLoginPage();
    }

    public void editProfile(ActionEvent e) {
        new ManagerInfoDialog(application, manager, editedManager -> {
            managerHomePage.getNameLabel().setText(editedManager.getName());
            managerHomePage.getEmailLabel().setText(editedManager.getEmail());
        }).rise();
    }

    public void editStore(Store store) {
        new StoreInfoDialog(application, store, editedStore ->{
            managerHomePage.riseSelectedStorePanel(editedStore);
            managerHomePage.revalidate();
            managerHomePage.repaint();
        }).rise();
    }

    public void newItemForStore(Store store, SwingUtilities.JPanelList<Item> UI_ItemsList) {
        new ItemInfoDialog(application, application.getNewItemID(), newItem->{
            store.addItem(newItem);
            UI_ItemsList.addItem(newItem);
            managerHomePage.revalidate();
            managerHomePage.repaint();
        }).rise();
    }

    public void editItemInStore(Store store, Item item) {
        new ItemInfoDialog(application, item, newItem->{
            managerHomePage.riseSelectedStorePanel(store);
        }).rise();
    }

    public void removeItemFromStore(Store store, Item item, SwingUtilities.JPanelList<Item> UI_itemsList) {
        store.getItems().remove(item);
        UI_itemsList.clearAndReset();
        UI_itemsList.addItems(store.getItems());
        managerHomePage.revalidate();
        managerHomePage.repaint();
    }


    public void selectStore(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = managerHomePage.getAllStoresJList().getSelectedIndex();
            if (index >= 0)
                managerHomePage.riseSelectedStorePanel(manager.getStores().get(index));
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
        managerHomePage.revalidate();
        managerHomePage.repaint();
    }


    public ManagerHomePage getManagerHomePage() {
        return managerHomePage;
    }

    public Vector<Store> getAllStores() {
        return application.getAllStores();
    }

}
