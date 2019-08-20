/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.ArrayList;

/**
 *
 * @author lvoorhaar
 */
public enum Category {
    
    CAKE("cake"),
    COOKIES("cookies"),
    BREAKFAST("breakfast"),
    DINNER("dinner"),
    SNACK("snack"),
    DESSERT("dessert"),
    OTHER("other");
    
    private final String name;
    
    private Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
       return this.name;
    }
    
    public static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Category c : Category.values()) {
            categories.add(c.toString().toUpperCase());
        }
        return categories;
    }
}
