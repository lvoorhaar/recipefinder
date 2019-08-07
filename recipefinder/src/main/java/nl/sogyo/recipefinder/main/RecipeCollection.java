/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;


import java.util.ArrayList;

/**
 *
 * @author lvoorhaar
 */
public class RecipeCollection {
    
    private ArrayList<Recipe> recipes;
    
    public RecipeCollection() {
        /*MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("recipes");
        MongoCollection<Document> collection = database.getCollection("recipes-testdb");
        this.recipes = new ArrayList<Recipe>();
        for (Document doc : collection.find()) {
            recipes.add(new Recipe(doc));
        }
        mongoClient.close();*/
    }
    
    public void updateRecipes() {
        
    }
    
    public ArrayList<Recipe> getRecipes() {
        return this.recipes;
    }
}
