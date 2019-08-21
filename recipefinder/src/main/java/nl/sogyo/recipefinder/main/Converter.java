/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lvoorhaar
 */
public class Converter {
    
    private static final Map<String, Double> densities;
    static {
        Map<String, Double> aMap = new HashMap<>();
        aMap.put("all-purpose flour", 0.625);
        aMap.put("flour", 0.625);
        aMap.put("sugar", 1.0);
        aMap.put("white sugar", 1.0);
        aMap.put("caster sugar", 0.94);
        aMap.put("granulated sugar", 0.825);
        aMap.put("brown sugar", 0.75);
        aMap.put("icing sugar", 0.5);
        aMap.put("cocoa powder", 0.5);
        aMap.put("butter", 0.94);
        aMap.put("chocolate", 0.71);
        aMap.put("peanut butter", 0.94);
        aMap.put("salt", 2.17);
        aMap.put("cornstarch", 0.54);
        aMap.put("pasta", 0.50);
        aMap.put("macaroni", 0.55);
        aMap.put("spaghetti", 0.38);
        aMap.put("cheese", 0.34);
        aMap.put("oats", 0.35);
        aMap.put("rolled oats", 0.35);
        aMap.put("coconut", 0.35);
        aMap.put("baking powder", 0.8);
        aMap.put("baking soda", 1.2);
        aMap.put("protein powder", 0.41);
        aMap.put("chia seeds", 0.81);
        aMap.put("hemp seeds", 0.68);
        aMap.put("flax seeds", 0.72);
        aMap.put("cinnamon", 0.53);
        aMap.put("pecans", 0.46);
        densities = Collections.unmodifiableMap(aMap);
    }

    public static Ingredient convertToUSUnits(Ingredient originalIngredient) {
        /*String amount = originalIngredient.getAmount();
        String unit = originalIngredient.getUnit();
        String name = originalIngredient.getName();
        String notes = originalIngredient.getNotes();
        String newAmount;
        String newUnit;
        if (unit.toLowerCase().contains("ml")) {
            
        } else if (unit.toLowerCase().contains("dl")) {
            
        } else if (unit.toLowerCase().contains("l")) {
            
        } else if (unit.toLowerCase().contains("kg") || unit.toLowerCase().contains("kilogram")) {
            
        } else if (unit.toLowerCase().equals("g") || unit.toLowerCase().contains("gram")) {
            
        } else {
            newAmount = amount;
            newUnit = unit;
        }
        Ingredient newIngredient = new Ingredient();
        newIngredient.setAmount(newAmount);
        newIngredient.setUnit(newUnit);
        newIngredient.setName(name);
        newIngredient.setNotes(notes);*/
        return originalIngredient;
    }

    public static Ingredient convertToMetricUnits(Ingredient originalIngredient) {
        if (originalIngredient.getAmount() != null && originalIngredient.getUnit() != null) {
            String amount = originalIngredient.getAmount();
            String unit = originalIngredient.getUnit();
            String name = originalIngredient.getName();
            String notes = originalIngredient.getNotes();
            String newAmount;
            String newUnit;
            double doubleAmount;
            if (unit.toLowerCase().contains("tsp") || unit.toLowerCase().contains("teaspoon")) {
                doubleAmount = fractionToDouble(amount) * 5;
                newUnit = "mL";
                if (Converter.densities.containsKey(name)) {
                    doubleAmount *= Converter.densities.get(name);
                    newUnit = "g";
                }
                newAmount = String.format("%.1f", doubleAmount);
            } else if (unit.toLowerCase().contains("tbsp") || unit.toLowerCase().contains("tablespoon")) {
                doubleAmount = fractionToDouble(amount) * 15;
                newUnit = "mL";
                if (Converter.densities.containsKey(name)) {
                    doubleAmount *= Converter.densities.get(name);
                    newUnit = "g";
                }
                newAmount = String.format("%.1f", doubleAmount);
            } else if (unit.toLowerCase().contains("cup")) {
                doubleAmount = fractionToDouble(amount) * 240;
                newUnit = "mL";
                if (Converter.densities.containsKey(name)) {
                    doubleAmount *= Converter.densities.get(name);
                    newUnit = "g";
                }
                newAmount = String.format("%.0f", doubleAmount);
            } else if (unit.toLowerCase().contains("fl") && (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounce"))) {
                doubleAmount = fractionToDouble(amount) * 30;
                newUnit = "mL";
                newAmount = String.format("%.0f", doubleAmount);
            } else if (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounce")) {
                doubleAmount = fractionToDouble(amount) * 28.35;
                newUnit = "g";
                newAmount = String.format("%.0f", doubleAmount);
            } else if (unit.toLowerCase().contains("inch") && !unit.toLowerCase().contains("pinch")) {
                doubleAmount = fractionToDouble(amount) * 2.54;
                newUnit = "cm";
                newAmount = String.format("%.0f", doubleAmount);
            } else {
                newAmount = amount;
                newUnit = unit;
            }
            Ingredient newIngredient = new Ingredient();
            newIngredient.setAmount(newAmount);
            newIngredient.setUnit(newUnit);
            newIngredient.setName(name);
            newIngredient.setNotes(notes);
            return newIngredient;
        } else {
            return originalIngredient;
        }
    }

    static double fractionToDouble(String amount) {
        double doubleAmount = 0;
        if (amount.contains("-") || amount.contains("to")) {
            doubleAmount = Converter.averageOfTwoValues(amount);
        } else {
            String[] splitamount = amount.trim().split(" |\\+");
            try {
                for (String s : splitamount) {
                    if (s.contains("/")) {
                        String[] fraction = s.split("/");
                        double number = Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]);
                        doubleAmount += number;
                    } else if (s == null || s.equals("")) {
                        continue;
                    } else if (Normalizer.normalize(s, Normalizer.Form.NFKD).contains("\u2044")) {
                        doubleAmount += Converter.vulgarFractionToDouble(s);
                    } else {
                        doubleAmount += Double.parseDouble(s);
                    }
                }
            } catch (NumberFormatException e) {
                return 1.0;
            }
        }
        return doubleAmount;
    }
    
    static double vulgarFractionToDouble(String s) {
        double doubleAmount = 0;
        if (s.length() > 1) {
            char[] vulgarFraction = s.toCharArray();
            for (char c : vulgarFraction) {
                if (Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFKD).contains("\u2044")) {
                    doubleAmount += Converter.vulgarFractionToDouble(String.valueOf(c));
                } else {
                    doubleAmount += Double.parseDouble(String.valueOf(c));
                }
            }
        } else {
            String[] fraction = Normalizer.normalize(s, Normalizer.Form.NFKD).split("\u2044");
            double number = Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]);
            doubleAmount += number;
        }
        return doubleAmount;
    }
    
    static double averageOfTwoValues(String s) {
        String[] splitamount = s.trim().split(" |\\-|to");
        double first = Converter.fractionToDouble(splitamount[0]);
        double second = Converter.fractionToDouble(splitamount[splitamount.length - 1]);
        double average = (first + second) / 2.0;
        return average;
    }
}


