package ch.bfh.guggisberg.stefan.beans;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
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

	@Inject
	private Password pass;

	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private static EntityManager em;

	@Resource
	private UserTransaction ut;

	// Passwort 
	// =========


	// Passwort einf�gen
	//-------------------

	public String addPassword(){

		// User Suchen
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 		
		System.out.println("User: " + lg.getEmail());
		System.out.println("Neues Password:");
		System.out.println(password.getDescription());

		// Password speichern
		// ==================

		// Entity des Users laden

		user2 = em.find(User2.class, lg.getUser().getId());
		System.out.println("Folgender User aus der Session geladen:" + user2.getUserEmail());

		System.out.println("Passwort Daten: ");
		System.out.println(password.getDescription());
		System.out.println(password.getLogin());
		System.out.println(password.getPassword());
		// User / Password zusammenf�hren 
		//user2.addPassword(password);
		//password.setUser(user2);		

		// Alles Persistieren
		try {
			ut.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		Password t = new Password(); //Warum m�ssen die Atribute mit Set gesetzt werden? em.persist(password); m�sste doch auch gehen?
		t.setDescription(password.getDescription());
		t.setLogin(password.getLogin());
		t.setPassword(password.getPassword());
		t.setUser(user2);
		user2.addPassword(t);

		em.merge(user2);
		em.persist(t);
		lg.setUser(user2);


		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Message senden!
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Das Password wurde erfolgreich gespeichert", "gespeichert");
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, facesMsg);

		password=null;
		return "list";
	}

	//Passwort l�schen
	// ----------------
	public String deletePassword(Password p){
		// User Suchen
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 	
		Query query = em.createNativeQuery("Delete FROM Passwords WHERE passwordid=" + p.getId());
		try {
			ut.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int deletedCount = query.executeUpdate();
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (deletedCount>0){
			// Message senden!
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Das Password wurde erfolgreich gel�scht", "l�schen");
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, facesMsg);
		}else{
			System.out.println("L�schen nicht m�glich. ID: " + p.getId() );
		}
		return "list";
	}



	//Passwort �ndern
	// ----------------
	public String savePassword(){
		return "list";
	}



	// Login
	// Ist hier im IndexActionBean. Sch�ner w�re ein CRUD Service. F�r unser Projekt reicht es hier!

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


	public void changeLang(String langCode) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale (langCode));
		System.out.println("Eingestellte Location ist: " + FacesContext.getCurrentInstance().getViewRoot().getLocale());
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


	public Password getPass() {
		System.out.println("********************************************************** Get Pass");
		return pass;
	}

	public void setPass(Password pass) {
		System.out.println("********************************************************** Set Pass");
		this.pass = pass;
	}

}
