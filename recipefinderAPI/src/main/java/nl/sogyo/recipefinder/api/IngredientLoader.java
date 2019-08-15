/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.sogyo.recipefinder.main.Recipe;
import nl.sogyo.recipefinder.main.RecipeCollection;

/**
 *
 * @author lvoorhaar
 */
@Path("loadingredients")
public class IngredientLoader {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadIngredients(
            @Context HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        System.out.println("received call for ingredients");
       
        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();
        
        ArrayList<String> ingredients = new ArrayList<>();
        MongoCursor<String> values = datastore.getDatabase().getCollection("recipes-testdb").distinct("ingredients.name", String.class).iterator();
        while (values.hasNext()) {
            ingredients.add(values.next());
        }
        Collections.sort(ingredients);
        
        String output = new Gson().toJson(ingredients);

        return Response.status(200).entity(output).build();
    }
}
