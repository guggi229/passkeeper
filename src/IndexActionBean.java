import java.io.Serializable;
import java.util.Locale;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
	private Test t;
	private User user;
	private Password password;

	
	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction ut;
	
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
	
	
	// Sprache
	
	
	// Allgemeine Getter und Setter
	public Test getT() {
		return t;
	}

	public void setT(Test t) {
		this.t = t;
	}

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
		return em.find(User.class, 1L);
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
