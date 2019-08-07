/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.ArrayList;
import java.util.Map;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author lvoorhaar
 */
@Entity("recipes-morphiadb")
public class Recipe {
    
    @Id
    private ObjectId id;
    private String name;
    private ArrayList<Category> categories;
    private int preptime;
    private int rating;
    private String instructions;
    private List<Ingredient> ingredients;
    
    public Recipe() {
        
    }

    public Recipe(String name, ArrayList<Category> categories, int preptime, int rating, String instructions, ArrayList<Ingredient> ingredients) {
        this.name = name;
        this.categories = categories;
        this.preptime = preptime;
        this.rating = rating;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }
    
    public Recipe(Map<String, Object> document) {
        this.name = (String)document.get("name");
        this.preptime = (Integer)document.get("preptime");
        this.rating = (Integer)document.get("rating");
        this.instructions = (String)document.get("instructions");
        
        this.categories = new ArrayList<Category>();
        for (String category : (ArrayList<String>)document.get("category")) {
            this.categories.add(Category.valueOf(category.toUpperCase()));
        }
        
        this.ingredients = new ArrayList<Ingredient>();
        for (ArrayList<String> ingredient : (ArrayList<ArrayList<String>>)document.get("ingredients")) {
            this.ingredients.add(new Ingredient(ingredient));
        }
    }
    
    @Override
    public String toString() {
        String string = "";
        string += "\nName: " + this.name;
        string += "\nCategory: ";
        for (Category c : this.categories) {
            string += c.toString();
            if (this.categories.indexOf(c) < this.categories.size() - 1) {
                string +=  ", ";
            }
        }
        string += "\nPreparation time: " + this.preptime;
        string += "\nRating: " + this.rating;
        string += "\nInstructions: " + this.instructions;
        string += "\nIngredients: ";
        for (Ingredient i : this.ingredients) {
            string += "\n - " + i.toString();
        }
        return string;
    }
    
    private boolean matchesIngredients(ArrayList<Ingredient> searchIngredients) {
        return false;
    }
    
    private boolean matchesCategories(ArrayList<Category> searchCategories) {
        return false;
    }
    
    private boolean matchesPreptime(int searchPreptime) {
        return false;
    }
    
    private boolean matchesSearch(ArrayList<Ingredient> searchIngredients, ArrayList<Category> searchCategories, int searchPreptime) {
        return false;
    }
}
