package training.mysimpleblog.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * Entity class, that describes {@link Post}. Contains list of {@link Post}
 * described by given tag.
 * 
 * @author Aleksejs Beloglazovs
 */
@Entity
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private List<Post> posts;

    public Tag() {
    }

    @Id
    @SequenceGenerator(name = "TAG_ID_GENERATOR", 
                        sequenceName = "TAG_ID_SEQ", 
                        initialValue = 1, 
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAG_ID_GENERATOR")
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // bi-directional many-to-many association to Post
    @ManyToMany(mappedBy = "tags")
    public List<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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
        Tag other = (Tag) obj;
        if (id != other.id)
            return false;
        return true;
    }

}