/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.ArrayList;
import java.util.Map;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import dev.morphia.utils.IndexType;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author lvoorhaar
 */
@Indexes(@Index(fields = @Field(value = "$**", type = IndexType.TEXT)))
@Entity("recipes-testdb")
public class Recipe {
    
    @Id
    private ObjectId id;
    private String name;
    private List<Category> categories;
    private int preptime;
    private int rating;
    private String instructions;
    private List<Ingredient> ingredients;
    
    public Recipe() {}

    public Recipe(String name, List<Category> categories, int preptime, int rating, String instructions, List<Ingredient> ingredients) {
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
        
        this.categories = new ArrayList<>();
        for (String category : (ArrayList<String>)document.get("category")) {
            this.categories.add(Category.valueOf(category.toUpperCase()));
        }
        
        this.ingredients = new ArrayList<>();
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
        string += "\n";
        return string;
    }
    
    boolean matchesIngredients(List<String> searchIngredients) {
        for (Ingredient ingredient : this.ingredients) {
            if (!ingredient.matches(searchIngredients)) {
                return false;
            }
        }
        return true;
    }
    
    boolean matchesCategories(List<Category> searchCategories) {
        return false;
    }
    
    boolean matchesPreptime(int searchPreptime) {
        return false;
    }
    
    boolean matchesSearch(List<Ingredient> searchIngredients, List<Category> searchCategories, int searchPreptime) {
        return false;
    }

    String getName() {
        return name;
    }

    List<Category> getCategories() {
        return categories;
    }

    void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    int getPreptime() {
        return preptime;
    }

    void setPreptime(int preptime) {
        this.preptime = preptime;
    }

    int getRating() {
        return rating;
    }

    void setRating(int rating) {
        this.rating = rating;
    }

    String getInstructions() {
        return instructions;
    }

    void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    List<Ingredient> getIngredients() {
        return ingredients;
    }

    void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    
    
}
