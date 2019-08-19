/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lvoorhaar
 */
public class RecipeTest {
    @Test
    public void testRecipeMatches() {
        Recipe recipe = new Recipe();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        ingredients.add(sugar);
        Ingredient butter = new Ingredient();
        butter.setName("butter");
        ingredients.add(butter);
        Ingredient flour = new Ingredient();
        flour.setName("flour");
        ingredients.add(flour);
        recipe.setIngredients(ingredients);
        List<String> testIngredients = Arrays.asList("butter","flour","sugar");
        Assert.assertTrue(recipe.matchesIngredients(testIngredients));
    }
    
    @Test
    public void testMatchScore100() {
        Recipe recipe = new Recipe();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        ingredients.add(sugar);
        Ingredient butter = new Ingredient();
        butter.setName("butter");
        ingredients.add(butter);
        Ingredient flour = new Ingredient();
        flour.setName("flour");
        ingredients.add(flour);
        recipe.setIngredients(ingredients);
        List<String> testIngredients = Arrays.asList("butter","flour","sugar");
        Assert.assertEquals(100, recipe.getMatchScore(testIngredients), 0.1);
    }
    
    @Test
    public void testMatchScore33() {
        Recipe recipe = new Recipe();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        ingredients.add(sugar);
        Ingredient butter = new Ingredient();
        butter.setName("butter");
        ingredients.add(butter);
        Ingredient flour = new Ingredient();
        flour.setName("flour");
        ingredients.add(flour);
        recipe.setIngredients(ingredients);
        List<String> testIngredients = Arrays.asList("sugar");
        Assert.assertEquals(33.33, recipe.getMatchScore(testIngredients), 0.1);
    }
    
}
