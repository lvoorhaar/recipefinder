/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.sogyo.recipefinder.main.*;

/**
 *
 * @author lvoorhaar
 */
@Path("searchrecipes")
public class RecipeSearcher {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRecipes(
            @Context HttpServletRequest request,
            Map<String, Object> searchDetails) {

        System.out.println("received search request");
        HttpSession session = request.getSession(true);
        
        ArrayList<String> ingredients = (ArrayList<String>)searchDetails.get("ingredients");
        ArrayList<String> categories = (ArrayList<String>)searchDetails.get("categories");
        int maxPreptime = Integer.parseInt((String)searchDetails.get("maxPreptime"));
        
        RecipeSearch search;
        if (categories.contains("all")) {
            if (maxPreptime > 0) {
                search = new RecipeSearch(ingredients, maxPreptime);
            } else {
                search = new RecipeSearch(ingredients);
            }
        } else {
            if (maxPreptime > 0) {
                search = new RecipeSearch(ingredients, categories, maxPreptime);
            } else {
                search = new RecipeSearch(ingredients, categories);
            }
        }
        
        List<Recipe> recipes = search.sortRecipes();
        
        String output = new Gson().toJson(recipes);

        return Response.status(200).entity(output).build();
    }
}
