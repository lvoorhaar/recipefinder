/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.dbconnect;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.sogyo.recipefinder.main.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author lvoorhaar
 */
public class WebReader {

    public static Recipe getRecipeFromWebsite(String link) throws IOException {
        //read HTML from website
        Document doc = Jsoup.connect(link).get();

        //create recipe
        Elements nameElements = doc.getElementsByClass("wprm-recipe-name wprm-color-header");
        String name = nameElements.get(0).text();

        ArrayList<Category> categories = new ArrayList<>();
        try {
            Elements categoryElements = doc.getElementsByClass("wprm-recipe-course");
            categories.add(Category.valueOf(categoryElements.get(0).text().trim().toUpperCase()));
        } catch (Exception e) {
            categories.add(Category.valueOf("UNKNOWN"));
        }

        Elements timeElements = doc.getElementsByClass("wprm-recipe-time");
        int preptime;
        try {
            preptime = Integer.parseInt(timeElements.get(2).child(0).text());
            if (timeElements.get(2).child(1).text().equals("hr")) {
                preptime *= 60;
            }
        } catch (Exception IndexOutOfBoundsException) {
            preptime = Integer.parseInt(timeElements.get(1).child(0).text());
        }

        Elements ratingElements = doc.getElementsByClass("wprm-recipe-rating-average");
        int rating = Integer.parseInt(ratingElements.get(0).text().substring(0, 1));

        String instructions = "";
        Elements instructionElements = doc.getElementsByClass("wprm-recipe-instruction-text");
        for (Element e : instructionElements) {
            instructions += e.text() + "<br><br>";
        }

        //get ingredients
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Elements ingredientsElements = doc.getElementsByClass("wprm-recipe-ingredient");
        for (Element e : ingredientsElements) {
            Ingredient i = new Ingredient();
            if (!e.getElementsByClass("wprm-recipe-ingredient-amount").isEmpty()) {
                i.setAmount(e.getElementsByClass("wprm-recipe-ingredient-amount").get(0).text());
            } else {
                i.setAmount("1");
            }
            if (!e.getElementsByClass("wprm-recipe-ingredient-unit").isEmpty()) {
                i.setUnit(e.getElementsByClass("wprm-recipe-ingredient-unit").get(0).text());
            } else {
                i.setUnit("(number)");
            }
            String ingredientName = e.getElementsByClass("wprm-recipe-ingredient-name").get(0).text();
            if (!e.getElementsByClass("wprm-recipe-ingredient-notes").isEmpty()) {
                ingredientName += e.getElementsByClass("wprm-recipe-ingredient-notes").get(0).text();
            }
            i.setName(ingredientName);
            ingredients.add(i);
        }

        Recipe importedRecipe = new Recipe(name, categories, preptime, rating, instructions, ingredients);

        return importedRecipe;
    }

    public static void main(String[] args) throws Exception {

        Recipe importedRecipe = WebReader.getRecipeFromWebsite("https://www.biggerbolderbaking.com/neapolitan-ice-cream/");

        System.out.println(importedRecipe);

        //add recipe to MongoDB collection
        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();
        datastore.save(importedRecipe);

    }

}
