/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lvoorhaar
 */
public class RecipeSearchTest {
    @Test
    public void testShortbreadCookies() {
        List<String> ingredients = Arrays.asList("all purpose flour","butter","powdered sugar");
        RecipeSearch search = new RecipeSearch(ingredients);
        List<Recipe> recipes = search.findRecipes();
        String name = recipes.get(0).getName();
        Assert.assertEquals(name, "3 Ingredient Shortbread Cookies");
    }
    
    @Test
    public void testShortbreadCookiesWithWater() {
        List<String> ingredients = Arrays.asList("all purpose flour","butter","powdered sugar", "water");
        RecipeSearch search = new RecipeSearch(ingredients);
        List<Recipe> recipes = search.findRecipes();
        String name = recipes.get(0).getName();
        Assert.assertEquals(name, "3 Ingredient Shortbread Cookies");
    }
}
