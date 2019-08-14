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
public class RecipeCollection {
    
    private List<Recipe> recipes;
    
    public RecipeCollection() {
        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();
        Query<Recipe> query = datastore.createQuery(Recipe.class);
        this.recipes = query.find().toList();
    }
    
    public void updateRecipes() {
        
    }
    
    public List<Recipe> getRecipes() {
        return this.recipes;
    }
}
