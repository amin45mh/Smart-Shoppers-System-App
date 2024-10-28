package EECS3311.model;

import java.util.Vector;

public class Store {
    private String name;
    private Location location;
    private int id;
    private Vector<Item> items;

    public Store(String name, Location location, int id) {
        this(name, location, id, new Vector<>());
    }

    public Store(String name, Location location, int id, Vector<Item> items) {
        this.name = name;
        this.location = location;
        this.id = id;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public Vector<Item> getItems() {
        return items;
    }

    public void addItem(Item ... items){
        for(Item item : items)
            this.items.add(item);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
