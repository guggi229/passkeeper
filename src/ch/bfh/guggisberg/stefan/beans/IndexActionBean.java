package ch.bfh.guggisberg.stefan.beans;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Locale;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ch.bfh.guggisberg.stefan.model.Password;
import ch.bfh.guggisberg.stefan.model.Test;
import ch.bfh.guggisberg.stefan.model.User;
@Named
@RequestScoped 
public class IndexActionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4960772482749013848L;
	private boolean isAdded = false;

	@Inject
	private User user;
	private Password password;


	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction ut;

	// Login
	// Ist hier im IndexActionBean. Schöner wäre ein CRUD Service. Für unser Projekt reicht es hier!

	public String checkLogin(){
		
		Query query = em.createNativeQuery("Select count(*) from user u WHERE useremail='" + user.getUserEmail()+ "' AND userpassword='"+user.getUserPassword()+"'");
		BigInteger result = (BigInteger) query.getSingleResult();
		if (result.intValue()>0){
			System.out.println("Eingeloggt");
		}else{
			System.out.println("Nicht eingeloggt");
		}
		return "login";
	}


	// Sprache

	private static String lang = Locale.getDefault().getDisplayLanguage();

	public void handleButtonClicked(ActionEvent event){
		isAdded=true;
		Test t1 = em.find(Test.class, 1L);
		System.out.println("Info" + t1.getTestName());
		User user = em.find(User.class, 1L);
		System.out.println("Deine Email ist : " + user.getUserEmail());
		Password p = em.find(Password.class, 1L);
		System.out.println("Passwort für " + p.getApplication());
		System.out.println("Passwort für : " + user.getPasswords().get(1).getApplication());
	}


	// addUser

	public String addUser() {
		try {
			ut.begin();
		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.persist(user);
		try {
			ut.commit();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "thanks";
	}

	public void login(){
		System.out.println("You are logged in");
	}

	public String thanks(){
		return "thanks";
	}
	public String inputData(){
		return "register";
	}

	// Allgemeine Getter und Setter

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}


	public String getLang() {
		return lang;
	}


	public void setLang(String lang) {

		this.lang = lang;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User u) {
		this.user = u;
	}


	public Password getPassword() {
		return password;
	}


	public void setPassword(Password password) {
		this.password = password;
	}


}
