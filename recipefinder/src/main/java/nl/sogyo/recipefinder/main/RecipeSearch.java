/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lvoorhaar
 */
public class RecipeSearch {
    
    private List<Category> categories;
    private int maxPreptime;
    private ArrayList<String> ingredients;
    //private RecipeCollection recipeCollection;
    private Morphia morphia;
    private Datastore datastore;
    private Query<Recipe> query;
    
    public RecipeSearch(List<String> ingredients) {
        this.ingredients = new ArrayList<>();
        for (String s : ingredients) {
            this.ingredients.add(s);
        }
        this.morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        this.datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();
        this.query = datastore.createQuery(Recipe.class);
        this.addObviousIngredients();
    }
    
    public RecipeSearch(List<String> ingredients, List<Category> categories) {
        
    }
    
    public RecipeSearch(List<String> ingredients, int maxPreptime) {
        
    }
    
    public RecipeSearch(List<String> ingredients, ArrayList<Category> categories, int maxPreptime) {
        
    }
    
    private void addObviousIngredients() {
        this.ingredients.add("water");
    }
    
    public List<Recipe> findRecipes() {
        List<Recipe> searchedRecipes = query.field("ingredients.name").in(this.ingredients).find().toList();
        ArrayList<Recipe> matchingRecipes = new ArrayList<>();
        for (Recipe currentRecipe : searchedRecipes) {
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
