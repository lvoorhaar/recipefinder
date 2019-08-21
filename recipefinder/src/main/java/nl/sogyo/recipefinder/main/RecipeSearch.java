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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author lvoorhaar
 */
public class RecipeSearch {
    
    private List<String> categories;
    private int maxPreptime;
    private List<String> ingredients;
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
    }
    
    public RecipeSearch(List<String> ingredients, List<String> categories) {
        this(ingredients);
        this.categories = categories;
        this.query = this.query.field("categories").in(categories);
    }
    
    public RecipeSearch(List<String> ingredients, int maxPreptime) {
        this(ingredients);
        this.maxPreptime = maxPreptime;
        this.query = this.query.field("preptime").lessThanOrEq(maxPreptime);
    }
    
    public RecipeSearch(List<String> ingredients, List<String> categories, int maxPreptime) {
        this(ingredients, categories);
        this.maxPreptime = maxPreptime;
        this.query = this.query.field("preptime").lessThanOrEq(maxPreptime).field("categories").in(categories);
    }
    
    //returns only exact matches
    public List<Recipe> findRecipes() {
        List<Recipe> searchedRecipes = query.field("ingredients.name").in(this.ingredients).find().toList();
        ArrayList<Recipe> matchingRecipes = new ArrayList<>();
        for (Recipe currentRecipe : searchedRecipes) {
            if (currentRecipe.matchesIngredients(this.ingredients)) {
                matchingRecipes.add(currentRecipe);
            }
        }
        this.addIDAndConversions(matchingRecipes);
        return matchingRecipes;
    }
    
    public void addIDAndConversions(List<Recipe> recipes) {
        for (Recipe r : recipes) {
            r.setStringID(r.getId().toHexString());
            r.setIngredientsMetric();
            r.setIngredientsUS();
        }
    }
    
    //retuns partial matches, sorts by exact ingredient match
    public List<Recipe> sortRecipes() {
        List<Recipe> recipes = query.field("ingredients.name").in(this.ingredients).find().toList();
        RecipeComparator recipeComparator = new RecipeComparator(this.ingredients);
        Collections.sort(recipes, recipeComparator.reversed());
        this.addIDAndConversions(recipes);
        return recipes;
    }
    
    //retuns partial matches, sorts by fuzzy ingredient match
    public List<Recipe> fuzzySortRecipes() {
        List<Recipe> recipes = query.field("ingredients.name").in(this.ingredients).find().toList();
        RecipeComparator recipeComparator = new RecipeComparator(this.ingredients);
        Collections.sort(recipes, recipeComparator.reversed());
        this.addIDAndConversions(recipes);
        return recipes;
    }
}
