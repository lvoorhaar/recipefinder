/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.sogyo.recipefinder.api;

import com.google.gson.Gson;
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
import nl.sogyo.recipefinder.dbconnect.ImportHandler;

/**
 *
 * @author lvoorhaar
 */
@Path("importrecipe")
public class ImportRecipe {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importRecipe(
            @Context HttpServletRequest request,
            Map<String, String> source) {
        
        String weblink = source.get("source");
        HttpSession session = request.getSession(true);
        
        ImportHandler importHandler = new ImportHandler();
        String output;
        
        try {
            importHandler.importRecipeFromLink(weblink);
            output = new Gson().toJson("Recipe imported succesfully");
        } catch (Exception e) {
            output = new Gson().toJson("An error occured");
        }

        return Response.status(200).entity(output).build();
    }
}
