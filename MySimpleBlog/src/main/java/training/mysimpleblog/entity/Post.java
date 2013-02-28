package training.mysimpleblog.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class, that represents blog entry. Contains list of {@link Comment}
 * made by users for given post and list of {@link Tag}, that briefly describes
 * given post.
 * 
 * @author Aleksejs Beloglazovs
 */
@Entity
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String body;
    private Calendar creationDate;
    private String title;
    private List<Comment> comments;
    private User user;
    private List<Tag> tags;

    public Post() {
    }

    @Id
    @SequenceGenerator(name = "POST_ID_GENERATOR", 
                        sequenceName = "POST_ID_SEQ", 
                        initialValue = 1, 
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_ID_GENERATOR")
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Lob
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // bi-directional many-to-one association to Comment
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @OrderBy("creationDate")
    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // bi-directional many-to-one association to User
    @ManyToOne(fetch = FetchType.LAZY)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // bi-directional many-to-many association to Tag
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "POST_TAG", 
                joinColumns = { @JoinColumn(name = "POST_ID", referencedColumnName = "ID") }, 
                inverseJoinColumns = { @JoinColumn(name = "TAG_ID", referencedColumnName = "ID") })
    @OrderBy("name")
    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
        Post other = (Post) obj;
        if (id != other.id)
            return false;
        return true;
    }

}