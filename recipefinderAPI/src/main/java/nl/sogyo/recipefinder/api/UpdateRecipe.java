/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.sogyo.recipefinder.main.Recipe;
import org.bson.types.ObjectId;

/**
 *
 * @author lvoorhaar
 */
@Path("updaterecipe")
public class UpdateRecipe {
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{recipeId}")
    public Response searchRecipes(
            @Context HttpServletRequest request,
            @PathParam("recipeId") String recipeId,
            Map<String, Object> recipeDetails) {

        System.out.println("received updated recipe");
        HttpSession session = request.getSession(true);
        
        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();

        ObjectId objectId = new ObjectId(recipeId);
        Recipe updatedRecipe = new Recipe(recipeDetails);
        updatedRecipe.setId(objectId);
        datastore.save(updatedRecipe);
        
        String output = new Gson().toJson("recipe updated succesfully");

        return Response.status(200).entity(output).build();
    }
}
