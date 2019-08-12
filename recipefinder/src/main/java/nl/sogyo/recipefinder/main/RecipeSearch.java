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
public class RecipeSearch {
    
    private List<Category> categories;
    private int maxPreptime;
    private List<String> ingredients;
    private RecipeCollection recipeCollection;
    
    public RecipeSearch(List<String> ingredients) {
        this.ingredients = ingredients;
        this.recipeCollection = new RecipeCollection();
    }
    
    public RecipeSearch(List<String> ingredients, ArrayList<Category> categories) {
        
    }
    
    public RecipeSearch(List<String> ingredients, int maxPreptime) {
        
    }
    
    public RecipeSearch(List<String> ingredients, ArrayList<Category> categories, int maxPreptime) {
        
    }
    
    public ArrayList<Recipe> findRecipes() {
        ArrayList<Recipe> matchingRecipes = new ArrayList<>();
        for (Recipe currentRecipe : this.recipeCollection.getRecipes()) {
            if (currentRecipe.matchesIngredients(this.ingredients)) {
                matchingRecipes.add(currentRecipe);
            }
        }
        return matchingRecipes;
    }
    
    public ArrayList<Recipe> sortRecipes() {
        return null;
    }
    
}
