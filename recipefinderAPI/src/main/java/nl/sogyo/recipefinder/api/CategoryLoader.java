/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import com.google.gson.Gson;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.sogyo.recipefinder.main.Category;

/**
 *
 * @author lvoorhaar
 */
@Path("loadcategories")
public class CategoryLoader {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadCategories(
            @Context HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        System.out.println("received call for categories");
        
        ArrayList<String> categories = Category.getCategories();

        String output = new Gson().toJson(categories);

        return Response.status(200).entity(output).build();
    }
}
