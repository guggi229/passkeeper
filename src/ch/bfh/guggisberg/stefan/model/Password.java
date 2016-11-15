package ch.bfh.guggisberg.stefan.model;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import javax.persistence.Column;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@RequestScoped
@Entity
@Table(name="passwords")
@Named
public class Password implements Serializable {

	/**
	 * Repräsentiert die Tabelle 'Password' in der DB
	 */
	private static final long serialVersionUID = -9170472267165197806L;
	@Id
	@Column(name="passwordid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="passworddesc")
	private String description;

	@Column(name="passworddata") // Das eigentliche Passwort
	private String password;
	
	@Column(name="passwordlogin")
	private String passwordlogin;

	//  Fremdkeys
//	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "useridfk", nullable = false)
	private User2 user;
	
	
	// Kosntruktor
	public Password(){

	}
	public Password(String desc, String password, String login){
		super();
		this.description = desc;
		this.password = password;
		this.passwordlogin=login;
	}

	// Setter Getter
	// =============

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogin() {
		return passwordlogin;
	}
	public void setLogin(String login) {
		this.passwordlogin = login;
	}
	public User2 getUser() {
		return user;
	}
	public void setUser(User2 user) {
		this.user = user;
	}
	

}
