package EECS3311.model.users;

import EECS3311.model.Item;
import EECS3311.model.Location;
import EECS3311.model.Store;

import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public class Customer extends User {
    private Map<Store, Vector<Item>> shoppingLists;
    private Location location;

    public Customer(int id, String name, String email, String password, Location location) {
        super(id, name, email, password);
        this.location = location;
        shoppingLists = new HashMap<>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<Store, Vector<Item>> getShoppingLists() {
        return shoppingLists;
    }

    public Vector<Store> getPreferredStores() {
        Vector<Store> stores = new Vector<>();
        for (Store store : shoppingLists.keySet())
            stores.add(store);
        return stores;
    }

    public void addPreferredStore(Store store) {
        if (!shoppingLists.containsKey(store))
            shoppingLists.put(store, new Vector<>());
    }

    public void removePreferredStore(Store store) {
        shoppingLists.remove(store);
    }

    public void addItemToShoppingList(Store store, Item... items) {
        addPreferredStore(store);
        for (Item item : items)
            shoppingLists.get(store).add(item);
    }

    public void removeItemFromShoppingList(Store store, Item... items) {
        addPreferredStore(store);
        for (Item item : items)
            shoppingLists.get(store).remove(item);
    }


}
