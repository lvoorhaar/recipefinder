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
    private String notes;

    public Ingredient() {}
    
    public Ingredient(List<String> elements) {
        this.amount = elements.get(0);
        this.unit = elements.get(1);
        this.name = elements.get(2);
    }
    
    @Override
    public String toString() {
        String text = "";
        if (this.amount != null) text += this.amount + " ";
        if (this.unit != null) text += this.unit + " ";
        text += this.name;
        if (this.notes != null) text += this.notes + " ";
        return text;
    }
    
    public String toImpString() {
        return null;
    }
    
    public String toSIString() {
        return null;
    }
    
    public boolean matches(String ingredient) {
        if (this.notes != null && this.notes.contains("optional")) {
            return true;
        } else if (this.name.equals(ingredient.toLowerCase().trim())) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean matches(List<String> ingredients) {
        for (String ingredient : ingredients) {
            if (this.matches(ingredient)) {
                return true;
            }
        }
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

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
  
}
