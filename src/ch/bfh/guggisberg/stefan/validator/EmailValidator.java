package ch.bfh.guggisberg.stefan.validator;


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

@FacesValidator("ch.bfh.guggisberg.stefan.validator.EmailValidator")
public class EmailValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		FacesMessage msg =
				new FacesMessage("E-mail validation failed.",
						"Emailadresse exitiert bereits!");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		throw new ValidatorException(msg);
		
	}

	


}
