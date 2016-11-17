package ch.bfh.guggisberg.stefan.beans;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.annotation.Resource;


import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ch.bfh.guggisberg.stefan.model.Password;
import ch.bfh.guggisberg.stefan.model.User2;

@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = -3511370201568906431L;

	private String  email;
	private String pass;
	private boolean loggedIn;

	// Sprache Einstellung
	private Locale locale;

	@Inject
	private User2 user;

	@Inject
	private Password password; 

	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction ut;

	@PostConstruct
	public void init() {
		locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	}

	public String checkLogin(){
		 // ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale)
		Integer result=0;
		System.out.println("Prüfe Login");
		Query query = em.createNativeQuery("Select userid from user u WHERE u.useremail='" + email + "' AND u.userpassword='" + pass + "'");
		try {
			result =  (Integer) query.getSingleResult();
			if (result.intValue()>0){
				user = em.find(User2.class, result.longValue());
				loggedIn=true;
				System.out.println("Login Status: " + loggedIn);
				return "/secured/welcome";
			}
		} catch (NoResultException e) {
			System.out.println("Nicht eingeloggt");
			loggedIn=false;
			user=null;
			// Message senden!
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"User Name oder Password falsch","loginFaild");
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, facesMsg);
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
	
	
	
	// Sprache
	// =======


	public void setLang(String langCode) {
	       locale = new Locale(langCode);
	        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		System.out.println("Eingestellte Location ist: " + FacesContext.getCurrentInstance().getViewRoot().getLocale());
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

	public Locale getLocale() {
		return locale;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}


}


