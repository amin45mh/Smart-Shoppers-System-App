package EECS3311.test;

import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Location;
import EECS3311.model.Store;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestStores {
    private Store store1, store2;

    @Before
    public void init() {
        store1 = new Store("Store 1", new Location("l1", 50, 60), 0);
        store2 = new Store("Store 2", new Location("l2", 30, 40), 1);
    }

    @Test
    public void testGetters() {
        Assert.assertArrayEquals(
                new String[]{
                        "Store 1", "l1", "Store 2", "l2",
                },
                new String[]{
                        store1.getName(), store1.getLocation().getLocation(),
                        store2.getName(), store2.getLocation().getLocation()
                }
        );
        Assert.assertArrayEquals(
                new Double[]{
                        50.0, 60.0, 30.0, 40.0
                },
                new Double[]{
                        store1.getLocation().getLat(), store1.getLocation().getLng(),
                        store2.getLocation().getLat(), store2.getLocation().getLng()
                }
        );
        Assert.assertArrayEquals(
                new Integer[]{0, 1},
                new Integer[]{store1.getId(), store2.getId(),}
        );
    }

    @Test
    public void testSetters() {
        store1.setName("Store 1__updated");
        store1.setLocation(new Location("l1__updated", 80, 90));

        store2.setName("Store 2__updated");
        store2.setLocation(new Location("l2__updated", 60, 70));

        Assert.assertArrayEquals(
                new String[]{
                        "Store 1__updated", "l1__updated", "Store 2__updated", "l2__updated",
                },
                new String[]{
                        store1.getName(), store1.getLocation().getLocation(),
                        store2.getName(), store2.getLocation().getLocation()
                }
        );
        Assert.assertArrayEquals(
                new Double[]{
                        80.0, 90.0, 60.0, 70.0
                },
                new Double[]{
                        store1.getLocation().getLat(), store1.getLocation().getLng(),
                        store2.getLocation().getLat(), store2.getLocation().getLng()
                }
        );
        Assert.assertArrayEquals(
                new Integer[]{0, 1},
                new Integer[]{store1.getId(), store2.getId(),}
        );
    }


    @Test
    public void testFunctionality() {
        Item item11 = new Item(0, "i1", "d1", "1 kg", 2.5, true, new Location("l1", 40, 40), Category.Drinks);
        Item item12 = new Item(1, "i2", "d2", "2 kg", 3.5, false, new Location("l2", 60, 50), Category.Fruits);
        Item item21 = new Item(2, "i3", "d3", "3 kg", 2.5, true, new Location("l3", 40, 40), Category.Drinks);
        Item item22 = new Item(3, "i4", "d4", "4 kg", 3.5, false, new Location("l4", 60, 50), Category.Fruits);

        Assert.assertEquals(0, store1.getItems().size());
        store1.addItem(item11, item12);
        Assert.assertEquals(2, store1.getItems().size());
        Assert.assertEquals(item11, store1.getItems().get(0));
        Assert.assertEquals(item12, store1.getItems().get(1));

        Assert.assertEquals(0, store2.getItems().size());
        store2.addItem(item21, item22);
        Assert.assertEquals(2, store2.getItems().size());
        Assert.assertEquals(item21, store2.getItems().get(0));
        Assert.assertEquals(item22, store2.getItems().get(1));
    }
}
