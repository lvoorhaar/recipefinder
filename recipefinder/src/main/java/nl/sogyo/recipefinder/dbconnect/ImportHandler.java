/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.dbconnect;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import java.io.IOException;
import java.util.ArrayList;
import nl.sogyo.recipefinder.main.Category;
import nl.sogyo.recipefinder.main.Ingredient;
import nl.sogyo.recipefinder.main.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author lvoorhaar
 */
public class ImportHandler {
    
    private Morphia morphia;
    private Datastore datastore;
    
    public ImportHandler() {
        this.morphia = new Morphia();
        this.morphia.mapPackage("nl.sogyo.recipefinder.main");
        this.datastore = morphia.createDatastore(new MongoClient(), "recipes");
        this.datastore.ensureIndexes();
    }
    
    public void importRecipeFromLink(String link) throws IOException {
        Recipe importedRecipe = createRecipe(link);
        this.datastore.save(importedRecipe);
        //System.out.println("saved recipe");
    }
    
    Recipe createRecipe(String link) throws IOException {
        //read HTML from website
        Document doc = Jsoup.connect(link).get();

        //create recipe
        Elements nameElements = doc.getElementsByClass("wprm-recipe-name");
        if (nameElements == null) {
            nameElements = doc.getElementsByClass("wprm-recipe-name wprm-color-header");
        }
        String name = nameElements.get(0).text();
        
        //System.out.println("set name");
        
        ArrayList<Category> categories = new ArrayList<>();
        try {
            Elements categoryElements = doc.getElementsByClass("wprm-recipe-course");
            categories.add(Category.valueOf(categoryElements.get(0).text().trim().toUpperCase()));
        } catch (Exception e) {
            categories.add(Category.valueOf("OTHER"));
        }
        
        //System.out.println("set categories");
        
        Elements timeElements = doc.getElementsByClass("wprm-recipe-time");
        int preptime;
        if (timeElements.size() > 1) {
            try {
                preptime = Integer.parseInt(timeElements.get(2).child(0).text());
                if (timeElements.get(2).child(1).text().equals("hr")) {
                    preptime *= 60;
                }
            } catch (Exception IndexOutOfBoundsException) {
                preptime = Integer.parseInt(timeElements.get(1).child(0).text());
            }
        } else {
            timeElements = doc.getElementsByClass("wprm-recipe-details wprm-recipe-details-minutes wprm-recipe-total_time wprm-recipe-total_time-minutes");
            if (timeElements.size() == 0) {
                timeElements = doc.getElementsByClass("wprm-recipe-details wprm-recipe-details-hours wprm-recipe-total_time wprm-recipe-total_time-hours");
                preptime = Integer.parseInt(timeElements.get(0).text()) * 60;
            } else {
                preptime = Integer.parseInt(timeElements.get(0).text());
            }
        }
        
        //System.out.println("set preptime");
        
        Elements ratingElements = doc.getElementsByClass("wprm-recipe-rating-average");
        double importedrating = Double.parseDouble(ratingElements.get(0).text().substring(0));
        int rating = (int)Math.round(importedrating);
        
        //System.out.println("set rating");

        String instructions = "";
        Elements instructionElements = doc.getElementsByClass("wprm-recipe-instruction-text");
        for (Element e : instructionElements) {
            instructions += e.text() + "<br><br>";
        }
        
        //System.out.println("set instructions");

        //get ingredients
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Elements ingredientsElements = doc.getElementsByClass("wprm-recipe-ingredient");
        for (Element e : ingredientsElements) {
            Ingredient i = new Ingredient();
            if (!e.getElementsByClass("wprm-recipe-ingredient-amount").isEmpty()) {
                i.setAmount(e.getElementsByClass("wprm-recipe-ingredient-amount").get(0).text().trim());
            }
            if (!e.getElementsByClass("wprm-recipe-ingredient-unit").isEmpty()) {
                i.setUnit(e.getElementsByClass("wprm-recipe-ingredient-unit").get(0).text().trim());
            }
            String ingredientName = e.getElementsByClass("wprm-recipe-ingredient-name").get(0).text().trim().toLowerCase();
            i.setName(ingredientName);
            if (!e.getElementsByClass("wprm-recipe-ingredient-notes").isEmpty()) {
                i.setNotes(e.getElementsByClass("wprm-recipe-ingredient-notes").get(0).text());
            }
            ingredients.add(i);
        }
        
        //System.out.println("set ingredients");
        
        String source = link;

        Recipe importedRecipe = new Recipe(name, categories, preptime, rating, instructions, ingredients, source);
        
        //System.out.println("created recipe");

        return importedRecipe;
    }
}
