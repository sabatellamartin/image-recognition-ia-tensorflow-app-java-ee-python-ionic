/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cubit.celerity.rest;

import java.io.UnsupportedEncodingException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cubit.celerity.model.Client;
import com.cubit.celerity.model.User;
import com.cubit.celerity.service.iservice.IUserService;
import com.cubit.celerity.util.Constants;
import com.cubit.celerity.util.security.AuthenticatedUser;
import com.cubit.celerity.util.security.Credentials;
import com.cubit.celerity.util.security.Role;
import com.cubit.celerity.util.security.Secured;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/authentication")
@RequestScoped
public class AuthenticationRESTService {
    
    @Inject
    private IUserService uservice;
    
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

	@GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({Role.CLIENT})
    public Response getCurrent() {
    	Client client = (Client) uservice.getByEmail(authenticatedUser.getEmail());
    	client.setPassword("");
        return Response.ok(client).build();
    }
	
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response authenticateUser(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
    	try {
            // Authenticate the user using the credentials provided
            Boolean result = authenticate(username, password);
            // Issue a token for the user
            String token = issueToken(username);
            // Return the token on the response
            return result ? Response.ok(token).build(): Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    
    private Boolean authenticate(String username, String password) throws Exception {
    	Boolean result = true;
    	// Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    	if (!uservice.login(username, password)) {
    		//throw new IllegalArgumentException("Invalid credentials");
    		result = false;
    	}
    	return result;
    }

    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    	// We need a signing key, so we'll create one just for this example. Usually
    	// the key would be read from your application configuration instead.
    	String token = "";
    	// Como username se pasa la cedula
    	User user = uservice.getByEmail(username);
    	String rol = uservice.getRolByEmail(user.getEmail());
    	/* Para habilitar la expiracion del token */
    	// Long exp = System.currentTimeMillis() / 1000L;
    	// exp += 60*60*24; // Sumo 24 horas en segundos
    	try {
			token = Jwts.builder()
					.setSubject(user.getEmail())
					//.setExpiration(new Date(exp))
					.claim("username", user.getEmail())
					.claim("email", user.getEmail())
					.claim("scope", rol)
					.signWith(
						SignatureAlgorithm.HS256,
						Constants.TOKEN_KEY.getBytes("UTF-8")
					).compact();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return token;
    }

}

