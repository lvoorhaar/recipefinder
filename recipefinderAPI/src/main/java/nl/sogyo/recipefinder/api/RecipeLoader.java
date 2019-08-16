/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import com.google.gson.Gson;
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
@Path("loadrecipes")
public class RecipeLoader {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadRecipes(
            @Context HttpServletRequest request) {

        System.out.println("received fetch recipes call");
        HttpSession session = request.getSession(true);
        RecipeCollection recipeCollection = new RecipeCollection();
        List<Recipe> recipes = recipeCollection.getRecipes();
        Collections.shuffle(recipes);
        
        String output = new Gson().toJson(recipes);

        return Response.status(200).entity(output).build();
    }
}
