/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.main;

import java.text.Normalizer;

/**
 *
 * @author lvoorhaar
 */
public class Converter {

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
            if (unit.toLowerCase().contains("tsp") || unit.toLowerCase().contains("teaspoon")) {
                newAmount = String.format("%.2f", fractionToDouble(amount) * 5);
                newUnit = "mL";
            } else if (unit.toLowerCase().contains("tbsp") || unit.toLowerCase().contains("tablespoon")) {
                newAmount = String.format("%.2f", fractionToDouble(amount) * 15);
                newUnit = "mL";
            } else if (unit.toLowerCase().contains("cup")) {
                newAmount = String.format("%.2f", fractionToDouble(amount) * 240);
                newUnit = "mL";
            } else if (unit.toLowerCase().contains("fl") && (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounce"))) {
                newAmount = String.format("%.2f", fractionToDouble(amount) * 30);
                newUnit = "mL";
            } else if (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounce")) {
                newAmount = String.format("%.2f", fractionToDouble(amount) * 28.35);
                newUnit = "g";
            } else if (unit.toLowerCase().contains("inch") && !unit.toLowerCase().contains("pinch")) {
                newAmount = String.format("%.2f", fractionToDouble(amount) * 2.54);
                newUnit = "cm";
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


