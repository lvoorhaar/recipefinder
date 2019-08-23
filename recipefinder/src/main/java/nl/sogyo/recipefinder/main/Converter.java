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

    public static Ingredient convertToUSUnits(Ingredient originalIngredient) {
        if (originalIngredient.getAmount() != null && originalIngredient.getUnit() != null) {
            String amount = originalIngredient.getAmount();
            String unit = originalIngredient.getUnit();
            String name = originalIngredient.getName();
            String notes = originalIngredient.getNotes();
            String newAmount;
            String newUnit;
            if (unit.toLowerCase().contains("ml")) {
                if (fractionToDouble(amount) >= 30) {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / 240);
                    newUnit = "cup";
                } else if (fractionToDouble(amount) > 10) {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / 15);
                    newUnit = "tbsp";
                } else {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / 5);
                    newUnit = "tsp";
                }
            } else if (unit.toLowerCase().contains("dl")) {
                if (fractionToDouble(amount) >= 3) {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / 24);
                    newUnit = "cup";
                } else if (fractionToDouble(amount) > 1) {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / 15);
                    newUnit = "tbsp";
                } else {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / 0.5);
                    newUnit = "tsp";
                }
            } else if (unit.toLowerCase().equals("l") || unit.toLowerCase().equals("liter") || unit.toLowerCase().equals("litre")) {
                newAmount = doubleToFractionString(fractionToDouble(amount) / 0.24);
                newUnit = "cup";
            } else if (unit.toLowerCase().contains("kg") || unit.toLowerCase().contains("kilogram")) {
                if (Converter.densities.containsKey(name)) {
                    newAmount = doubleToFractionString((fractionToDouble(amount) * 1000) / (240 * Converter.densities.get(name)));
                    newUnit = "cup";
                } else {
                    newAmount = amount;
                    newUnit = unit;
                }
            } else if (unit.toLowerCase().equals("g") || unit.toLowerCase().contains("gram")) {
                if (Converter.densities.containsKey(name)) {
                    newAmount = doubleToFractionString(fractionToDouble(amount) / (240 * Converter.densities.get(name)));
                    newUnit = "cup";
                } else {
                    newAmount = amount;
                    newUnit = unit;
                }
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

    public static Ingredient convertToMetricUnits(Ingredient originalIngredient) {
        if (originalIngredient.getAmount() != null && originalIngredient.getUnit() != null) {
            String amount = originalIngredient.getAmount();
            String unit = originalIngredient.getUnit();
            String name = originalIngredient.getName();
            String notes = originalIngredient.getNotes();
            String newAmount;
            String newUnit;
            double doubleAmount;
            if (unit.toLowerCase().contains("cup")) {
                doubleAmount = fractionToDouble(amount) * 240;
                newUnit = "mL";
                if (Converter.densities.containsKey(name)) {
                    doubleAmount *= Converter.densities.get(name);
                    newUnit = "g";
                }
                newAmount = String.format("%.0f", doubleAmount);
            } else if ((unit.toLowerCase().contains("tbsp") || unit.toLowerCase().contains("tablespoon")) && fractionToDouble(amount) > 2) {
                doubleAmount = fractionToDouble(amount) * 15;
                newUnit = "mL";
                if (Converter.densities.containsKey(name)) {
                    doubleAmount *= Converter.densities.get(name);
                    newUnit = "g";
                }
                newAmount = String.format("%.0f", doubleAmount);
            /*} else if (unit.toLowerCase().contains("tsp") || unit.toLowerCase().contains("teaspoon")) {
                doubleAmount = fractionToDouble(amount) * 5;
                newUnit = "mL";
                if (Converter.densities.containsKey(name)) {
                    doubleAmount *= Converter.densities.get(name);
                    newUnit = "g";
                }
                newAmount = String.format("%.1f", doubleAmount);*/
            } else if (unit.toLowerCase().contains("can") && (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounce")) 
                    && (unit.toLowerCase().contains("15") || unit.toLowerCase().contains("14"))) {
                doubleAmount = fractionToDouble(amount) * 400;
                newUnit = "g";
                newAmount = String.format("%.0f", doubleAmount);
            } else if (unit.toLowerCase().contains("can") && (unit.toLowerCase().contains("oz") || unit.toLowerCase().contains("ounce")) 
                    && unit.toLowerCase().contains("28")) {
                doubleAmount = fractionToDouble(amount) * 800;
                newUnit = "g";
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
    
    static String doubleToFractionString(double d) {
        String result = "";
        double decimals = d % 1;
        if (d >= 1.0) {
            if (decimals < 0.94) {
                result += (int)d + " ";
            } else {
                result += (int)(d + 1);
            }
        }
        if (decimals <= 0.06) {
        } else if (decimals <= 0.19) {
            result += "1/8";
        } else if (decimals <= 0.29) {
            result += "1/4";
        } else if (decimals <= 0.35) {
            result += "1/3";
        } else if (decimals <= 0.44) {
            result += "3/8";
        } else if (decimals <= 0.56) {
            result += "1/2";
        } else if (decimals <= 0.65) {
            result += "5/8";
        } else if (decimals <= 0.71) {
            result += "2/3";
        } else if (decimals <= 0.81) {
            result += "3/4";
        } else if (decimals <= 0.94) {
            result += "7/8";
        }
        return result.trim();
    }

    private static final Map<String, Double> densities;

    static {
        Map<String, Double> aMap = new HashMap<>();
        aMap.put("all-purpose flour", 0.625);
        aMap.put("flour", 0.625);
        aMap.put("wholemeal flour", 0.51);
        aMap.put("whole wheat flour", 0.51);
        aMap.put("buckwheat flour", 0.66);
        aMap.put("cornflour", 0.55);
        aMap.put("oat flour", 0.53);
        aMap.put("cake flour", 0.5);
        aMap.put("almond flour", 0.4);
        aMap.put("sugar", 0.95);
        aMap.put("white sugar", 0.9);
        aMap.put("raw sugar", 0.95);
        aMap.put("unrefined sugar", 0.95);
        aMap.put("cane sugar", 0.95);
        aMap.put("coconut sugar", 0.81);
        aMap.put("palm sugar", 0.91);
        aMap.put("caster sugar", 0.94);
        aMap.put("granulated sugar", 0.7);
        aMap.put("brown sugar", 0.75);
        aMap.put("light brown sugar", 0.75);
        aMap.put("powdered sugar", 0.56);
        aMap.put("icing sugar", 0.5);
        aMap.put("confectioners' sugar", 0.5);
        aMap.put("cocoa powder", 0.5);
        aMap.put("cacao nibs", 0.68);
        aMap.put("instant dry yeast", 0.62);
        aMap.put("butter", 0.94);
        aMap.put("unsalted butter", 0.94);
        aMap.put("vegan butter", 0.94);
        aMap.put("chocolate", 0.71);
        aMap.put("peanut butter", 1.08);
        aMap.put("nut butter", 1.08);
        aMap.put("almond butter", 1.08);
        aMap.put("cashew butter", 1.08);
        aMap.put("nutella", 1.2);
        aMap.put("salt", 2.17);
        aMap.put("sea salt", 2.17);
        aMap.put("cornstarch", 0.54);
        aMap.put("pasta", 0.50);
        aMap.put("macaroni", 0.55);
        aMap.put("spaghetti", 0.38);
        aMap.put("cheese", 0.34);
        aMap.put("oats", 0.35);
        aMap.put("rolled oats", 0.35);
        aMap.put("coconut", 0.35);
        aMap.put("baking powder", 0.9);
        aMap.put("baking soda", 1.2);
        aMap.put("protein powder", 0.41);
        aMap.put("tahini", 1.01);
        aMap.put("chia seeds", 0.81);
        aMap.put("hemp seeds", 0.68);
        aMap.put("flaxseeds", 0.72);
        aMap.put("sunflower seeds", 0.62);
        aMap.put("cinnamon", 0.53);
        aMap.put("curry powder", 0.43);
        aMap.put("garlic powder", 0.32);
        aMap.put("pecans", 0.46);
        aMap.put("almonds", 0.46);
        aMap.put("cashews", 0.5);
        aMap.put("peanuts", 0.53);
        aMap.put("rice", 0.72);
        aMap.put("white rice", 0.72);
        aMap.put("brown rice", 0.72);
        aMap.put("wild rice", 0.72);
        aMap.put("arborio rice", 0.72);
        aMap.put("brown onions", 0.7);
        aMap.put("red onions", 0.7);
        aMap.put("red onion", 0.7);
        aMap.put("onion", 0.7);
        aMap.put("shallot", 0.7);
        aMap.put("strawberries", 0.71);
        aMap.put("blueberries", 0.71);
        aMap.put("pineapple", 0.5);
        aMap.put("carrots", 0.54);
        aMap.put("corn", 0.62);
        aMap.put("bell pepper", 0.6);
        aMap.put("bell peppers", 0.6);
        aMap.put("red bell pepper", 0.6);
        aMap.put("red bell peppers", 0.6);
        aMap.put("green bell pepper", 0.6);
        aMap.put("green bell peppers", 0.6);
        aMap.put("potato", 0.63);
        aMap.put("potatoes", 0.63);
        aMap.put("sweet potato", 0.63);
        aMap.put("sweet potatoes", 0.63);
        aMap.put("kale", 0.089);
        aMap.put("cabbage", 0.45);
        aMap.put("cauliflower", 0.45);
        aMap.put("broccoli", 0.45);
        aMap.put("crushed tomatoes", 1.0);
        aMap.put("diced tomatoes", 1.0);
        aMap.put("shiitake mushrooms", 0.3);
        aMap.put("mushrooms", 0.3);
        aMap.put("baby spinach", 0.18);
        aMap.put("spinach", 0.24);
        aMap.put("ginger", 1.01);
        aMap.put("garlic", 1.01);
        aMap.put("dry kidney beans", 0.72);
        aMap.put("kidney beans", 1.0);
        aMap.put("dry lentils", 0.81);
        aMap.put("red lentils", 0.81);
        aMap.put("lentils", 0.89);
        aMap.put("dry soybeans", 0.74);
        aMap.put("dry chickpeas", 0.74);
        aMap.put("chickpeas", 0.74);
        aMap.put("split mung beans", 0.86);
        aMap.put("mung beans", 0.86);
        aMap.put("gelatin", 1.27);
        aMap.put("honey", 1.42);
        aMap.put("cilantro", 0.0833);
        aMap.put("parsley", 0.0833);
        aMap.put("avocado", 1.0);
        aMap.put("lady finger cookies", 0.375);
        aMap.put("cream cheese", 1.01);
        densities = Collections.unmodifiableMap(aMap);
    }
}
