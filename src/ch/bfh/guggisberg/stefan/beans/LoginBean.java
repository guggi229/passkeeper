package ch.bfh.guggisberg.stefan.beans;

import java.io.Serializable;
import java.math.BigInteger;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import ch.bfh.guggisberg.stefan.model.User;

@ManagedBean
@SessionScoped
@Named
public class LoginBean implements Serializable {
	private static final long serialVersionUID = -3511370201568906431L;

	private boolean loggedIn;

	@Inject
	private User user;

	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction ut;


	public String checkLogin(){
		System.out.println("Prüfe Login");
		System.out.println("Email: "+user.getUserEmail());
		System.out.println("Passwort: "+user.getPasswords()); // Darf nie so benutzt werden. Ist rein als Schulprojekt!

		//		if((user==null) || (user.getUserEmail()==null) || (user.getPasswords()==null) ){ 
		//			System.out.println("Userobjekt ist null oder propertys davon");
		//			return "login";
		// Sollte nicht notwendig sein. 
		//		}
		//Query query = em.createNativeQuery("Select userid from user u WHERE useremail='" + user.getUserEmail()+ "' AND userpassword='"+user.getUserPassword()+"'");
		// Query query = em.createNativeQuery("Select count(*) from user u WHERE u.useremail='" + user.getUserEmail()+ "'");
		Query query = em.createNativeQuery("Select userid from user u WHERE u.useremail='" + user.getUserEmail()+ "'");
		Integer result =  (Integer) query.getSingleResult();
		System.out.println( "ID: " +result);
		if (result.intValue()>0){
			System.out.println("Eingeloggt mit ID: " + result);
			user = em.find(User.class, result.longValue());
			System.out.println("User " +user.getUserName() + " hat eingeloggt");
			loggedIn=true;
			System.out.println("Login Status: " + loggedIn);
			return "/secured/welcome";
		}else{
			System.out.println("Nicht eingeloggt");
		}

		// Set login ERROR
//		FacesMessage msg = new FacesMessage("Login error!", "ERROR MSG");
//		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//		FacesContext.getCurrentInstance().addMessage(null, msg);

		return "login";
	}

	public String doLogout(){


		// Set the paremeter indicating that user is logged in to false
		loggedIn = false;
		// Set logout message
//		FacesMessage msg = new FacesMessage("Logout success!", "INFO MSG");
//		msg.setSeverity(FacesMessage.SEVERITY_INFO);
//		FacesContext.getCurrentInstance().addMessage(null, msg);


		return "index";
	}
	public boolean isLogged(){
		return loggedIn;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}


}


