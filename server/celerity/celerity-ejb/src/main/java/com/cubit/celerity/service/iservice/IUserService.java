package com.cubit.celerity.service.iservice;

import java.util.List;

import com.cubit.celerity.model.Client;
import com.cubit.celerity.model.User;

public interface IUserService {
	
	public Boolean login(String username, String password);

	public Boolean register(User user);
	
	public Boolean add(User user);

	public Boolean update(User user);

	public Boolean removeById(Long id);

	public Boolean remove(User user);

	public User getById(Long id);

	public User getByEmail(String email);
	
	//public User getByUsername(String username);

	public Boolean existByEmail(String email);
	
	/*public Boolean existByUsername(String username);*/

	public List<User> getAll();

	public List<User> getAllOrderedByUsername();

	public List<User> search(String term);

	public String getRolByEmail(String email);

	public List<User> searchByRol(String term, String rol);
	
	public List<Client> getAllClients();

	public Boolean toggleLockById(Long id);

	public Boolean setPassword(Client user);
	
}
