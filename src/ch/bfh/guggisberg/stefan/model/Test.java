package ch.bfh.guggisberg.stefan.model;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@RequestScoped
@Table(name="user")
public class Test implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -166682036597231670L;

	@Column(name="username")
	private String  testName;
	
	@Id
	@Column(name="userid")
	private Long id;
	

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
