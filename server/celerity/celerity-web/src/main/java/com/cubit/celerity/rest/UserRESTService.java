package com.cubit.celerity.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.cubit.celerity.model.Client;
import com.cubit.celerity.service.iservice.IUserService;
import com.cubit.celerity.util.security.Role;
import com.cubit.celerity.util.security.Secured;


@Path("/users")
@RequestScoped
public class UserRESTService {

    @Inject
    private IUserService uservice;
 
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Client usuario) {
    	Boolean result = uservice.register(usuario);
    	
    	System.out.println(usuario.getEmail());
    	System.out.println(usuario.getPassword());
    	
        if (result) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("/update/client/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response updateClient(@PathParam("id") Long id, Client client) {
    	Boolean result = uservice.update(client);
		Client user = (Client) (result ? uservice.getById(id) : null);
    	if (user!=null) {
    		return Response.ok(user).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("/set/password/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response setPassword(@PathParam("id") Long id, Client user) {
    	Boolean result = uservice.setPassword(user);
    	if (result) {
    		return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
/*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR})
    public Response getAll() {
        List<User> usuarios = uservice.getAll();
        if (usuarios != null) {
            return Response.ok(usuarios).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/ordered")
    @Secured({Role.ADMINISTRADOR})
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllOrderedByUsername() {
        return uservice.getAllOrderedByUsername();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR, Role.OPERADOR})
    public Response getById(@PathParam("id") Long id) {
    	User usuario = null;
    	usuario = uservice.getById(id);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/search/{term}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR})
    public Response findByTerm(@PathParam("term") String term) {
    	List<User> usuarios = uservice.search(term);
        if (usuarios != null) {
            return Response.ok(usuarios).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/add/client")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR})
    public Response addClient(Client usuario) {
    	Boolean result = uservice.add(usuario);
        if (result != null) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/update/client/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response updateClient(@PathParam("id") Long id, Client usuario) {
    	Boolean result = uservice.update(usuario);
        if (result != null) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR, Role.OPERADOR})
    public Response getByEmail(@PathParam("email") String email) {
    	User usuario = uservice.getByEmail(email);
		if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
	@GET
	@Path("/exist/email/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({ Role.ADMINISTRADOR, Role.OPERADOR})
	public Response existByEmail(@PathParam("email") String email) {
		Boolean result = uservice.existByEmail(email);
		if (result != null) {
			return Response.ok(result).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
    
	@GET
    @Path("/search/rol/{term}/{rol}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR})
    public Response searchByRol(@PathParam("term") String term, @PathParam("rol") String rol) {
    	List<User> usuarios = uservice.searchByRol(term, rol);
        if (usuarios != null) {
            return Response.ok(usuarios).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
	
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR})
    public Response delete(@PathParam("id") Long id) {
    	Boolean result = uservice.removeById(id);
        if (result) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/email/client/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response getClientByEmail(@PathParam("email") String email) {
    	Client usuario = (Client) uservice.getByEmail(email);
		if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/toggle/lock/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.ADMINISTRADOR})
    public Response toggleLockById(@PathParam("id") Long id) {
    	Boolean result = uservice.toggleLockById(id);
        if (result) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
*/
    
}
