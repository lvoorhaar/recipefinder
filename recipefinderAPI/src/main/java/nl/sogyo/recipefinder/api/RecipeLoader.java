/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
    /**
	 * @param request
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession(true);
		RecipeCollection recipeCollection = new RecipeCollection();
                List<Recipe> recipes = recipeCollection.getRecipes();
		
		session.setAttribute("recipes", recipes);		
		
		//String output = new JSONResultProcessor().createJSONResponse(recipes);
		
		return Response.status(200).entity(output).build();
	}
}
