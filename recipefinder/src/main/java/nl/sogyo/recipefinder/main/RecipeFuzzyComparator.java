/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.Comparator;
import java.util.List;
import nl.sogyo.recipefinder.main.Recipe;

/**
 *
 * @author lvoorhaar
 */
public class RecipeFuzzyComparator implements Comparator<Recipe> {
    private List<String> ingredients;
    
    public RecipeFuzzyComparator(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int compare(Recipe r1, Recipe r2) {
        return r1.getFuzzyMatchScore(this.ingredients).compareTo(r2.getFuzzyMatchScore(this.ingredients)); 
    }
    
}
