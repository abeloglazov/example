package training.mysimpleblog.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity class, that represents user of the application. Contains lists of
 * {@link Comment} and {@link Post} made by given user.
 * 
 * @author Aleksejs Beloglazovs
 */
@Entity
@Table(name = "\"USER\"")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String email;
    private String password;
    private String username;
    private char banned;
    private List<Comment> comments;
    private List<Post> posts;
    private Role role;

    public User() {
    }

    @Id
    @SequenceGenerator(name = "USER_ID_GENERATOR", 
                        sequenceName = "USER_ID_SEQ", 
                        initialValue = 1,
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GENERATOR")
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private char getBanned() {
        return this.banned;
    }

    private void setBanned(char banned) {
        this.banned = banned;
    }

    @Transient
    public boolean isBanned() {
        return getBanned() == 'Y';
    }

    public void setBanned(boolean banned) {
        setBanned(banned ? 'Y' : 'N');
    }

    // bi-directional many-to-one association to Comment
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // bi-directional many-to-one association to Post
    @OneToMany(mappedBy = "user")
    public List<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    // bi-directional many-to-one association to Role
    @ManyToOne(fetch = FetchType.LAZY)
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

}