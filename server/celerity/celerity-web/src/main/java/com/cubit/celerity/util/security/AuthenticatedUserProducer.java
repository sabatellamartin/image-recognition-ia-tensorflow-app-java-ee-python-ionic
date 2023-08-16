package com.cubit.celerity.util.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.cubit.celerity.model.User;
import com.cubit.celerity.service.iservice.IUserService;

@RequestScoped
public class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private User authenticatedUser;

	@Inject
    private IUserService uservice;
	
    public void handleAuthenticationEvent(@Observes @AuthenticatedUser String email) {
        this.authenticatedUser = this.findUser(email);
    }

    private User findUser(String email) {
        // Hit the the database or a service to find a user by its username and return it
        // Return the User instance
    	return uservice.getByEmail(email);
    }
}