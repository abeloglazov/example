package training.mysimpleblog.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.RowEditEvent;

import training.mysimpleblog.datamodel.UserDataModel;
import training.mysimpleblog.entity.Role;
import training.mysimpleblog.entity.User;
import training.mysimpleblog.service.UserService;

/**
 * Backing bean class for user management section of admin panel (jsf page
 * userList.xhtml).
 * 
 * @author Aleksejs Beloglazovs
 */
@ManagedBean
@ViewScoped
public class AdminUserBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger
            .getLogger(AdminUserBacking.class.getName());

    private List<Role> roles;
    private User[] selectedUsers;
    transient private UserDataModel users;

    @Inject
    private UserService userService;

    @Inject
    private AuthBacking authBacking;

    /**************************************
     *** Constructors And Inintializers ***
     **************************************/

    public AdminUserBacking() {
        logger.info(this.getClass().getSimpleName() + " created");
    }

    @PostConstruct
    private void init() {
        users = new UserDataModel(userService.findAllUsers());
        roles = new ArrayList<Role>();
        roles.add(userService.findRole(2L));
        roles.add(userService.findRole(3L));
        logger.info(this.getClass().getSimpleName() + " initialized");
    }

    /***************************
     *** Getters And Setters ***
     **************************/

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public User[] getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(User[] selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public UserDataModel getUsers() {
        return users;
    }

    /************************
     *** Business Methods ***
     ************************/

    public void deleteUsers(ActionEvent actionEvent) {
        for (User user : this.selectedUsers) {
            // can't delete Administrator
            if (authBacking.isAdmin(user)) {
                continue;
                // can't delete self
            } else if (authBacking.isCurrentUser(user)) {
                continue;
            } else {
                userService.deleteUser(user);
            }
        }

        // refresh data in table
        users.setWrappedData(this.userService.findAllUsers());
    }

    public void banUsers(ActionEvent actionEvent) {
        for (User user : this.selectedUsers) {
            // can't ban Administrator
            if (authBacking.isAdmin(user)) {
                continue;
                // bloggers can't ban other bloggers
            } else if (authBacking.isCurrentBlogger()
                    && user.getRole().getName().equals("Blogger")) {
                continue;
                // can't ban self
            } else if (authBacking.isCurrentUser(user)) {
                continue;
            } else {
                user.setBanned(true);
                userService.updateUser(user);
            }
        }

        // refresh data in table
        users.setWrappedData(this.userService.findAllUsers());
    }

    public void onEdit(RowEditEvent event) {
        User user = (User) event.getObject();
        userService.updateUser(user);
    }
}
