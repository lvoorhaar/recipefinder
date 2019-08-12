/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

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
    public void testTwoCups() {
        String result = Converter.convertToSIUnits("2", "cups");
        Assert.assertEquals("480.00 ml", result);
    }
    
    @Test
    public void testQuarterCup() {
        String result = Converter.convertToSIUnits("1/4", "cup");
        Assert.assertEquals("60.00 ml", result);
    }
    
    @Test
    public void testOneAndAHalfTbsp() {
        String result = Converter.convertToSIUnits("1 1/2", "tbsp");
        Assert.assertEquals("22.50 ml", result);
    }
    
    @Test
    public void testHalfTsp() {
        String result = Converter.convertToSIUnits("1/2", "tsp");
        Assert.assertEquals("2.50 ml", result);
    }
    
    @Test
    public void testTenOz() {
        String result = Converter.convertToSIUnits("10", "oz");
        Assert.assertEquals("283.50 g", result);
    }
}
