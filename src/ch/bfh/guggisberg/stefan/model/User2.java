package ch.bfh.guggisberg.stefan.model;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;


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
@NamedNativeQueries({
	@NamedNativeQuery(name=User2.QUERY_COUNT_EMAIL_ADRESSE,query="SELECT COUNT(*) FROM bfhschema.user where userEmail='123'")
}) 
public class User2 implements Serializable {

	private static final long serialVersionUID = -1330912948199950826L;
	public static final String QUERY_COUNT_EMAIL_ADRESSE="QUERY_COUNT_EMAIL_ADRESSE";
		
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

	

}
