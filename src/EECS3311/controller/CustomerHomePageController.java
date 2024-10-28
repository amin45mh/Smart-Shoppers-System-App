package EECS3311.controller;

import EECS3311.Application;
import EECS3311.model.Item;
import EECS3311.model.Store;
import EECS3311.model.users.Customer;
import EECS3311.view.SwingUtilities;
import EECS3311.view.dialogs.CustomerInfoDialog;
import EECS3311.view.dialogs.PathInStoreDialog;
import EECS3311.view.pages.CustomerHomePage;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Vector;

public class CustomerHomePageController {
    private CustomerHomePage customerHomePage;
    private Application application;
    private Customer customer;

    public CustomerHomePageController(Application application, Customer customer) {
        this.application = application;
        this.customer = customer;
        this.customerHomePage = new CustomerHomePage(customer, this);
        customerHomePage.init();
        gotoAllStores(null);
    }

    public CustomerHomePage getCustomerHomePage() {
        return customerHomePage;
    }

    public void logout(ActionEvent e) {
        application.gotoLoginPage();
    }

    public void editProfile(ActionEvent e) {
        new CustomerInfoDialog(application, customer, customer -> {
            customerHomePage.getNameLabel().setText(customer.getName());
            customerHomePage.getEmailLabel().setText(customer.getEmail());
            gotoAllStores(null);
        }).rise();
    }

    public void getPathInStore(Store store) {
        PathInStoreDialog.rise(application, customer.getShoppingLists().get(store));
    }

    public void search(Store store, String txt, String category, SwingUtilities.JPanelList<Item> UI_ItemsList) {
        UI_ItemsList.clearAndReset();
        for (Item item : store.getItems()) {
            if (category.equals("All Categories") || category.equals(item.getCategory().name())) {
                if (txt.isEmpty() || item.getName().contains(txt))
                    UI_ItemsList.addItem(item);
            }
        }
        customerHomePage.revalidate();
        customerHomePage.repaint();
    }

    public void removeItemFromShoppingList(Store store, Item item, SwingUtilities.JPanelList<Item> UI_ShoppingList) {
        customer.removeItemFromShoppingList(store, item);
        UI_ShoppingList.clearAndReset();
        UI_ShoppingList.addItems(customer.getShoppingLists().get(store));
        customerHomePage.revalidate();
        customerHomePage.repaint();
    }

    public void addItemToShoppingList(Store store, Item item, SwingUtilities.JPanelList<Item> UI_ShoppingList) {
        customer.addItemToShoppingList(store, item);
        UI_ShoppingList.clearAndReset();
        UI_ShoppingList.addItems(customer.getShoppingLists().get(store));
        customerHomePage.revalidate();
        customerHomePage.repaint();
    }

    public void addPreferredStore(Store store) {
        if (!customer.getPreferredStores().contains(store)) {
            customer.addPreferredStore(store);
            customerHomePage.getPrefStoresListModel().addElement(store);
        }
    }

    public void removePreferredStore(Store store) {
        customer.removePreferredStore(store);
        customerHomePage.getPrefStoresListModel().removeElement(store);
        gotoAllStores(null);
    }

    public void selectStore(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = customerHomePage.getPrefStoresList().getSelectedIndex();
            if (index >= 0)
                customerHomePage.riseSelectedStorePanel(customer.getPreferredStores().elementAt(index));
        }
    }

    public void gotoAllStores(MouseEvent e) {
        Vector<Store> stores = application.getAllStores();
        stores.sort(Comparator.comparingDouble((Store o) -> customer.getLocation().distance(o.getLocation())).reversed());
        customerHomePage.riseStoresPanel(stores);
        customerHomePage.getPrefStoresList().clearSelection();
    }
}
