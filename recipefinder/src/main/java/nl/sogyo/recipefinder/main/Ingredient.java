/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lvoorhaar
 */
public class Ingredient {
    
    private String name;
    private String amount;
    private String unit;

    public Ingredient() {}
    
    public Ingredient(List<String> elements) {
        this.amount = elements.get(0);
        this.unit = elements.get(1);
        this.name = elements.get(2);
    }
    
    @Override
    public String toString() {
        return this.amount + " " + this.unit + " " + this.name;
    }
    
    public String toImpString() {
        return null;
    }
    
    public String toSIString() {
        return null;
    }
    
    public boolean matches(Ingredient ingredient) {
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
  
}
