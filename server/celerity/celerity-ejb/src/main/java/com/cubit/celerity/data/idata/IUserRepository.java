package com.cubit.celerity.data.idata;

import java.util.List;

import com.cubit.celerity.model.Client;
import com.cubit.celerity.model.User;

public interface IUserRepository {

	public Boolean add(User user);

	public Boolean update(User user);

	public Boolean removeById(Long id);

	public Boolean remove(User user);

	public User getById(Long id);

	public User getByEmail(String email);
	
	//public User getByUsername(String username);
	
	public List<User> getAll();
	
	public List<User> getAllOrderedByUsername();

	public List<Client> getAllClients();

	public List<User> searchByRol(String term, String rol); 

	public List<User> search(String term);

	public void saveLogin(String username, Boolean result);
		
}
