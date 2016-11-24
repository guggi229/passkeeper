package ch.bfh.guggisberg.stefan.validator;


import java.math.BigInteger;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Validiert die Emailadresse und prüft, ob sie bereits existiert
 * @author guggi229
 *
 */

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ch.bfh.guggisberg.stefan.beans.IndexActionBean;

@FacesValidator("ch.bfh.guggisberg.stefan.validator.EmailValidator")
public class EmailValidator implements Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
			"[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
			"(\\.[A-Za-z]{2,})$";

	private Pattern pattern;
	private Matcher matcher;

	public EmailValidator(){
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		// Prüfen, ob die Emailadresse im richtigen Format ist
		// Wird auch auf der Clientseite gemacht!

		matcher = pattern.matcher(value.toString());
		if(!matcher.matches()){

			FacesMessage msg =
					new FacesMessage("E-mail validation failed.",
							"Die Emaailadresse hat ein falsches Format!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}

		// Prüfen, ob diese Emailadresse bereits existiert!

		EntityManager em = IndexActionBean.getEntityManager();

		Query q= em.createNativeQuery("SELECT COUNT(*) FROM bfhschema.user where userEmail=:email");
		q.setParameter("email", value.toString());
		BigInteger count = (BigInteger) q.getSingleResult();
		if(count.intValue()>0){

			FacesMessage msg =
					new FacesMessage("E-mail validation failed.",
							getText("err.emailAlreadyExist"));
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

	}
	// ********************************************************************************
	// Messages
	// ========
	// ********************************************************************************
	
	public String getText(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String messageBundleName = facesContext.getApplication().getMessageBundle();
		Locale locale = facesContext.getViewRoot().getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
		return bundle.getString(key);
	}



}
