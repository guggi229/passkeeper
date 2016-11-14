package ch.bfh.guggisberg.stefan.beans;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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
import ch.bfh.guggisberg.stefan.model.User;
import ch.bfh.guggisberg.stefan.model.User2;
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

	@Inject
	private User2 user2;

	@Inject
	private Password password;


	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private static EntityManager em;

	@Resource
	private UserTransaction ut;

	// Passwort 
	// =========


	// Passwort einfügen
	//-------------------

	public String addPassword(){
		
		//
		//		Password temp = new Password("1","2","3");
		//		user.addPassword(temp);
		//		temp.setUser(user);
		//		em.persist(user);
		//		em.persist(temp);
		//		System.out.println("Start Liste:");
		//		List<Password> mylist = user.getPasswords();
		//		for (Password password : mylist) {
		//			System.out.println(password.getDescription());
		//		}
		//		System.out.println("Ende Liste:");
		//		System.out.println("Gespeichert");
		//		try {
		//			ut.commit();
		//		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
		//				| HeuristicRollbackException | SystemException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		System.out.println("Username2: " + user.getUserName());

		return "list";
	}

	//Passwort löschen
	// ----------------
	public String deletePassword(){
		System.out.println("Zu löschendem Objekt" + password.getDescription());
		return "list";

	}
	
	
	
	//Passwort ändern
	// ----------------
	public String savePassword(){
		return "list";
	}



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
	// =======


	public String changeLang(String langCode) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale (langCode));
		System.out.println("Eingestellte Location ist: " + FacesContext.getCurrentInstance().getViewRoot().getLocale());
		return null;
	}


	// addUser
	// ========

	public String addUser()  {
		
		try {
			ut.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User2 t = new User2();
		t.setUserEmail(user2.getUserEmail());
		t.setUserName(user2.getUserName());
		t.setUserPassword(user2.getUserPassword());
		em.persist(t);
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		return "thanks";
	}
	public void checkEmail(FacesContext context, UIComponent component, Object value){

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
	// ============================

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
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

	public User2 getUser2() {
		return user2;
	}

	public void setUser2(User2 user2) {
		this.user2 = user2;
	}

	public static EntityManager getEntityManager(){
		return em;
	}

}
