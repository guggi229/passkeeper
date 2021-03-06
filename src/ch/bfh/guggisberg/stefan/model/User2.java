package ch.bfh.guggisberg.stefan.model;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


;


/**
 * Dieses Model repräsentiert den User
 * 
 * @author guggi229
 *
 */
@Named
@RequestScoped
@Entity
@Table(name="user")
public class User2 implements Serializable {

	private static final long serialVersionUID = -1330912948199950826L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="userid")
	private Long id;

	@Column(name="username")
	private String  userName;

	@Column(name="userpassword")
	private String userPassword;

	@Column(name="useremail")
	private String userEmail;

	@OneToMany(fetch=FetchType.EAGER, mappedBy="user")
	private List <Password> passwords; 


	// Gette Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public List<Password> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<Password> passwords) {
		this.passwords = passwords;
	}
	public void addPassword(Password pass){
		this.passwords.add(pass);
	}


}
