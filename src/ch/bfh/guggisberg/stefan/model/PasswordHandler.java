package ch.bfh.guggisberg.stefan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import ch.bfh.guggisberg.stefan.beans.PasswordBean;

@ApplicationScoped
public class PasswordHandler implements Serializable {
	private static final long serialVersionUID = -2132913914189973677L;

	private List<Password> passwords = new ArrayList<>();
	
	public void add(PasswordBean bean){
		Password password = new Password(bean.getApplication(), bean.getPassword());
		passwords.add(password);
	}

	public List<Password> getPasswords() {
		return passwords;
	}

}
