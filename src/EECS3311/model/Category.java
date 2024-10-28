package EECS3311.model;

import java.util.Arrays;
import java.util.Vector;

public enum Category {
    Drinks,
    Vegetables,
    Beans,
    Proteins,
    DailyProducts,
    Nuts,
    Sweets,
    Cereals,
    Fruits;

    public static Vector<String> getAllCategories() {
        return new Vector<>(Arrays.asList(Drinks.name(), Vegetables.name(), Beans.name(), Proteins.name(), DailyProducts.name(), Nuts.name(), Sweets.name(), Cereals.name(), Fruits.name()));
    }


}

