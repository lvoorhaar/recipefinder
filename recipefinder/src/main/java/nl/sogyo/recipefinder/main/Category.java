/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

/**
 *
 * @author lvoorhaar
 */
public enum Category {
    
    CAKE("cake"),
    COOKIES("cookies"),
    BREAKFAST("breakfast"),
    DINNER("dinner"),
    SNACKS("snacks"),
    DESSERT("dessert"),
    UNKNOWN("unknown");
    
    private final String name;
    
    private Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
       return this.name;
    }
}
