package com.cubit.celerity.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cubit.celerity.model.Setting;
import com.cubit.celerity.model.User;
import com.cubit.celerity.model.translation.Lang;
import com.cubit.celerity.service.iservice.ISettingService;
import com.cubit.celerity.util.security.AuthenticatedUser;
import com.cubit.celerity.util.security.Role;
import com.cubit.celerity.util.security.Secured;

@Path("/settings")
@RequestScoped
public class SettingRESTService {
    
    @Inject
    private ISettingService settingService;
    
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response getCurrent() {
    	Setting setting = settingService.getCurrent(this.authenticatedUser);
        if (setting != null) {
            return Response.ok(setting).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("/language")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response setLanguage(String language) {
    	Setting setting = this.settingService.setLanguage(this.authenticatedUser, language);
        if (setting != null) {
            return Response.ok(setting).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("/traduction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response setTraduction(String traduction) {
    	Setting setting = this.settingService.setTraduction(this.authenticatedUser, traduction);
        if (setting != null) {
            return Response.ok(setting).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/languages")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response getLanguages() {
    	List<Lang> langs = this.settingService.getLanguages();
        if (langs != null) {
            return Response.ok(langs).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
}
