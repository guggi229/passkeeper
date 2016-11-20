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
	private static final long serialVersionUID = -4960772482749013848L;

	@Inject
	private User2 user2;			// Version 2 des Users

	@Inject
	private Password password;

	@Inject
	private Password pass;

	// Kommunikation zur DB und Persitierung
	@PersistenceContext
	private static EntityManager em;

	@Resource
	private UserTransaction ut;

	// Divers
	private String newPassword;

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
		// User / Password zusammenführen 
		//user2.addPassword(password);
		//password.setUser(user2);		

		// Alles Persistieren
		try {
			ut.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		Password t = new Password(); //Warum müssen die Atribute mit Set gesetzt werden? em.persist(password); müsste doch auch gehen?
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
		showGlobalMessage("Das Password wurde erfolgreich gespeichert", "gespeichert");
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
		System.out.println("Zu löschendes Passwort: " + p.getId() );
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
			
			showGlobalMessage("Das Password wurde erfolgreich gelöscht", "löschen");
		}else{
			System.out.println("Löschen nicht möglich. ID: " + p.getId() );
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
		int r = query.executeUpdate();
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showGlobalMessage("Die neunen Daten wurden erfolgreich gespeichert!", "saveOK");
		password=null;
		return "list";
	}

	//	public String savePassword(Password p){
	//		System.out.println("Daten:" + p.getPassword());
	//		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
	//		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
	//		user2 = em.find(User2.class, lg.getUser().getId());
	//		Password temp = em.find(Password.class, p.getId());				// Lade altes Passwort
	//		System.out.println("Password mit ID " + p.getId() + " geladen");
	//		System.out.println("Neue Daten: ");
	//		System.out.println(p.getDescription());
	//		System.out.println(p.getLogin());
	//		System.out.println(p.getPassword());
	//		System.out.println(p.getId());
	//		System.out.println(p.getUser());
	//		System.out.println("Alte Daten:");
	//		System.out.println(temp.getDescription());
	//		System.out.println(temp.getLogin());
	//		System.out.println(temp.getPassword());
	//		System.out.println(temp.getId());
	//		System.out.println(temp.getUser());
	//		
	//		temp = p;														// Schreibe neue daten zum Passwort
	//		try {
	//			ut.begin();
	//		} catch (NotSupportedException | SystemException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		em.merge(user2);
	//		em.merge(temp);
	//		lg.setUser(user2);
	//		try {
	//			ut.commit();
	//		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
	//				| HeuristicRollbackException | SystemException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//
	//		// Message schreiben
	//		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Das Password wurde erfolgreich geändert", "change");
	//		FacesContext fc = FacesContext.getCurrentInstance();
	//		fc.addMessage(null, facesMsg);
	//
	//		return "list";
	//
	//	}


	// ********************************************************************************
	// Messages
	// ========
	// ********************************************************************************

	public void showGlobalMessage(String message, String key){
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,message, key);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, facesMsg);
	}

	// ********************************************************************************
	// Sprache
	// =======
	// ********************************************************************************

	public void changeLang(String langCode) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale (langCode));
		System.out.println("Eingestellte Location ist: " + FacesContext.getCurrentInstance().getViewRoot().getLocale());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "thanks";
	}
	public String updateUser(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
		LoginBean lg = (LoginBean) session.getAttribute("loginBean");
		if (lg==null) return "login"; 	
		System.out.println("Neues Password: " + newPassword);
		// Prüfe, ob User berechtigt ist

		// To do

		// Das Passwort im Formular ist absichtlich nicht gebunden. So wird beim laden des Formulars, das Password nicht angezeigt.
		lg.getUser().setUserPassword(newPassword);

		// Neue Daten mergen
		try {
			ut.begin();
		} catch (NotSupportedException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		em.merge(lg.getUser());
		lg.setUser(lg.getUser());
		try {
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showGlobalMessage("Deine Daten wurden gespeichert!", "Dataok");
		return "home";
	}

	// ********************************************************************************
	// Generelle Sachen
	// =================
	// ********************************************************************************

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


	public Password getPass() {
		return pass;
	}

	public void setPass(Password pass) {
		this.pass = pass;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
