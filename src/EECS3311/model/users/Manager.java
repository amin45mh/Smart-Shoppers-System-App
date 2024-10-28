package EECS3311.model.users;

import EECS3311.model.Store;

import java.util.Vector;

public class Manager extends User{
    private Vector<Store> stores;
    public Manager(int id, String name, String email, String password) {
        super(id, name, email, password);
        stores = new Vector<>();
    }

    public Vector<Store> getStores() {
        return stores;
    }

    public void addStore(Store... stores){
        for (Store store : stores)
            this.stores.add(store);
    }
}
