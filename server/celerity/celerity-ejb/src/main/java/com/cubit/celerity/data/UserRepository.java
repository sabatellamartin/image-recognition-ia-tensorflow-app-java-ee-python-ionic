package com.cubit.celerity.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.bson.Document;

import com.cubit.celerity.data.idata.IUserRepository;
import com.cubit.celerity.model.Client;
import com.cubit.celerity.model.User;
import com.cubit.celerity.util.Constants;
import com.cubit.celerity.util.MongoThread;
import com.mongodb.client.MongoCollection;

@Stateless
public class UserRepository implements IUserRepository {

	@PersistenceContext(unitName = Constants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@Inject
	private MongoThread mongoThread;
	
	public UserRepository() {}

	@Override
	public void saveLogin(String username, Boolean result) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		long unixTime = System.currentTimeMillis() / 1000L;
		Date date = new Date();
		MongoCollection<Document> collection = mongoThread.getDB().getCollection("logins");
		Document doc = new Document("username", username)
				.append("resultado", result)
				.append("timestamp", unixTime)
				.append("fecha", dateFormat.format(date));
		collection.insertOne(doc);
	}
	
	@Override
	public Boolean add(User user) {
		Boolean result = false;
		try {
			if (user instanceof Client) {
				em.persist((Client)user); 
			}
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean update(User user) {
		Boolean result = false;
		try {
			em.merge(user);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		return result;
	}

	@Override
	public Boolean remove(User user) {
		Boolean result = false;
		try {
			user = em.find(User.class, user.getId());
			em.remove(user);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		return result;
	}

	@Override
	public Boolean removeById(Long id) {
		Boolean result = false;
		try {
			User user = getById(id);
			result = remove(user);
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}
		return result;
	}

	@Override
	public User getById(Long id) {
		return em.find(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		return em.createQuery("FROM " + User.class.getName()).getResultList();
	}

	@Override
	public List<User> getAllOrderedByUsername() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> User = criteria.from(User.class);
		criteria.select(User).orderBy(cb.desc(User.get("username")));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public User getByEmail(String email) {
		User user = null;
		String sql = "FROM " + User.class.getName() + " u WHERE u.email =:email";
		List<?> result =  em.createQuery(sql).setParameter("email", email).getResultList();
		if (!result.isEmpty()){
			for(Object obj : result) {
				user = (User) obj;
			}
		}
		return user;
	}
	/*
	@Override
	public User getByUsername(String username) {
		User user = null;
		String sql = "FROM " + User.class.getName() + " u WHERE u.username =:username";
		List<?> result =  em.createQuery(sql).setParameter("username", username).getResultList();
		if (!result.isEmpty()){
			for(Object obj : result) {
				user = (User) obj;
			}
		}
		return user;
	}
	*/
	@Override
	public List<User> search(String term) {
		List<User> list = new ArrayList<User>();
		String sql = "FROM " + User.class.getName() + " u " 
				+ "WHERE u.email LIKE :term " 
				+ "OR u.username LIKE :term";
		List<?> result = em.createQuery(sql).setParameter("term", '%' + term + '%').getResultList();
		if (!result.isEmpty()) {
			for (Object obj : result) {
				list.add((User) obj);
			}
		}
		return list;
	}

	@Override
	public List<User> searchByRol(String term, String rol) {
		List<User> list = new ArrayList<User>();
		String sql;
		List<?> result;
		if(rol.compareToIgnoreCase("CLIENT")==0) {
			rol = Client.class.getName();
		} /*else if(rol.compareToIgnoreCase("OPERADOR")==0) {
			rol = Operador.class.getName();
		}*/
		sql = "FROM " + rol + " u ";
		if (term == null || term.compareToIgnoreCase("void") == 0) {
			result = em.createQuery(sql).getResultList();
		} else {
			sql += "WHERE u.email LIKE :term " 
				+ "OR u.username LIKE :term";
			result = em.createQuery(sql).setParameter("term", '%' + term + '%').getResultList();
		}
		if (!result.isEmpty()) {
			for (Object obj : result) {
				list.add((User) obj);
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Client> getAllClients() {
		return em.createQuery("FROM " + Client.class.getName() + " u ORDER BY u.username DESC").getResultList();
	}
	
}
