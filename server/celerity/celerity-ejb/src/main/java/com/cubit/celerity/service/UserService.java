package com.cubit.celerity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.cubit.celerity.data.idata.IUserRepository;
import com.cubit.celerity.model.Client;
import com.cubit.celerity.model.Setting;
import com.cubit.celerity.model.User;
import com.cubit.celerity.service.iservice.IUserService;
import com.cubit.celerity.util.Constants;
import com.cubit.celerity.util.PasswordTool;

@Stateless
public class UserService implements IUserService {

	@Inject
	private IUserRepository urepo;
	
	@Inject
	private PasswordTool password;
	
	@Override
	public Boolean login(String username, String password) {
		Boolean result = false;
		if (password != null && username != null) {
			User u = this.getByEmail(username);
			if (u.getEmail() != null && !(u.getEmail().isEmpty())) {
				if (u.getLock()==null) {
					if (u.getPassword().compareToIgnoreCase(password) == 0) {
						result = true;
					}
				}
			}
		}
		urepo.saveLogin(username, result);
		return result;
	}

	@Override
	public Boolean register(User user) {
		Boolean result = false;
		user.setCreation(new Date());
		user.setSetting(new Setting());
		if (this.isValid(user)) {
			result = urepo.add(user);
		}
		return result;
	}
	
	@Override
	public Boolean add(User user) {
		Boolean result = false;
		// Generate clear random password
		String clear = this.password.randomPassword();
		user.setCreation(new Date());
		// Set Clear encrypt in SHA512
		user.setPassword(this.password.hashPassword(clear));
		user.setSetting(new Setting());
		// Validate & save
		if (this.isValid(user)) {
			result = urepo.add(user);
		}
		return result;
	}
	
	@Override
	public Boolean update(User user) {
		Boolean result = false;
		User old = this.getById(user.getId());
		if (old!=null) {
			user.setPassword(old.getPassword());
			result = urepo.update(user);
		}
		return result;
	}

	@Override
	public Boolean removeById(Long id) {
		return urepo.removeById(id);
	}

	@Override
	public Boolean remove(User user) {
		return urepo.remove(user);
	}
	
	@Override
	public User getById(Long id) {
		return urepo.getById(id);
	}

	@Override
	public User getByEmail(String email) {
		return urepo.getByEmail(email);
	}
	/*
	@Override
	public User getByUsername(String username) {
		return urepo.getByUsername(username);
	}
	*/
	@Override
	public List<User> getAll() {
		return urepo.getAll();
	}

	@Override
	public List<User> getAllOrderedByUsername() {
		return urepo.getAllOrderedByUsername();
	}

	@Override
	public List<User> search(String term) {
		List<User> users = new ArrayList<User>();
		if (term == null || term.compareToIgnoreCase("void") == 0) {
			users = this.getAll();
		} else {
			users = urepo.search(term);
		}
		return users;
	}
	
	@Override
	public String getRolByEmail(String email) {
		String rol = "";
		User user = this.getByEmail(email);
		if (user != null) {
			/*if (user instanceof Administrador) {
				rol = Constants.ADMINISTRADOR;
			} else if (user instanceof Operador) {
				rol = Constants.OPERADOR;
			} else */
			if (user instanceof Client) {
				rol = Constants.CLIENT;
			} else {
				rol = Constants.CLIENT;
			}
		}
		return rol;
	}

	@Override
	public Boolean existByEmail(String email) {
		Boolean result = false;
		User u = this.getByEmail(email);
		if (u != null) {
			result = true;
		}
		return result;
	}
	
	/*@Override
	public Boolean existByUsername(String username) {
		Boolean result = false;
		User u = this.getByUsername(username);
		if (u != null) {
			result = true;
		}
		return result;
	}*/
	
	@Override
	public List<User> searchByRol(String term, String rol) {
		List<User> users = new ArrayList<User>();
		users = urepo.searchByRol(term,rol);
		return users;
	}
	
	@Override
	public List<Client> getAllClients() {
		return urepo.getAllClients();
	}

	public Boolean isValid(User user) {
		Boolean result = false;
		if (!this.existByEmail(user.getEmail())) {
			result = true;
		}
		return result;
	}

	@Override
	public Boolean toggleLockById(Long id) {
		Boolean result = false;
		User user = this.getById(id);
		if (user!=null) {
			Date date = user.getLock()==null ? new Date() : null;
			user.setLock(date);			
			result = urepo.update(user);
		}
		return result;
	}

	@Override
	public Boolean setPassword(Client user) {
		Boolean result = false;
		Client aux = (Client) this.getById(user.getId());
		if (aux.getPassword().compareToIgnoreCase(user.getPassword())!=0) {
			aux.setPassword(user.getPassword());
			this.update(aux);
			result = true;
		}
		return result;
	}
	
}
