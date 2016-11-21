package ch.bfh.guggisberg.stefan.beans;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
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
import ch.bfh.guggisberg.stefan.model.User2;
@Named
@RequestScoped
public class IndexActionBean implements Serializable {
	private static final long serialVersionUID = -4960772482749013848L;

	@Inject
	private User2 user2;			// Version 2 des Users

	@Inject
	private Password password;

	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private static EntityManager em;

	@Resource
	private UserTransaction ut;

	// Divers
	private String newPassword;
	private String oldPassword;

	// ********************************************************************************
	// Passwort 
	// =========
	// ********************************************************************************


	// Passwort einfügen
	//-------------------

	public String addPassword(){
		// User Suchen
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 		

		// Password speichern
		// ==================

		// Entity des Users laden

		user2 = em.find(User2.class, lg.getUser().getId());
		// Alles Persistieren
		try {
			ut.begin();
			Password t = new Password(); 
			t.setDescription(password.getDescription());
			t.setLogin(password.getLogin());
			t.setPassword(password.getPassword());
			t.setUser(user2);
			user2.addPassword(t);
			em.merge(user2);
			em.persist(t);
			lg.setUser(user2);
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Message senden!
		showGlobalMessage(getText("info.passwordSaved"), "gespeichert");
		password=null;
		return "list";
	}

	//Passwort löschen
	// ----------------
	public String deletePassword(Password p){
		// User Suchen
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 	
		Query query = em.createNativeQuery("Delete FROM Passwords WHERE passwordid=" + p.getId());
		user2 = em.find(User2.class, lg.getUser().getId());
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
		user2 = em.find(User2.class, lg.getUser().getId());
		lg.setUser(user2);
		if (deletedCount>0){
			// Message senden!
			showGlobalMessage(getText("info.passwordDeleted"), "löschen");
		}
		return "list";
	}
	public String editPassword(Password p){
		password = p;
		return "edit";
	}
	public String savePassword(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 

		try {
			ut.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query query = em.createNativeQuery("Update bfhschema.passwords SET passworddata = '" + password.getPassword() + "' ,passworddesc = '" + password.getDescription() + "' ,passwordlogin='" +password.getLogin() + "' WHERE passwordid =" + password.getId());
		query.executeUpdate();
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User2 u = em.find(User2.class, lg.getUser().getId());
		lg.setUser(u);
		
		
		showGlobalMessage(getText("info.UserDataSaved"), "saveOK");
		password=null;
		return "list";
	}


	// ********************************************************************************
	// Messages
	// ========
	// ********************************************************************************

	public void showGlobalMessage(String message, String key){
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,message, key);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, facesMsg);
	}

	public void showGlobalErrorMessage(String message, String key){
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,message, key);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, facesMsg);
	}
	
	public String getText(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String messageBundleName = facesContext.getApplication().getMessageBundle();
		Locale locale = facesContext.getViewRoot().getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
		return bundle.getString(key);
	}
	

	// ********************************************************************************
	// User
	// ====
	// ********************************************************************************

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
			e.printStackTrace();
		}
		return "thanks";
	}
	public String updateUser(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 	
		// Prüfe, ob User berechtigt ist
		// Falls der User seine Emailadresse ändert, muss geprüft werden, ob diese Adresse bereits existiert. 
		//Analog "NewUser". Jedoch wissen wir, dass der Datensatz bereits in der DB ist. Daher etwas ein anderes Verfahren

		Query query = em.createQuery("FROM User2 WHERE userEmail ='" + lg.getUser().getUserEmail()+"'");
		@SuppressWarnings("unchecked")
		List<User2> userWithThisEmail = query.getResultList(); // Liste kann nicht null sein
		for (User2 user : userWithThisEmail) {
			// Hier wird geprüft, ob die bereits existierende EMail zum User oder zu einer anderen Person gehört.
			// Hat ein User diese Email schon, wird ein Fehler generiert
			if (user.getUserEmail().equals(lg.getUser().getUserEmail()) && (user.getId()==lg.getUser().getId())  ){
				
				System.out.println("Session Password:  " + lg.getUser().getUserPassword());
				System.out.println("Aus der DB:" + user.getUserPassword());
				if(!lg.getUser().getUserPassword().equals(oldPassword)){
					// Passwort stimmt nicht!
					showGlobalErrorMessage(getText("err.passwordWrong"), "error with Password");
					return "home";
				}
			}
			else {
				// In diesem Falle hat ein fremder User bereits diese Email
				showGlobalErrorMessage(getText("err.emailAlreadyExist"), "error with EMail");
				return "home";
			}
		}

		// Das Passwort im Formular ist absichtlich nicht gebunden. So wird beim laden des Formulars, das Password nicht angezeigt.
		lg.getUser().setUserPassword(newPassword);

		// Jetzt muss noch geprüft werden, ob das alte Passwort stimmt! --> Security


		// Neue Daten mergen
		try {
			ut.begin();
			em.merge(lg.getUser());
			lg.setUser(lg.getUser());
		} 
		catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showGlobalMessage(getText("info.UserDataSaved"), "Dataok");
		oldPassword=null;
		newPassword=null;
		return "home";
	}


	// ********************************************************************************
	// Getter / Setter
	// ===============
	// ********************************************************************************


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
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
