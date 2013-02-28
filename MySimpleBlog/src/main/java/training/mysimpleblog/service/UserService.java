package training.mysimpleblog.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import training.mysimpleblog.entity.Comment;
import training.mysimpleblog.entity.Post;
import training.mysimpleblog.entity.Role;
import training.mysimpleblog.entity.User;
import training.mysimpleblog.util.StringUtil;

/**
 * EJB class, that contains both DAO and business logic for authentication and
 * authorization related entitites ({@link User}, {@link Role}). Service objects
 * can be used by backing beans, but not by jsf pages.
 * 
 * @author Aleksejs Beloglazovs
 */
@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    /********************
     *** User methods ***
     ********************/

    public User findUserByUsername(String username) {
        User result;

        try {
            result = em
                    .createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            result = null;
        }

        return result;
    }

    public User findUserByEmail(String email) {
        User result;

        try {
            result = em
                    .createQuery("SELECT u FROM User u WHERE u.email = :email",
                            User.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            result = null;
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUsers() {
        Query query = em.createQuery("SELECT u FROM User u ORDER BY u.id ASC");
        return (List<User>) query.getResultList();
    }

    public void createGuestUser(User user) {
        // hashing password with SHA-256
        user.setPassword(StringUtil.sha256(user.getPassword()));

        Role role = em.find(Role.class, 3L);
        user.setRole(role);
        user.setBanned(false);
        em.persist(user);
    }

    public void updateUser(User user) {
        em.merge(user);
    }

    public void deleteUser(User user) {
        // attach user
        user = em.merge(user);

        // remove all user comments from posts
        for (Comment comment : user.getComments()) {
            Post post = comment.getPost();
            post.getComments().remove(comment);
        }

        em.remove(user);
    }

    /********************
     *** Role methods ***
     ********************/

    public Role findRole(long roleId) {
        return em.find(Role.class, roleId);
    }

    @SuppressWarnings("unchecked")
    public List<Role> findAllRoles() {
        Query query = em.createQuery("SELECT r FROM Role r ORDER BY r.id ASC");
        return (List<Role>) query.getResultList();
    }
}