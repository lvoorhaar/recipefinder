/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.dbconnect;

import com.google.gson.Gson;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import nl.sogyo.recipefinder.main.Category;
import nl.sogyo.recipefinder.main.Ingredient;
import nl.sogyo.recipefinder.main.Recipe;
import nl.sogyo.recipefinder.main.RecipeCollection;

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
        
        /*Query<Recipe> query = datastore.createQuery(Recipe.class);

        long number = query.count();

        System.out.println(number);
        
        Recipe found = query.first();
        
        System.out.println(found);
        
        List<Recipe> recipes = query.find().toList();*/
        
        //List<String> list = Arrays.asList("all purpose flour","butter","powdered sugar");
        
        //Query<Recipe> query = datastore.createQuery(Recipe.class);
        
        //List<Recipe> recipes = query.filter("preptime <=", 20).asList();
        
        /*List<Recipe> recipes = datastore.createQuery(Recipe.class)
                .field("ingredients")
                .elemMatch(query)
                .find()
                .toList();*/
        
        //String JSONoutput = new Gson().toJson(recipes);
        
        /*db.test1.find().forEach(function(x) {
            x.array.forEach(function(y) {
                if (y.name instanceof Array) {
                    y.name.forEach(function(z) {
                        print(z.name);
                    });
                }
            });
        });*/
        
        //List<Recipe> recipes = query.field("ingredients").sizeEq(3).find().toList();
        

        //List<Recipe> recipes = query.field("ingredients.name").in(list).find().toList();

        //List<Recipe> recipes = query.search("coconut").asList();
        
        List<String> list = Arrays.asList("all purpose flour","butter","powdered sugar");
        Query<Recipe> query = datastore.createQuery(Recipe.class);
        //ArrayList<Recipe> recipes = new ArrayList<>();
        
        
        //List<Recipe> recipes = query.field("ingredients.name").notIn(list).find().toList();
        
        List<Recipe> recipes = query.filter("ingredients.name all", list).find().toList();

        System.out.println(recipes);
    }
    
}
