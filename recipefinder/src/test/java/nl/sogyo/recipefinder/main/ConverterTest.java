/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lvoorhaar
 */
public class ConverterTest {
    @Test
    public void testOneToDouble() {
        double result = Converter.fractionToDouble("1");
        Assert.assertEquals(1, result, 0.01);
    }
    
    @Test
    public void testHalfToDouble() {
        double result = Converter.fractionToDouble("1/2");
        Assert.assertEquals(0.5, result, 0.01);
    }
    
    @Test
    public void testwoAndQuarterToDouble() {
        double result = Converter.fractionToDouble("2 1/4");
        Assert.assertEquals(2.25, result, 0.01);
    }
    
    @Test
    public void testTwoFractions() {
        double result = Converter.fractionToDouble("1/4 + 1/16");
        Assert.assertEquals(0.31, result, 0.01);
    }
    
    @Test
    public void testTwoFractionsNoSpaces() {
        double result = Converter.fractionToDouble("1/4+1/16");
        Assert.assertEquals(0.31, result, 0.01);
    }
    
    @Test
    public void testAverageValue() {
        double result = Converter.fractionToDouble("1-2");
        Assert.assertEquals(1.5, result, 0.01);
    }
    
    @Test
    public void testAverageValueWithSpaces() {
        double result = Converter.fractionToDouble(" 1 - 2 ");
        Assert.assertEquals(1.5, result, 0.01);
    }
    
    @Test
    public void testAverageValueWithTo() {
        double result = Converter.fractionToDouble("1 to 2");
        Assert.assertEquals(1.5, result, 0.01);
    }
    
    @Test
    public void testTwoCups() {
        Ingredient water = new Ingredient("2", "cups", "water", null);
        Ingredient result = Converter.convertToMetricUnits(water);
        Assert.assertEquals("480", result.getAmount());
        Assert.assertEquals("mL", result.getUnit());
    }
    
    @Test
    public void testQuarterCup() {
        Ingredient water = new Ingredient("1/4", "cups", "water", null);
        Ingredient result = Converter.convertToMetricUnits(water);
        Assert.assertEquals("60", result.getAmount());
        Assert.assertEquals("mL", result.getUnit());
    }
    
    /*@Test
    public void testOneAndAHalfTbsp() {
        Ingredient water = new Ingredient("1 1/2", "tbsp", "water", null);
        Ingredient result = Converter.convertToMetricUnits(water);
        Assert.assertEquals("22.5", result.getAmount());
        Assert.assertEquals("mL", result.getUnit());
    }*/
    
    /*@Test
    public void testHalfTsp() {
        Ingredient water = new Ingredient("1/2", "tsp", "water", null);
        Ingredient result = Converter.convertToMetricUnits(water);
        Assert.assertEquals("2.5", result.getAmount());
        Assert.assertEquals("mL", result.getUnit());
    }*/
    
    @Test
    public void testTenOz() {
        Ingredient flour = new Ingredient("10", "oz", "flour", null);
        Ingredient result = Converter.convertToMetricUnits(flour);
        Assert.assertEquals("284", result.getAmount());
        Assert.assertEquals("g", result.getUnit());
    }
    
    @Test
    public void testTenFlOz() {
        Ingredient water = new Ingredient("10", "fl oz", "water", null);
        Ingredient result = Converter.convertToMetricUnits(water);
        Assert.assertEquals("300", result.getAmount());
        Assert.assertEquals("mL", result.getUnit());
    }
    
    @Test
    public void testTenFluidOunce() {
        Ingredient water = new Ingredient("10", "fluid ounce", "water", null);
        Ingredient result = Converter.convertToMetricUnits(water);
        Assert.assertEquals("300", result.getAmount());
        Assert.assertEquals("mL", result.getUnit());
    }
    
    @Test
    public void testTwoInch() {
        Ingredient ginger = new Ingredient("2", "inch", "ginger", null);
        Ingredient result = Converter.convertToMetricUnits(ginger);
        Assert.assertEquals("5", result.getAmount());
        Assert.assertEquals("cm", result.getUnit());
    }
    
    @Test
    public void test120ml() {
        Ingredient water = new Ingredient("120", "ml", "water", null);
        Ingredient result = Converter.convertToUSUnits(water);
        Assert.assertEquals("1/2", result.getAmount());
        Assert.assertEquals("cup", result.getUnit());
    }
    
    @Test
    public void test100gFlour() {
        Ingredient flour = new Ingredient("100", "g", "flour", null);
        Ingredient result = Converter.convertToUSUnits(flour);
        Assert.assertEquals("2/3", result.getAmount());
        Assert.assertEquals("cup", result.getUnit());
    }
    
    @Test
    public void test1dlWater() {
        Ingredient water = new Ingredient("1", "dl", "water", null);
        Ingredient result = Converter.convertToUSUnits(water);
        Assert.assertEquals("2", result.getAmount());
        Assert.assertEquals("tsp", result.getUnit());
    }
    
    @Test
    public void test3dlWater() {
        Ingredient water = new Ingredient("3", "dl", "water", null);
        Ingredient result = Converter.convertToUSUnits(water);
        Assert.assertEquals("1/8", result.getAmount());
        Assert.assertEquals("cup", result.getUnit());
    }
    
    @Test
    public void test1mlWater() {
        Ingredient water = new Ingredient("1", "ml", "water", null);
        Ingredient result = Converter.convertToUSUnits(water);
        Assert.assertEquals("1/4", result.getAmount());
        Assert.assertEquals("tsp", result.getUnit());
    }
    
    @Test
    public void testOnePinch() {
        Ingredient salt = new Ingredient("1", "pinch", "salt", null);
        Ingredient result = Converter.convertToMetricUnits(salt);
        Assert.assertEquals("1", result.getAmount());
        Assert.assertEquals("pinch", result.getUnit());
    }
    
    @Test
    public void testPinch() {
        Ingredient salt = new Ingredient(null, "pinch", "salt", null);
        Ingredient result = Converter.convertToMetricUnits(salt);
        Assert.assertEquals(null, result.getAmount());
        Assert.assertEquals("pinch", result.getUnit());
    }
    
    @Test
    public void testOneApple() {
        Ingredient apple = new Ingredient("1", null, "apple", null);
        Ingredient result = Converter.convertToMetricUnits(apple);
        Assert.assertEquals("1", result.getAmount());
        Assert.assertEquals(null, result.getUnit());
    }
    
    @Test
    public void testEgg() {
        Ingredient egg = new Ingredient(null, null, "egg", null);
        Ingredient result = Converter.convertToMetricUnits(egg);
        Assert.assertEquals(null, result.getAmount());
        Assert.assertEquals(null, result.getUnit());
    }
    
    @Test
    public void testThreeQuarterVF() {
        double result = Converter.fractionToDouble("¾");
        Assert.assertEquals(0.75, result, 0.01);
    }
    
    @Test
    public void testOneAndQuarterVF() {
        double result = Converter.fractionToDouble("1¼");
        Assert.assertEquals(1.25, result, 0.01);
    }
    
    @Test
    public void testTwoAndHalfVF() {
        double result = Converter.fractionToDouble("2 ½");
        Assert.assertEquals(2.50, result, 0.01);
    }
    
    @Test
    public void testOneEightVF() {
        double result = Converter.fractionToDouble("⅛");
        Assert.assertEquals(0.125, result, 0.01);
    }
    
    @Test
    public void testOneThirdVF() {
        double result = Converter.fractionToDouble("⅓");
        Assert.assertEquals(0.33, result, 0.01);
    }
    
    @Test
    public void testTwoThirdVF() {
        double result = Converter.fractionToDouble("⅔");
        Assert.assertEquals(0.67, result, 0.01);
    }
    
    @Test
    public void testFraction15() {
        String result = Converter.doubleToFractionString(1.5);
        Assert.assertEquals("1 1/2", result);
    }
    
    @Test
    public void testFraction195() {
        String result = Converter.doubleToFractionString(1.95);
        Assert.assertEquals("2", result);
    }
    
    @Test
    public void testFlourConversion() {
        Ingredient flour = new Ingredient("1", "cup", "flour", null);
        Ingredient result = Converter.convertToMetricUnits(flour);
        Assert.assertEquals("150", result.getAmount());
        Assert.assertEquals("g", result.getUnit());
    }
    
    @Test
    public void testCupToUSConversion() {
        Ingredient flour = new Ingredient("1", "cup", "flour", null);
        Ingredient result = Converter.convertToUSUnits(flour);
        Assert.assertEquals("1", result.getAmount());
        Assert.assertEquals("cup", result.getUnit());
    }
    
    @Test
    public void testRecipeWithFractions() {
        boolean exceptionThrown = false;
        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();
        Query<Recipe> query = datastore.createQuery(Recipe.class);
        ObjectId objectId = new ObjectId("5d4d7b382678a84345254900");
        query.criteria("_id").equal(objectId);
        Recipe recipe = query.first();
        try {
            recipe.setIngredientsMetric();
            recipe.setIngredientsUS();
        } catch (Exception e) {
            exceptionThrown = true;
        }
        Assert.assertFalse(exceptionThrown);
    }
}
