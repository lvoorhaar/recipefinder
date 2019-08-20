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
import com.mongodb.client.MongoCursor;
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
import org.bson.types.ObjectId;

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
        
        List<String> list = Arrays.asList("all purpose flour","butter","powdered sugar");
        Query<Recipe> query = datastore.createQuery(Recipe.class);
        
        //List<Recipe> recipes = query.field("ingredients").sizeEq(3).find().toList();          // finds all recipes with 3 ingredients
        //List<Recipe> recipes = query.field("ingredients.name").in(list).find().toList();      // match if only 1 ingredient is in list
        //List<Recipe> recipes = query.search("coconut").asList();                              // searches in all fields for string
        //List<Recipe> recipes = query.field("ingredients.name").notIn(list).find().toList();   // opposite of what I want, finds all recipes that dont contain any ingredients from list
        //List<Recipe> recipes = query.filter("ingredients.name all", list).find().toList();    // match if all items from list are in ingredients
        //List<Recipe> recipes = query.filter("ingredients.name in", list).find().toList();     // match if only 1 ingredient is in list
        //List<Recipe> recipes = query.filter("ingredients.name all in", list).find().toList(); // no results
        //List<Recipe> recipes = query.filter("ingredients.name forEach in", list).find().toList(); // no results
        //List<Recipe> recipes = query.field("ingredients.name").not().notIn(list).find().toList(); // match if only 1 ingredient is in list
        //List<Recipe> recipes = query.field("ingredients.name").elemMatch(query).find().toList();
        
        //List<Recipe> recipes = query.field("ingredients.name").in(list).find().toList();       
        //System.out.println(recipes);
        //System.out.println(recipes.size());
        
        //MongoCursor<String> values = datastore.getDatabase().getCollection("recipes-testdb").distinct("ingredients.name", String.class).iterator();
        
        //while (values.hasNext()) {
        //    System.out.println(values.next());
        //}
        
        //List<Recipe> recipes = query.field("preptime").lessThanOrEq(15).find().toList();        //find all recipes with preptime of max 15 min
        
        /*List<String> categories = Arrays.asList("COOKIES");
        //List<Recipe> recipes = query.field("categories").in(categories).find().toList();    //find all recipes of matching categories
        
        List<Recipe> recipes = query.field("categories").in(categories).field("preptime").lessThanOrEq(15).find().toList(); //two search criteria
        
        System.out.println(recipes);
        System.out.println(recipes.size());*/
        
        ObjectId objectId = new ObjectId("5d4d7b382678a84345254900");
        
        query.criteria("_id").equal(objectId);
        Recipe recipe = query.first();
        
        System.out.println(recipe);
        System.out.println(recipe.getId());
    }
    
}
