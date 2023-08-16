package com.cubit.celerity.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
@RequestScoped
public class TestRESTService {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String test() {
    	System.out.println("Hola test");
    	return "Hello test";
    }

}
