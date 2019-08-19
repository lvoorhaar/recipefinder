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
public class IngredientTest {
    @Test
    public void testSugar() {
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        Assert.assertTrue(sugar.matches("sugar"));
    }
    
    @Test
    public void testSugarAndFlour() {
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("sugar");
        ingredients.add("flour");
        Assert.assertTrue(sugar.matches(ingredients));
    }
    
    @Test
    public void testButterAndFlour() {
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("butter");
        ingredients.add("flour");
        Assert.assertFalse(sugar.matches(ingredients));
    }
    
    @Test
    public void testLetters() {
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("ugar");
        ingredients.add("s");
        Assert.assertFalse(sugar.matches(ingredients));
    }
    
    @Test
    public void testPeanutButter() {
        Ingredient peanutButter = new Ingredient();
        peanutButter.setName("peanut butter");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("butter");
        ingredients.add("peanut");
        Assert.assertFalse(peanutButter.matches(ingredients));
    }
    
    
}   
