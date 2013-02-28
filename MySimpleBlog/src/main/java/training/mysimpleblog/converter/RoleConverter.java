package training.mysimpleblog.converter;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import training.mysimpleblog.entity.Role;
import training.mysimpleblog.service.UserService;

/**
 * Jsf converter class for {@link Role}
 * 
 * @author Aleksejs Beloglazovs
 */
@Named
@ApplicationScoped
public class RoleConverter implements Converter {

    @Inject
    private UserService userService;

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        Role role = (Role) value;
        return String.valueOf(role.getId());
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        return userService.findRole(Long.valueOf(value));
    }

}
