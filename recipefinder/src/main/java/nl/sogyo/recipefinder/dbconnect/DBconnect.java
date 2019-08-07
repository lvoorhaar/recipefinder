/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.dbconnect;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.sogyo.recipefinder.main.Category;
import nl.sogyo.recipefinder.main.Ingredient;
import nl.sogyo.recipefinder.main.Recipe;

/**
 *
 * @author lvoorhaar
 */
public class DBconnect {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();
        
        
        /*List<String> list = Arrays.asList("1","g","sugar");
        Ingredient ingredient = new Ingredient(list);
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(ingredient);
        
        
        Recipe testRecipe = new Recipe("test", new ArrayList<Category>(Arrays.asList(Category.BREAKFAST)), 5, 1, "blabla", ingredients);
        datastore.save(testRecipe);*/
        
        Query<Recipe> query = datastore.createQuery(Recipe.class);

        long number = query.count();

        System.out.println(number);
        
        Recipe found = query.first();
        
        System.out.println(found.toString());
    }
    
}
