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
    private String source;
    
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
        this.preptime = Integer.parseInt((String)document.get("preptime"));
        this.rating = Integer.parseInt((String)document.get("rating"));
        this.instructions = (String)document.get("instructions");
        
        this.categories = new ArrayList<>();
        ArrayList<String> categories = (ArrayList<String>)document.get("categories");
        for (String category : categories) {
            this.categories.add(Category.valueOf(category.toUpperCase()));
        }
        
        this.ingredients = new ArrayList<>();
        ArrayList<Object> ingredients = (ArrayList<Object>)document.get("ingredients");
        for (Object ingredient : ingredients) {
            Map<String, String> currentIngredient = (Map<String, String>)ingredient;
            Ingredient i = new Ingredient();
            i.setName(currentIngredient.get("name"));
            if (currentIngredient.get("amount") != null) {
                i.setAmount(currentIngredient.get("amount"));
            }
            if (currentIngredient.get("unit") != null) {
                i.setUnit(currentIngredient.get("unit"));
            }
            if (currentIngredient.get("notes") != null) {
                i.setNotes(currentIngredient.get("notes"));
            }
            this.ingredients.add(i);
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
    
    public Double getMatchScore(List<String> searchIngredients) {
        Double score = 0.0;
        double pieces = 100.0 / this.ingredients.size();
        for (Ingredient ingredient : this.ingredients) {
            if (ingredient.matches(searchIngredients)) {
                score += pieces;
            }
        }
        return score;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSource(String source) {
        this.source = source;
    }

    
}
