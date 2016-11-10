package ch.bfh.guggisberg.stefan.model;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@RequestScoped
@Table(name="passwords")
public class Password implements Serializable {

	/**
	 * Repräsentiert die Tabelle 'Password' in der DB
	 */
	private static final long serialVersionUID = -9170472267165197806L;
	@Id
	@Column(name="passwordid")
	private Long Id;

	@Column(name="passworddesc")
	private String description;

	@Column(name="passworddata")
	private String password;
	
	@Column(name="passwordlogin")
	private String passwordlogin;

	//  Fremdkeys
//	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "useridfk", nullable = false)
	private User user;
	
	
	// Kosntruktor
	public Password(){

	}
	public Password(String application, String password){
		super();
		this.description = application;
		this.password = password;
	}

	// Setter Getter
	// =============

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getApplication() {
		return description;
	}

	public void setApplication(String application) {
		this.description = application;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	

}
