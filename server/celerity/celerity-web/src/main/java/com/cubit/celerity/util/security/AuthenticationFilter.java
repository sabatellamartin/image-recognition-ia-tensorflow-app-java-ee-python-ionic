package com.cubit.celerity.util.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.json.JSONObject;

import com.cubit.celerity.model.Client;
import com.cubit.celerity.model.User;
import com.cubit.celerity.service.iservice.IUserService;
import com.cubit.celerity.util.Constants;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	@Inject
    private IUserService uservice;

    @Inject
    @AuthenticatedUser
    Event<String> userAuthenticatedEvent;
	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = "" ; // Initialize variable token	
    	// Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null /*|| !authorizationHeader.startsWith("Bearer ")*/) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        if (authorizationHeader.startsWith("Bearer ")) {
        	// Extract the token from the HTTP Authorization header
	        token = authorizationHeader.substring("Bearer".length()).trim();
	        try {
	            validateToken(token);
	        } catch (Exception e) {
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
	        }
        } else if (authorizationHeader.startsWith("Google ")) {
        	token = authorizationHeader.substring("Google".length()).trim();
	        try {
	            validateTokenGoogle(token);
	        } catch (Exception e) {
	        	System.out.println(e);
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
	        }
        } else if (authorizationHeader.startsWith("Facebook ")) {
        	token = authorizationHeader.substring("Facebook".length()).trim();
	        try {
	            validateTokenFacebook(token);
	        } catch (Exception e) {
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
	        }
        } else {
        	throw new NotAuthorizedException("Invalid token type");
        }
    }

	private void validateToken(String token) throws Exception {
    	// Check if it was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
    	Jws<Claims> claims = Jwts.parser().setSigningKey(Constants.TOKEN_KEY.getBytes("UTF-8")).parseClaimsJws(token);
    	String subject = claims.getBody().getSubject();
    	//String email = (String) claims.getBody().get("email");
    	User usuario = uservice.getByEmail(subject);
    	assert subject.equals(usuario.getEmail());
    	// Save the userAuthenticateEvent
    	userAuthenticatedEvent.fire(usuario.getEmail());
    }
    
    private void validateTokenFacebook(String token) {
		
		ClientConfig clientConfig = new DefaultClientConfig();
	    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
	    com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
	    
		String url = "https://graph.facebook.com/me?fields=id,name,email,first_name,last_name,picture,gender&access_token=" + token;
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON + ";charset=utf-8").get(ClientResponse.class);
        
		if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }

        String output = response.getEntity(String.class);
        JSONObject payload = new JSONObject(output);
		
		// Get profile information from payload
		String email = (String) payload.get("email");
		//boolean emailVerified = Boolean.valueOf((boolean) payload.get("email_verified"));
		//String username = ((String) payload.get("name")).trim().toLowerCase().replaceAll("\\s+","");
		// Picture url changes every login session
		String picture = (String) payload.getJSONObject("picture").getJSONObject("data").get("url");
		String lastname = (String) payload.get("last_name");
		String firstname = (String) payload.get("first_name");
		Client user = new Client();
		if (email.compareToIgnoreCase("")!=0) {
			user = saveClient("Facebook", email, /*username,*/ firstname, lastname, picture);
		}
		assert email.equals(user.getEmail());
		// Save the userAuthenticateEvent
		userAuthenticatedEvent.fire(user.getEmail());
	}

	private void validateTokenGoogle(String token) throws UnsupportedEncodingException {
		
		ClientConfig clientConfig = new DefaultClientConfig();
	    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
	    com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
	    
		String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token;
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON + ";charset=utf-8").get(ClientResponse.class);
        
		if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }

        String output = response.getEntity(String.class);
		JSONObject payload = new JSONObject(output);
	
		// Get profile information from payload
		String email = (String) payload.get("email");
		boolean emailVerified = Boolean.valueOf((boolean) payload.get("email_verified"));
		//String username = ((String) payload.get("name")).trim().toLowerCase().replaceAll("\\s+","");
		String picture = (String) payload.get("picture");
		String lastname = (String) payload.get("family_name");
		String firstname = (String) payload.get("given_name");
		/*String gender = (String) payload.get("gender");
		String profile = (String) payload.get("profile");
		String locale = (String) payload.get("locale");*/
		Client user = new Client();
		if (emailVerified) {
			user = saveClient("Google", email, /*username,*/ firstname, lastname, picture);
		}
		assert email.equals(user.getEmail());
		// Save the userAuthenticateEvent
		userAuthenticatedEvent.fire(user.getEmail());
	}
	
	private Client saveClient(String type, String email, /*String username,*/ String firstname, String lastname, String picture) {
		Client user = new Client();
		if (uservice.existByEmail(email)) {
			user = (Client) uservice.getByEmail(email);
			if (user.getFirstname().compareToIgnoreCase(firstname)!=0 ||
				user.getLastname().compareToIgnoreCase(lastname)!=0 ||
				/*user.getUsername().compareToIgnoreCase(username) !=0 ||*/
				user.getPicture().compareToIgnoreCase(picture)!=0) {
				//user.setUsername(username);
				user.setPicture(picture);
				user.setFirstname(firstname);
				user.setLastname(lastname);
				uservice.update(user);
				System.out.println("Update " + type + " user " + email);
			}
		} else {
			user.setCreation(new Date());
			user.setEmail(email);
			//user.setUsername(username);
			user.setPicture(picture);
			user.setFirstname(firstname);
			user.setLastname(lastname);
			uservice.add((Client)user);
			System.out.println("Add " + type + " user " + email);
		}
		return user;
	}
	
}

