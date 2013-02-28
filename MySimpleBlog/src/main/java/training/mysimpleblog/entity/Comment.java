package training.mysimpleblog.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class, that represents user's comment for some post.
 * 
 * @author Aleksejs Beloglazovs
 */
@Entity
@Table(name = "\"COMMENT\"")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String body;
    private Calendar creationDate;
    private Calendar updatedDate;
    private Post post;
    private User user;

    public Comment() {
    }

    @Id
    @SequenceGenerator(name = "COMMENT_ID_GENERATOR", 
                        sequenceName = "COMMENT_ID_SEQ", 
                        initialValue = 1, 
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_ID_GENERATOR")
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "\"BODY\"")
    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATE")
    public Calendar getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }

    // bi-directional many-to-one association to Post
    @ManyToOne(fetch = FetchType.LAZY)
    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    // bi-directional many-to-one association to User
    @ManyToOne(fetch = FetchType.LAZY)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Comment other = (Comment) obj;
        if (id != other.id)
            return false;
        return true;
    }

}