package EECS3311.test;

import EECS3311.model.Category;
import EECS3311.model.Item;
import EECS3311.model.Location;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestItems {
    private Item item1, item2;


    @Before
    public void init() {
        item1 = new Item(0, "i1", "d1", "1 kg", 2.5, true, new Location("l1", 40, 40), Category.Drinks);
        item2 = new Item(1, "i2", "d2", "2 kg", 3.5, false, new Location("l2", 60, 50), Category.Fruits);
    }

    @Test
    public void testGetters() {
        Assert.assertArrayEquals(
                new String[]{
                        "i1", "d1", "1 kg", "Drinks", "l1",
                        "i2", "d2", "2 kg", "Fruits", "l2",
                },
                new String[]{
                        item1.getName(), item1.getDescription(), item1.getSize(), item1.getCategory().name(), item1.getLocationInStore().getLocation(),
                        item2.getName(), item2.getDescription(), item2.getSize(), item2.getCategory().name(), item2.getLocationInStore().getLocation()
                }
        );
        Assert.assertArrayEquals(
                new Double[]{
                        2.5, 40.0, 40.0,
                        3.5, 60.0, 50.0
                },
                new Double[]{
                        item1.getPrice(), item1.getLocationInStore().getLat(), item1.getLocationInStore().getLng(),
                        item2.getPrice(), item2.getLocationInStore().getLat(), item2.getLocationInStore().getLng()
                }
        );
        Assert.assertArrayEquals(
                new Boolean[]{true, false},
                new Boolean[]{item1.isAvailable(), item2.isAvailable()}
        );
        Assert.assertArrayEquals(
                new Integer[]{0, 1},
                new Integer[]{item1.getId(), item2.getId(),}
        );
    }

    @Test
    public void testSetters() {
        item1.setName("i1__updated");
        item1.setAvailability(false);
        item1.setDescription("d1__update");
        item1.setSize("1 kg__update");
        item1.setPrice(5.6);
        item1.setCategory(Category.Vegetables);
        item1.setLocationInStore(new Location("l1__updated", 20, 25));

        item2.setName("i2__updated");
        item2.setAvailability(true);
        item2.setDescription("d2__update");
        item2.setSize("2 kg__update");
        item2.setPrice(56.3);
        item2.setCategory(Category.Beans);
        item2.setLocationInStore(new Location("l2__updated", 30, 35));

        Assert.assertArrayEquals(
                new String[]{
                        "i1__updated", "d1__update", "1 kg__update", "Vegetables", "l1__updated",
                        "i2__updated", "d2__update", "2 kg__update", "Beans", "l2__updated",
                },
                new String[]{
                        item1.getName(), item1.getDescription(), item1.getSize(), item1.getCategory().name(), item1.getLocationInStore().getLocation(),
                        item2.getName(), item2.getDescription(), item2.getSize(), item2.getCategory().name(), item2.getLocationInStore().getLocation()
                }
        );
        Assert.assertArrayEquals(
                new Double[]{
                        5.6, 20.0, 25.0,
                        56.3, 30.0, 35.0
                },
                new Double[]{
                        item1.getPrice(), item1.getLocationInStore().getLat(), item1.getLocationInStore().getLng(),
                        item2.getPrice(), item2.getLocationInStore().getLat(), item2.getLocationInStore().getLng()
                }
        );
        Assert.assertArrayEquals(
                new Boolean[]{false, true},
                new Boolean[]{item1.isAvailable(), item2.isAvailable()}
        );
        Assert.assertArrayEquals(
                new Integer[]{0, 1},
                new Integer[]{item1.getId(), item2.getId(),}
        );
    }


}
