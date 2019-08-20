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
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
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
@Path("loadsinglerecipe")
public class LoadSingleRecipe {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{recipeId}")
    public Response playGet(
            @PathParam("recipeId") String recipeId,
            @Context HttpServletRequest request) {

        System.out.println("received recipe call");
        HttpSession session = request.getSession(true);

        Morphia morphia = new Morphia();
        morphia.mapPackage("nl.sogyo.recipefinder.main");
        Datastore datastore = morphia.createDatastore(new MongoClient(), "recipes");
        datastore.ensureIndexes();

        Query<Recipe> query = datastore.createQuery(Recipe.class);
        ObjectId objectId = new ObjectId(recipeId);

        query.criteria("_id").equal(objectId);
        Recipe recipe = query.first();
        recipe.setStringID(recipe.getId().toHexString());

        String output = new Gson().toJson(recipe);

        return Response.status(200).entity(output).build();
    }
}
