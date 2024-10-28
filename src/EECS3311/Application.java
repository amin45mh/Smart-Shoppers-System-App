package EECS3311;

import EECS3311.controller.*;
import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Location;
import EECS3311.model.Store;
import EECS3311.model.users.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Application extends JFrame {
    Vector<Store> allStores;
    Vector<Manager> allManagers;
    Vector<Customer> allCustomers;
    Vector<Admin> allAdmins;

    public static void main(String[] args) {
        /*
        Admin account:
            > username: Admin
            > password: AdminPass
         */
        //todo: (Low) use dialog box to confirm remove confirmation
        // https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
        Application app = new Application();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setTitle("Smart Shoppers");
        app.pack();
        app.setVisible(true);
    }

    public Application() {
        loadData();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveData));
        gotoLoginPage();
    }

    public void gotoLoginPage() {
        setMinimumSize(new Dimension(400, 500));
        setSize(new Dimension(400, 500));
        getContentPane().removeAll();
        add(new LoginPageController(this).getLoginPage());
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void gotoSignupPage() {
        setMinimumSize(new Dimension(400, 500));
        setSize(new Dimension(400, 500));
        getContentPane().removeAll();
        add(new SignupPageController(this).getSignupPage());
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void gotoCustomerHomePage(Customer customer) {
        setMinimumSize(new Dimension(1000, 700));
        setPreferredSize(new Dimension(1000, 700));
        getContentPane().removeAll();
        add(new CustomerHomePageController(this, customer).getCustomerHomePage());
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void gotoAdminHomePage(Admin admin) {
        setMinimumSize(new Dimension(1000, 700));
        getContentPane().removeAll();
        add(new AdminHomePageController(this, admin).getAdminHomePage());
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void gotoManagerHomePage(Manager manager) {
        setMinimumSize(new Dimension(1000, 700));
        getContentPane().removeAll();
        add(new ManagerHomePageController(this, manager).getManagerHomePage());
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public Vector<Store> getAllStores() {
        return allStores;
    }

    public Vector<Manager> getAllManagers() {
        return allManagers;
    }

    public Vector<Customer> getAllCustomers() {
        return allCustomers;
    }

    public int getNewStoreID() {
        int newId = 9999;
        for (Store store : allStores)
            if (store.getId() > newId)
                newId = store.getId();
        return newId + 1;
    }

    public int getNewItemID() {
        int newId = 99999;
        for (Store store : allStores)
            for (Item item : store.getItems())
                if (item.getId() > newId)
                    newId = item.getId();
        return newId + 1;
    }

    public int getNewUserID(UserType type) {
        int newId = 999999;
        switch (type) {
            case Manager:
                for (Manager manager : allManagers)
                    if (manager.getId() > newId)
                        newId = manager.getId();
                break;
            case Administrator:
                for (Admin admin : allAdmins)
                    if (admin.getId() > newId)
                        newId = admin.getId();
                break;
            case Customer:
                for (Customer customer : allCustomers)
                    if (customer.getId() > newId)
                        newId = customer.getId();
                break;
        }
        return newId + 1;
    }

    public Admin loginAdmin(String email, String pass) {
        for (Admin admin : allAdmins)
            if (admin.getEmail().equals(email) && admin.mathPassword(pass))
                return admin;
        return null;
    }

    public Manager loginManager(String email, String pass) {
        for (Manager manager : allManagers)
            if (manager.getEmail().equals(email) && manager.mathPassword(pass))
                return manager;
        return null;
    }

    public Customer loginCustomer(String email, String pass) {
        for (Customer customer : allCustomers)
            if (customer.getEmail().equals(email) && customer.mathPassword(pass))
                return customer;
        return null;
    }

    public boolean isUniqueEmail(String email, UserType type) {
        switch (type) {
            case Manager:
                for (Manager manager : allManagers)
                    if (manager.getEmail().equals(email))
                        return false;
                break;
            case Administrator:
                for (Admin admin : allAdmins)
                    if (admin.getEmail().equals(email))
                        return false;
                break;
            case Customer:
                for (Customer customer : allCustomers)
                    if (customer.getEmail().equals(email))
                        return false;
                break;
        }
        return true;
    }

    private Store getStoreById(int id) {
        for (Store store : allStores)
            if (store.getId() == id)
                return store;
        return null;
    }

    private Item getItemById(int id) {
        for (Store store : allStores)
            for (Item item : store.getItems())
                if (item.getId() == id)
                    return item;
        return null;
    }

    private void loadData() {
        allStores = new Vector<>();
        allManagers = new Vector<>();
        allCustomers = new Vector<>();
        allAdmins = new Vector<>();

        String[] rc;
        try {
            CSVReader storesReader = new CSVReader(new FileReader("src/database/stores.csv"));
            while ((rc = storesReader.readNext()) != null) {
                Store store = new Store(rc[1], new Location(rc[2], Double.parseDouble(rc[3]), Double.parseDouble(rc[4])), Integer.parseInt(rc[0]));
                allStores.add(store);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("rc/database/stores.csv  NOT-FOUND");
        }

        try {
            CSVReader itemsReader = new CSVReader(new FileReader("src/database/items.csv"));
            while ((rc = itemsReader.readNext()) != null) {
                Item item = new Item(Integer.parseInt(rc[1]), rc[2], rc[3], rc[4], Double.parseDouble(rc[5]), Boolean.parseBoolean(rc[6]),
                        new Location(rc[7], Double.parseDouble(rc[8]), Double.parseDouble(rc[9])), Category.valueOf(rc[10]));
                Store store = getStoreById(Integer.parseInt(rc[0]));
                if (store != null)
                    store.addItem(item);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("rc/database/items.csv  NOT-FOUND");
        }

        try {
            CSVReader customersReader = new CSVReader(new FileReader("src/database/customers.csv"));
            while ((rc = customersReader.readNext()) != null) {
                Customer customer = new Customer(Integer.parseInt(rc[0]), rc[1], rc[2], rc[3],
                        new Location(rc[4], Double.parseDouble(rc[5]), Double.parseDouble(rc[6])));
                int i = 7;
                while (rc[i].equals("|")) {
                    i++;
                    Store store = getStoreById(Integer.parseInt(rc[i++]));
                    if (store == null)
                        while (!rc[i].equals("|") && !rc[i].equals("*"))
                            i++;
                    else
                        while (!rc[i].equals("|") && !rc[i].equals("*")) {
                            Item item = getItemById(Integer.parseInt(rc[i]));
                            if (item != null)
                                customer.addItemToShoppingList(store, item);
                            i++;
                        }
                }
                allCustomers.add(customer);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("rc/database/customers.csv  NOT-FOUND");
        }

        try {
            CSVReader managersReader = new CSVReader(new FileReader("src/database/managers.csv"));
            while ((rc = managersReader.readNext()) != null) {
                Manager manager = new Manager(Integer.parseInt(rc[0]), rc[1], rc[2], rc[3]);
                for (int i = 4; i < rc.length; i++) {
                    Store store = getStoreById(Integer.parseInt(rc[i]));
                    if (store != null)
                        manager.addStore(store);
                }
                allManagers.add(manager);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("rc/database/managers.csv  NOT-FOUND");
        }

        try {
            CSVReader adminsReader = new CSVReader(new FileReader("src/database/admins.csv"));
            while ((rc = adminsReader.readNext()) != null) {
                Admin admin = new Admin(Integer.parseInt(rc[0]), rc[1], rc[2], rc[3]);
                allAdmins.add(admin);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("rc/database/admins.csv  NOT-FOUND");
        }
    }

    private void saveData() {
        try {
            CSVWriter storeWriter = new CSVWriter(new FileWriter("src/database/stores.csv"));
            for (Store store : allStores) {
                storeWriter.writeNext(new String[]{
                        String.valueOf(store.getId()), store.getName(), store.getLocation().getLocation(),
                        String.valueOf(store.getLocation().getLat()), String.valueOf(store.getLocation().getLng())
                });
            }
            storeWriter.close();


            CSVWriter itemWriter = new CSVWriter(new FileWriter("src/database/items.csv"));
            for (Store store : allStores)
                for (Item item : store.getItems()) {
                    itemWriter.writeNext(new String[]{
                            String.valueOf(store.getId()), String.valueOf(item.getId()), item.getName(), item.getDescription(), item.getSize(),
                            String.valueOf(item.getPrice()), Boolean.toString(item.isAvailable()), item.getLocationInStore().getLocation(),
                            String.valueOf(item.getLocationInStore().getLat()), String.valueOf(item.getLocationInStore().getLng()), item.getCategory().name()
                    });
                }
            itemWriter.close();


            CSVWriter customersWriter = new CSVWriter(new FileWriter("src/database/customers.csv"));
            for (Customer customer : allCustomers) {
                List<String> data = new ArrayList<>(Arrays.asList(
                        String.valueOf(customer.getId()), customer.getName(), customer.getEmail(), customer.getPassword(),
                        customer.getLocation().getLocation(), String.valueOf(customer.getLocation().getLat()), String.valueOf(customer.getLocation().getLng())
                ));
                for (Map.Entry<Store, Vector<Item>> entry : customer.getShoppingLists().entrySet()) {
                    data.add("|");//flag the start of  items map
                    data.add(String.valueOf(entry.getKey().getId()));
                    for (Item item : entry.getValue())
                        data.add(String.valueOf(item.getId()));
                }
                data.add("*");//flag the end of items map
                customersWriter.writeNext(data.toArray(new String[0]));
            }
            customersWriter.close();


            CSVWriter managersWriter = new CSVWriter(new FileWriter("src/database/managers.csv"));
            for (Manager manager : allManagers) {
                List<String> data = new ArrayList<>(Arrays.asList(
                        String.valueOf(manager.getId()), manager.getName(), manager.getEmail(), manager.getPassword()
                ));
                for (Store store : manager.getStores())
                    data.add(String.valueOf(store.getId()));
                managersWriter.writeNext(data.toArray(new String[0]));
            }
            managersWriter.close();


            CSVWriter adminsWriter = new CSVWriter(new FileWriter("src/database/admins.csv"));
            for (Admin admin : allAdmins) {
                adminsWriter.writeNext(new String[]{
                        String.valueOf(admin.getId()), admin.getName(), admin.getEmail(), admin.getPassword(),
                });
            }
            adminsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
