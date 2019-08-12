/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lvoorhaar
 */
public class RecipeSearchTest {
    @Test
    public void testShortbreadCookies() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("flour");
        ingredients.add("sugar");
        ingredients.add("butter");
        RecipeSearch search = new RecipeSearch(ingredients);
        ArrayList<Recipe> recipes = search.findRecipes();
        String name = recipes.get(0).getName();
        Assert.assertEquals(name, "3 Ingredient Shortbread Cookies");
    }
}
