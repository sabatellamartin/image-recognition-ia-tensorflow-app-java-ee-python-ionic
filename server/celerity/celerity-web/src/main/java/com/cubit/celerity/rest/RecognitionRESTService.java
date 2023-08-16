package com.cubit.celerity.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cubit.celerity.model.User;
import com.cubit.celerity.model.recognition.Classify;
import com.cubit.celerity.model.recognition.Query;
import com.cubit.celerity.service.iservice.IRecognitionService;
import com.cubit.celerity.util.security.AuthenticatedUser;
import com.cubit.celerity.util.security.Role;
import com.cubit.celerity.util.security.Secured;

@Path("/recognition")
@RequestScoped
public class RecognitionRESTService {
    
    @Inject
    private IRecognitionService recognitionService;
    
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;
	
    @POST
    @Path("/upload/img/{traduction}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response uploadImage(@PathParam("traduction") String traduction, String encodedImage) {
    	Classify  classify = this.recognitionService.getClassify(traduction, encodedImage, this.authenticatedUser);
    	if (classify!=null) {
            return Response.ok(classify).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/classify/{traduction}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response classify(@PathParam("traduction") String traduction, String fileName) {
    	Classify classify = this.recognitionService.getClassifyByFileName(traduction, fileName, this.authenticatedUser);
    	if (classify!=null) {
            return Response.ok(classify).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response getActiveQuerys() {
        List<Query> querys = this.recognitionService.getActiveQuerysByUser(this.authenticatedUser);
        if (querys != null) {
            return Response.ok(querys).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @POST
    @Path("/encoded")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response getEncodedFileByName(String fileName) {
    	String encoded = this.recognitionService.getEncodedFileByName(fileName, this.authenticatedUser);
    	if (encoded!=null) {
            return Response.ok(encoded).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

}
