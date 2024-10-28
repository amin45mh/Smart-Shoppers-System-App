package EECS3311.model;

public class Item {
    private String name, description, size;
    private double price;
    private boolean availability;
    private Location locationInStore;
    private Category category;
    int id;

    public Item(int id, String name, String description, String size, double price, boolean availability, Location locationInStore, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.size = size;
        this.price = price;
        this.availability = availability;
        this.locationInStore = locationInStore;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return availability;
    }

    public Location getLocationInStore() {
        return locationInStore;
    }

    public Category getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setLocationInStore(Location locationInStore) {
        this.locationInStore = locationInStore;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }
}
