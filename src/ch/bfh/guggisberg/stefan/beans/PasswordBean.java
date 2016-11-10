package ch.bfh.guggisberg.stefan.beans;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class PasswordBean implements Serializable {
//NewUserBean
	private static final long serialVersionUID = 6684002997452244642L;

	private String password;
	private String application;
	
	
	
	private List<PasswordBean> passwords;
	private boolean showThankYouMessage = false;
	
	
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public List<PasswordBean> getPasswords() {
		return passwords;
	}
	public void setPasswords(List<PasswordBean> passwords) {
		this.passwords = passwords;
	}
	public boolean isShowThankYouMessage() {
		return showThankYouMessage;
	}
	public void setShowThankYouMessage(boolean showThankYouMessage) {
		this.showThankYouMessage = showThankYouMessage;
	}
	
	
	
}
