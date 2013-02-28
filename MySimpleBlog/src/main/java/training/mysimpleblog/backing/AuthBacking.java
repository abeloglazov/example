package training.mysimpleblog.backing;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import training.mysimpleblog.entity.Comment;
import training.mysimpleblog.entity.User;
import training.mysimpleblog.service.UserService;

/**
 * Backing bean class, that handles user authentification related actions.
 * 
 * @author Aleksejs Beloglazovs
 */
@Named
@RequestScoped
public class AuthBacking implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AuthBacking.class
            .getName());

    private User user;

    @Inject
    private UserService userService;

    /********************
     *** Constructors ***
     ********************/

    public AuthBacking() {
        this.user = new User();
        logger.info(this.getClass().getSimpleName() + " created");
    }

    /***************************
     *** Getters And Setters ***
     ***************************/

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /************************
     *** Business Methods ***
     ************************/

    public void signUp() {
        if (userService.findUserByUsername(this.user.getUsername()) != null) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "User with such username already exists", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (userService.findUserByEmail(this.user.getEmail()) != null) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "User with such email already exists", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            userService.createGuestUser(this.user);
        }
        this.user = new User();
    }

    public String signIn() {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();

        try {
            request.login(this.user.getUsername(), this.user.getPassword());

            if (getCurrentUser().isBanned()) {
                request.logout();
                FacesMessage message = new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "This account is banned",
                        "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }

            if (this.isLoggedIn()) {
                return "/feed?faces-redirect=true";
            }
        } catch (ServletException e) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Username/password are incorrect or you are already signed in",
                    "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return "";
    }

    public String signOut() {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return "/feed?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        return request.getRemoteUser() != null;
    }

    public boolean isAdmin(User user) {
        return (user != null && user.getRole().getName()
                .equals("Administrator"));
    }

    public boolean isCurrentAdmin() {
        return this.isInRole("Administrator");
    }

    public boolean isCurrentBlogger() {
        return this.isInRole("Blogger");
    }

    public boolean isCurrentStaff() {
        return (this.isInRole("Administrator") || this.isInRole("Blogger"));
    }

    public boolean isCurrentUser(User user) {
        User currentUser = getCurrentUser();
        return (user != null && user.equals(currentUser));
    }

    public boolean isCurrentCommentAuthor(Comment comment) {
        if (isLoggedIn()) {
            User currentUser = getCurrentUser();
            return comment.getUser().equals(currentUser);
        }

        return false;
    }

    public User getCurrentUser() {
        return userService.findUserByUsername(this.getCurrentUsername());
    }

    private boolean isInRole(String roleName) {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        return request.isUserInRole(roleName);
    }

    private String getCurrentUsername() {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        return request.getRemoteUser();
    }
}
