package EECS3311.test;

import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Location;
import EECS3311.model.Store;
import EECS3311.model.users.Admin;
import EECS3311.model.users.Customer;
import EECS3311.model.users.Manager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestUsers {
    private Manager manager1, manager2;
    private Admin admin1, admin2;
    private Customer customer1, customer2;

    @Before
    public void init() {
        manager1 = new Manager(0, "manager1", "email_manager1", "pass manager 1");
        manager2 = new Manager(1, "manager2", "email_manager2", "pass manager 2");
        admin1 = new Admin(2, "admin1", "email_admin1", "pass admin 1");
        admin2 = new Admin(3, "admin2", "email_admin2", "pass admin 2");
        customer1 = new Customer(4, "customer1", "email_customer1", "pass customer 1", new Location("loc1", 25, 3.5));
        customer2 = new Customer(5, "customer2", "email_customer2", "pass customer 2", new Location("loc2", 1.2584, 2));
    }

    @Test
    public void testGetters() {
        Assert.assertArrayEquals(
                new String[]{
                        "manager1", "email_manager1", "pass manager 1",
                        "manager2", "email_manager2", "pass manager 2",
                        "admin1", "email_admin1", "pass admin 1",
                        "admin2", "email_admin2", "pass admin 2",
                        "customer1", "email_customer1", "pass customer 1", "loc1",
                        "customer2", "email_customer2", "pass customer 2", "loc2",
                },
                new String[]{
                        manager1.getName(), manager1.getEmail(), manager1.getPassword(),
                        manager2.getName(), manager2.getEmail(), manager2.getPassword(),
                        admin1.getName(), admin1.getEmail(), admin1.getPassword(),
                        admin2.getName(), admin2.getEmail(), admin2.getPassword(),
                        customer1.getName(), customer1.getEmail(), customer1.getPassword(), customer1.getLocation().getLocation(),
                        customer2.getName(), customer2.getEmail(), customer2.getPassword(), customer2.getLocation().getLocation(),
                }
        );
        Assert.assertArrayEquals(
                new Double[]{25.0, 3.5, 1.2584, 2.0},
                new Double[]{
                        customer1.getLocation().getLat(), customer1.getLocation().getLng(),
                        customer2.getLocation().getLat(), customer2.getLocation().getLng(),
                }
        );
        Assert.assertArrayEquals(
                new Integer[]{0, 1, 2, 3, 4, 5},
                new Integer[]{
                        manager1.getId(), manager2.getId(),
                        admin1.getId(), admin2.getId(),
                        customer1.getId(), customer2.getId(),
                }
        );
    }

    @Test
    public void testSetters() {
        manager1.setName("manager1__update");
        manager1.setEmail("email_manager1__update");
        manager2.setName("manager2__update");
        manager2.setEmail("email_manager2__update");
        admin1.setName("admin1__update");
        admin1.setEmail("email_admin1__update");
        admin2.setName("admin2__update");
        admin2.setEmail("email_admin2__update");
        customer1.setName("customer1__update");
        customer1.setEmail("email_customer1__update");
        customer1.setLocation(new Location("loc1__update", 135, 135.151));
        customer2.setName("customer2__update");
        customer2.setEmail("email_customer2__update");
        customer2.setLocation(new Location("loc2__update", 1365, 3541.24));

        Assert.assertArrayEquals(
                new String[]{
                        "manager1__update", "email_manager1__update", "pass manager 1",
                        "manager2__update", "email_manager2__update", "pass manager 2",
                        "admin1__update", "email_admin1__update", "pass admin 1",
                        "admin2__update", "email_admin2__update", "pass admin 2",
                        "customer1__update", "email_customer1__update", "pass customer 1", "loc1__update",
                        "customer2__update", "email_customer2__update", "pass customer 2", "loc2__update",
                },
                new String[]{
                        manager1.getName(), manager1.getEmail(), manager1.getPassword(),
                        manager2.getName(), manager2.getEmail(), manager2.getPassword(),
                        admin1.getName(), admin1.getEmail(), admin1.getPassword(),
                        admin2.getName(), admin2.getEmail(), admin2.getPassword(),
                        customer1.getName(), customer1.getEmail(), customer1.getPassword(), customer1.getLocation().getLocation(),
                        customer2.getName(), customer2.getEmail(), customer2.getPassword(), customer2.getLocation().getLocation(),
                }
        );
        Assert.assertArrayEquals(
                new Double[]{135.0, 135.151, 1365.0, 3541.24},
                new Double[]{
                        customer1.getLocation().getLat(), customer1.getLocation().getLng(),
                        customer2.getLocation().getLat(), customer2.getLocation().getLng(),
                }
        );
        Assert.assertArrayEquals(
                new Integer[]{0, 1, 2, 3, 4, 5},
                new Integer[]{
                        manager1.getId(), manager2.getId(),
                        admin1.getId(), admin2.getId(),
                        customer1.getId(), customer2.getId(),
                }
        );
    }

    @Test
    public void testCustomerFunctionalities(){
        /* initialize */
        Store store1 = new Store("store 1", new Location("loc_s_1", 20, 20), 0);
        Item item11 = new Item(0, "i1", "d1", "1 kg", 2.5, true, new Location("l1", 40, 40), Category.Drinks);
        Item item12 = new Item(1, "i2", "d2", "2 kg", 50, false, new Location("l2", 40, 50), Category.Drinks);
        Item item13 = new Item(2, "i3", "d3", "3 kg", 10, true, new Location("l3", 40, 60), Category.Drinks);
        store1.addItem(item11, item12, item13);

        Store store2 = new Store("store 2", new Location("loc_s_2", 50, 50), 1);
        Item item21 = new Item(2, "i4", "d4", "4 kg", 0.5, true, new Location("l3", 40, 60), Category.Drinks);
        store2.addItem(item21);

        /* test customer 1 */
        customer1.addPreferredStore(store1);
        customer1.addItemToShoppingList(store1);
        Assert.assertEquals(1, customer1.getPreferredStores().size());
        Assert.assertEquals(0, customer1.getShoppingLists().get(store1).size());
        Assert.assertNull(customer1.getShoppingLists().get(store2));

        customer1.addItemToShoppingList(store1, item11);
        Assert.assertEquals(1, customer1.getPreferredStores().size());
        Assert.assertEquals(1, customer1.getShoppingLists().get(store1).size());
        Assert.assertEquals(item11, customer1.getShoppingLists().get(store1).get(0));
        Assert.assertNull(customer1.getShoppingLists().get(store2));

        customer1.addItemToShoppingList(store2, item21);
        Assert.assertEquals(1, customer1.getShoppingLists().get(store2).size());
        Assert.assertEquals(item21, customer1.getShoppingLists().get(store2).get(0));

        customer1.addItemToShoppingList(store1, item12, item13);
        Assert.assertEquals(3, customer1.getShoppingLists().get(store1).size());
        Assert.assertEquals(item11, customer1.getShoppingLists().get(store1).get(0));
        Assert.assertEquals(item12, customer1.getShoppingLists().get(store1).get(1));
        Assert.assertEquals(item13, customer1.getShoppingLists().get(store1).get(2));


        /* test customer 2 */
        customer2.addPreferredStore(store1);
        customer2.addItemToShoppingList(store1);
        Assert.assertEquals(1, customer2.getPreferredStores().size());
        Assert.assertEquals(0, customer2.getShoppingLists().get(store1).size());
        Assert.assertNull(customer2.getShoppingLists().get(store2));

        customer2.addItemToShoppingList(store1, item11);
        Assert.assertEquals(1, customer2.getPreferredStores().size());
        Assert.assertEquals(1, customer2.getShoppingLists().get(store1).size());
        Assert.assertEquals(item11, customer2.getShoppingLists().get(store1).get(0));
        Assert.assertNull(customer2.getShoppingLists().get(store2));

        customer2.addItemToShoppingList(store2, item21);
        Assert.assertEquals(1, customer2.getShoppingLists().get(store2).size());
        Assert.assertEquals(item21, customer2.getShoppingLists().get(store2).get(0));

        customer2.addItemToShoppingList(store1, item12, item13);
        Assert.assertEquals(3, customer2.getShoppingLists().get(store1).size());
        Assert.assertEquals(item11, customer2.getShoppingLists().get(store1).get(0));
        Assert.assertEquals(item12, customer2.getShoppingLists().get(store1).get(1));
        Assert.assertEquals(item13, customer2.getShoppingLists().get(store1).get(2));
    }

    @Test
    public void testManagerFunctionalities(){
        /* initialize */
        Store store1 = new Store("store 1", new Location("loc_s_1", 20, 20), 0);
        Store store2 = new Store("store 2", new Location("loc_s_2", 50, 50), 1);

        /* test manager 1 */
        manager1.addStore(store1);
        Assert.assertEquals(1, manager1.getStores().size());
        Assert.assertEquals(store1, manager1.getStores().get(0));

        manager1.addStore(store2);
        Assert.assertEquals(2, manager1.getStores().size());
        Assert.assertEquals(store2, manager1.getStores().get(1));

        /* test manager 2 */
        manager2.addStore(store1);
        Assert.assertEquals(1, manager2.getStores().size());
        Assert.assertEquals(store1, manager2.getStores().get(0));

        manager2.addStore(store2);
        Assert.assertEquals(2, manager2.getStores().size());
        Assert.assertEquals(store2, manager2.getStores().get(1));
    }

    @Test
    public void testAdminFunctionalities(){
        //there is not any additional functionality in admin model class
    }
}
