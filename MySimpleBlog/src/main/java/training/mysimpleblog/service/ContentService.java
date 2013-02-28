package training.mysimpleblog.service;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import training.mysimpleblog.entity.Comment;
import training.mysimpleblog.entity.Post;
import training.mysimpleblog.entity.Setting;
import training.mysimpleblog.entity.Tag;
import training.mysimpleblog.entity.User;

/**
 * EJB class, that contains both DAO and business logic for content related
 * entitites ({@link Post}, {@link Comment}, {@link Tag}, {@link Setting}).
 * Service objects can be used by backing beans, but not by jsf pages.
 * 
 * @author Aleksejs Beloglazovs
 */
@Stateless
public class ContentService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserService userService;

    /********************
     *** Post methods ***
     ********************/

    public Post findPost(long postId) {
        Post post = em.find(Post.class, postId);
        return post;
    }

    @SuppressWarnings("unchecked")
    public List<Post> findPostsByPage(int page, int size) {
        Query query = em
                .createQuery("SELECT p FROM Post p ORDER BY p.creationDate DESC");
        int offset = (page - 1) * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);
        return (List<Post>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Post> findPostsByTagAndPage(String tagName, int page, int size) {
        Query query = em
                .createQuery("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tName ORDER BY p.creationDate DESC");
        query.setParameter("tName", tagName);
        int offset = (page - 1) * size;
        query.setFirstResult(offset);
        query.setMaxResults(size);
        return (List<Post>) query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Post> findAllPosts() {
        Query query = em
                .createQuery("SELECT p FROM Post p ORDER BY p.creationDate DESC");
        return (List<Post>) query.getResultList();
    }

    public int countPostsByTag(String tagName) {
        Query query = em
                .createQuery("SELECT count(p) FROM Post p JOIN p.tags t WHERE t.name = :tName");
        query.setParameter("tName", tagName);
        Number count = (Number) query.getSingleResult();
        return count.intValue();
    }

    public int countAllPosts() {
        Query query = em.createQuery("SELECT count(p) FROM Post p");
        Number count = (Number) query.getSingleResult();
        return count.intValue();
    }

    public void createPost(Post post, User user) {
        post.setCreationDate(Calendar.getInstance());
        post.setUser(user);
        em.merge(post);
    }

    public void updatePost(Post post) {
        em.merge(post);
    }

    public void deletePost(Post post) {
        em.remove(em.merge(post));
    }

    /***********************
     *** Comment methods ***
     ***********************/

    public Comment findComment(long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        return comment;
    }

    public void createComment(Comment comment, Post post, User user) {
        comment.setCreationDate(Calendar.getInstance());
        comment.setUser(user);
        comment.setPost(post);
        post.getComments().add(comment);
        em.merge(post);
    }

    public void updateComment(Comment comment) {
        comment.setUpdatedDate(Calendar.getInstance());
        em.merge(comment);
    }

    public void deleteComment(Comment comment) {
        Post post = comment.getPost();
        em.remove(em.merge(comment));
        post.getComments().remove(comment);
        em.merge(post);
    }

    /*******************
     *** Tag methods ***
     *******************/

    public Tag findTagByName(String name) {
        Tag result;

        try {
            result = em
                    .createQuery("SELECT t FROM Tag t WHERE t.name = :name",
                            Tag.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException ex) {
            result = null;
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findCountedTags() {
        Query query = em
                .createQuery("SELECT t.name, count(t.id) FROM Tag t JOIN t.posts p GROUP BY t.name ORDER BY t.name");
        return (List<Object[]>) query.getResultList();

    }

    /***********************
     *** Setting methods ***
     ***********************/

    @SuppressWarnings("unchecked")
    public List<Setting> findAllSettings() {
        Query query = em.createQuery("SELECT s FROM Setting s");
        return (List<Setting>) query.getResultList();
    }

    public Setting findSettingByName(String name) {
        Setting result;

        try {
            result = em
                    .createQuery(
                            "SELECT s FROM Setting s WHERE s.name = :name",
                            Setting.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException ex) {
            result = null;
        }

        return result;
    }

    public void updateSettings(List<Setting> settings) {
        for (Setting setting : settings) {
            em.merge(setting);
        }
    }
}