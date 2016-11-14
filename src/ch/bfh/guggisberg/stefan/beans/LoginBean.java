package ch.bfh.guggisberg.stefan.beans;

import java.io.Serializable;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.annotation.Resource;


import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;


import ch.bfh.guggisberg.stefan.model.User2;

@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = -3511370201568906431L;

	private String  email;
	private String pass;
	private boolean loggedIn;

	@Inject
	private User2 user;


	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction ut;


	public String checkLogin(){
		System.out.println("Prüfe Login");
		Query query = em.createNativeQuery("Select userid from user u WHERE u.useremail='" + email + "'");
		Integer result =  (Integer) query.getSingleResult();
		if (result.intValue()>0){
			user = em.find(User2.class, result.longValue());
			loggedIn=true;
			System.out.println("Login Status: " + loggedIn);
			return "/secured/welcome";
		}else{
			System.out.println("Nicht eingeloggt");
			loggedIn=false;
			user=null;
		}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public User2 getUser() {
		return user;
	}

	public void setUser(User2 user) {
		this.user = user;
	}


}


