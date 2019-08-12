/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

/**
 *
 * @author lvoorhaar
 */
public class Converter {
    
    public static String convertToImpUnits(String amount, String unit) {
        if (unit.toLowerCase().contains("ml")) {
            
        } else if (unit.toLowerCase().contains("g")) {
            
        }
        return amount + " " + unit;
    }
    
    public static String convertToSIUnits(String amount, String unit) {
        if (unit.toLowerCase().contains("tsp") || unit.toLowerCase().contains("teaspoon")) {
            return String.format("%.2f", fractionToDouble(amount) * 5) + " ml";
        } else if (unit.toLowerCase().contains("tbsp") || unit.toLowerCase().contains("tablespoon")) {
            return String.format("%.2f", fractionToDouble(amount) * 15) + " ml";
        } else if (unit.toLowerCase().contains("cup")) {
            return String.format("%.2f", fractionToDouble(amount) * 240) + " ml";
        } else if (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounces")) {
            return String.format("%.2f", fractionToDouble(amount) * 28.35) + " g";
        } else {
            return amount + " " + unit;
        }
    }
    
    static double fractionToDouble(String amount) {
        double doubleAmount = 0;
        String[] splitamount = amount.split(" ");
        for (String s : splitamount) {
            if (s.contains("/")) {
                double number = Double.parseDouble(s.split("/")[0]) / Double.parseDouble(s.split("/")[1]);
                doubleAmount += number;
            } else {
                doubleAmount += Double.parseDouble(s);
            }
        }
        return doubleAmount;
    }
}
