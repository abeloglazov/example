package training.mysimpleblog.backing;

import java.io.Serializable;
import java.util.Locale;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Backing bean class, that handles application language selection for given
 * user.
 * 
 * @author Aleksejs Beloglazovs
 */
@Named
@SessionScoped
public class LocaleBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LocaleBacking.class
            .getName());

    private Locale locale;

    /********************
     *** Constructors ***
     ********************/

    public LocaleBacking() {
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        logger.info(this.getClass().getSimpleName() + " created");
    }

    /***************************
     *** Getters And Setters ***
     ***************************/

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

}